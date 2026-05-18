package Mangement.StudentManagement.Mapper;
import Mangement.StudentManagement.DTO.Request.AddressRequestDTO;
import Mangement.StudentManagement.DTO.Response.AddressResponseDTO;
import Mangement.StudentManagement.Entity.*;
public class AddressMapper {
    public static Address mapToAddress(AddressRequestDTO dto){
        Address address=new Address();
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        return address;
    }
    public static AddressResponseDTO mapToAddressResponseDTO(Address address){
        AddressResponseDTO dto=new AddressResponseDTO(
                    address.getAddressId(),
                   address.getCity(),
                address.getState(),
                address.getPincode()

        );
        return dto;

    }
}
