package Mangement.StudentManagement.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentRequestDTO {
    @NotBlank(message= " Name cannot be empty")
    private String name;

    @NotBlank(message="Email cannot be empty")

    @Email(message="Invalid email format")
    private String email;

    @Positive(message="Course Id must be greater than 0")
    private int courseId;

    @Positive(message="Addressid must be greater than 0")
    private int addressId;

}
