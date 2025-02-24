package com.ecommerce.anatomy.service.interfaces;

import com.ecommerce.anatomy.model.User;
import com.ecommerce.anatomy.payload.DTO.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO addAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressesById(Long addressId);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO);

    String deleteAddressById(Long addressId);
}
