package Mangement.StudentManagement.ServiceImpl;
import Mangement.StudentManagement.Repository.*;
import Mangement.StudentManagement.Service.*;
import Mangement.StudentManagement.Exception.*;
import Mangement.StudentManagement.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Mangement.StudentManagement.DTO.Request.*;
import Mangement.StudentManagement.DTO.Response.*;
import Mangement.StudentManagement.Mapper.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class AddressServiceImpl implements AddressService {


    private final AddressRepository addressrepo;

    public AddressServiceImpl(AddressRepository repository) {
        this.addressrepo = repository;
    }

    @Override
    public AddressResponseDTO saveAddress(AddressRequestDTO dto){

        Address address=AddressMapper.mapToAddress(dto);

        Address saveaddress=addressrepo.save(address);

        return AddressMapper.mapToAddressResponseDTO(saveaddress);
    }
  @Override
    public List<AddressResponseDTO> getAllAddresses(){

        List<Address>address=addressrepo.findAll();

        return address.stream().
                map(AddressMapper::mapToAddressResponseDTO)
                .collect(Collectors.toList());
  }
  @Override
    public AddressResponseDTO getAddressById(int id){
        Address address= addressrepo.findById(id)
                .orElseThrow(()->new RuntimeException("Address not found with thwt id:"+id));
       return AddressMapper.mapToAddressResponseDTO(address);
    }
   @Override
    public AddressResponseDTO updateAddress(int id,AddressRequestDTO dto){
        Address existingaddress=addressrepo.findById(id).orElseThrow(()->new AddressNotFoundException("Address not found with that id"));
        existingaddress.setCity(dto.getCity());
        existingaddress.setState(dto.getState());
        existingaddress.setPincode(dto.getPincode());
        Address updatedaddress= addressrepo.save(existingaddress);
        return AddressMapper.mapToAddressResponseDTO(updatedaddress);
  }
  @Override
    public void deleteAddressById(int id){

        Address address=addressrepo.findById(id).orElseThrow(()->new AddressNotFoundException("Address not found with that id :"+ id));
         addressrepo.delete(address);
  }
  @Override
  public boolean existsById(int id){
        return addressrepo.existsById(id);
  }
   @Override
    public long countAddresses(){
        return addressrepo.count();
   }
}
