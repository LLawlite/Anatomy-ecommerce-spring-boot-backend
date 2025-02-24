package com.ecommerce.anatomy.service.implementation;

import com.ecommerce.anatomy.exceptions.ResourceNotFoundException;
import com.ecommerce.anatomy.model.Address;
import com.ecommerce.anatomy.model.User;
import com.ecommerce.anatomy.payload.DTO.AddressDTO;
import com.ecommerce.anatomy.repositories.AddressRepository;
import com.ecommerce.anatomy.repositories.UserRepository;
import com.ecommerce.anatomy.service.interfaces.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;


    @Override
    public AddressDTO addAddress(AddressDTO addressDTO, User user) {

        Address address= modelMapper.map(addressDTO,Address.class);
        address.setUser(user);

        List<Address>addressList=user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        Address savedAddress=addressRepository.save(address);
        AddressDTO savedAddressDTO=modelMapper.map(savedAddress,AddressDTO.class);
        return savedAddressDTO;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
          return  addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();

    }

    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address>addresses=user.getAddresses();
        return addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO) {
        Address addressFromDatabase = addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressFromDatabase.setCity(addressDTO.getCity());
        addressFromDatabase.setPincode(addressDTO.getPincode());
        addressFromDatabase.setState(addressDTO.getState());
        addressFromDatabase.setCountry(addressDTO.getCountry());
        addressFromDatabase.setStreet(addressDTO.getStreet());
        addressFromDatabase.setBuildingName(addressDTO.getBuildingName());

        Address updatedAddress = addressRepository.save(addressFromDatabase);

        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);



    }

    @Override
    public String deleteAddressById(Long addressId) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(addressFromDatabase);

        return "Address deleted successfully with addressId: " + addressId;
    }
}
