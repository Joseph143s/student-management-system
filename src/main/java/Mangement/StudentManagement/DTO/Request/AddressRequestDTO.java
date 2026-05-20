package Mangement.StudentManagement.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AddressRequestDTO {
    @NotBlank(message="City cannot be empty")
    private String city;

    @NotBlank(message="State cannot be empty")
    private String state;

    @NotBlank (message=" Pincode cannot be empty")

    @Pattern(
            regexp="^[0-9]{6}$",
            message="Pincode must contain exactly 6 digits"
    )

    private String pincode;

}
