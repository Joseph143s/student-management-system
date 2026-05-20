package Mangement.StudentManagement.Controller;
import Mangement.StudentManagement.Controller.AddressController;

import Mangement.StudentManagement.Service.AddressService;
import Mangement.StudentManagement.Utility.ApiResponseBuilder;
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
    public ResponseEntity<ApiResponse<AddressResponseDTO>> saveAddress(@Valid @RequestBody AddressRequestDTO dto){
        AddressResponseDTO response=addressservice.saveAddress(dto);

        return new ResponseEntity<>(
                ApiResponseBuilder.success(201,
                        "Address saved Successfully",
                        response),HttpStatus.CREATED);
    }
    @Operation(summary="Get All Addresses")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponseDTO>>>getAllAddresses()
    {
        List<AddressResponseDTO>list=addressservice.getAllAddresses();
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Address fetched Successfully",
                        list)
        );
    }

    @Operation(summary="Get Address By Id")
    @GetMapping("/{id}")
     public ResponseEntity<ApiResponse<AddressResponseDTO>> getAddressById(@PathVariable int id){
        AddressResponseDTO response=addressservice.getAddressById(id);
           return ResponseEntity.ok(
                   ApiResponseBuilder.success(
                           200,
                           "Address fetched successfully",
                           response)
           );
    }

   @Operation(summary="Update Address")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> updateAddress(
            @PathVariable int id,
            @Valid @RequestBody AddressRequestDTO dto){

        AddressResponseDTO updated= addressservice.updateAddress(id,dto);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Address updated successfully",
                        updated)
                );
    }

    @Operation(summary="Deleting address")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>>deleteAddress(@PathVariable int id){
        addressservice.deleteAddressById(id);
        return  ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Address deleted Successfully",
                        null
                ));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse<Boolean>> existsById(@PathVariable int id) {
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Existence checked successfully",
                        addressservice.existsById(id)
                )
                );
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countAddresses() {
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Address count fetch successfully ",
                        addressservice.countAddresses()
                ));
    }


}
