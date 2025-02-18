package com.ecommerce.anatomy.service.interfaces;

import com.ecommerce.anatomy.model.User;
import com.ecommerce.anatomy.payload.DTO.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO addAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressesById(Long addressId);
}
