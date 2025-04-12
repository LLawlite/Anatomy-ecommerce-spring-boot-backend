package com.ecommerce.anatomy.security.oauth2;

import com.ecommerce.anatomy.model.AppRole;
import com.ecommerce.anatomy.model.Role;
import com.ecommerce.anatomy.model.User;
import com.ecommerce.anatomy.repositories.RoleRepository;
import com.ecommerce.anatomy.repositories.UserRepository;
import com.ecommerce.anatomy.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUserName(name != null ? name : email.split("@")[0]);
            newUser.setPassword("");

            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role USER not found."));
            userRole = entityManager.merge(userRole); // Merges the role into the current session
            newUser.addRole(userRole);

            return userRepository.save(newUser);
        });

        String jwt = jwtUtils.generateTokenFromEmail(email);

        String redirectUrl = String.format(
                "http://localhost:3000/oauth2/redirect?token=%s&username=%s&id=%d&roles=%s",
                jwt,
                URLEncoder.encode(user.getUserName(), StandardCharsets.UTF_8),
                user.getUserId(),
                URLEncoder.encode(user.getRoles().stream()
                        .map(role -> role.getRoleName().name())
                        .collect(Collectors.joining(",")), StandardCharsets.UTF_8)
        );

        response.sendRedirect(redirectUrl);
    }
}
