package Mangement.StudentManagement.Controller;
import Mangement.StudentManagement.Controller.AddressController;

import Mangement.StudentManagement.Service.AddressService;

import Mangement.StudentManagement.DTO.Request.*;
import Mangement.StudentManagement.DTO.Response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.*;
@RestController
@RequestMapping("/Address")
@Tag(
         name="Address APIs",
        description="Operation related to Address Management"
)
public class AddressController {

    private final AddressService addressservice;

    public AddressController(AddressService addressservice) {

        this.addressservice = addressservice;
    }
  @Operation(summary="Save Address")
    @PostMapping
    public ResponseEntity<AddressResponseDTO> saveAddress(@Valid @RequestBody AddressRequestDTO dto){
        AddressResponseDTO response=addressservice.saveAddress(dto);

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @Operation(summary="Get All Addresses")
    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>>getAllAddresses()
    {
        List<AddressResponseDTO>list=addressservice.getAllAddresses();
        return ResponseEntity.ok(list);
    }

    @Operation(summary="Get Address By Id")
    @GetMapping("/{id}")
     public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable int id){
        AddressResponseDTO response=addressservice.getAddressById(id);
           return ResponseEntity.ok(response);
    }

   @Operation(summary="Update Address")
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable int id,
            @Valid @RequestBody AddressRequestDTO dto){

        AddressResponseDTO updated= addressservice.updateAddress(id,dto);

        return ResponseEntity.ok(updated);
    }

    @Operation(summary="Deleting address")
    @DeleteMapping("/{id}")
    public ResponseEntity<String>deleteAddress(@PathVariable int id){
        addressservice.deleteAddressById(id);
        return  ResponseEntity.ok(" Address deleted Successfully");
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable int id) {
        return ResponseEntity.ok(addressservice.existsById(id));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAddresses() {
        return ResponseEntity.ok(addressservice.countAddresses());
    }


}
