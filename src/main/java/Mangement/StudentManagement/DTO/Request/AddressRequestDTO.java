package Mangement.StudentManagement.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AddressRequestDTO {
    private String City;
    private String state;
    private String pincode;

}
