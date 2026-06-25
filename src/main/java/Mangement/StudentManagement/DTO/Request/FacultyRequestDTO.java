package Mangement.StudentManagement.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor

public class FacultyRequestDTO {
    @NotBlank(message="facultyName cannot be empty")
    private String facultyName;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message="specialization cannot be empty")
    private String specialization;

    @Positive(message=" department id must be greater than 0")
    private int departmentId;
}
