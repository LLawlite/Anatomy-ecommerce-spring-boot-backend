package com.ecommerce.anatomy.controller;

import com.ecommerce.anatomy.model.Address;
import com.ecommerce.anatomy.model.User;
import com.ecommerce.anatomy.payload.DTO.AddressDTO;
import com.ecommerce.anatomy.service.interfaces.AddressService;
import com.ecommerce.anatomy.utils.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/public/address")
    public ResponseEntity<AddressDTO> addAddress(@Valid @RequestBody AddressDTO addressDTO)
    {
        User user=authUtil.loggedInUser();
        AddressDTO saveAddressDTO= addressService.addAddress(addressDTO,user);

        return new ResponseEntity<>(saveAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/admin/address")
    public ResponseEntity<List<AddressDTO>>getAllAddresses()
    {
        List<AddressDTO> addresses=addressService.getAllAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/public/address/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addressDTO = addressService.getAddressesById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }
}
