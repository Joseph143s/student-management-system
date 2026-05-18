package Mangement.StudentManagement.Service;

import Mangement.StudentManagement.DTO.Response.*;
import Mangement.StudentManagement.DTO.Request.*;

import java.util.*;
public interface AddressService {
    AddressResponseDTO saveAddress(AddressRequestDTO dto);
    List<AddressResponseDTO> getAllAddresses();
    AddressResponseDTO getAddressById(int id);
    AddressResponseDTO updateAddress(int id,AddressRequestDTO dto);
    void deleteAddressById(int id);
    boolean existsById(int id);
    long countAddresses();
}
