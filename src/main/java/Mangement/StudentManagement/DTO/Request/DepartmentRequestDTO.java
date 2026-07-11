package Mangement.StudentManagement.DTO.Request;
import Mangement.StudentManagement.Entity.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DepartmentRequestDTO {

    @NotBlank(message="Department name cannot be empty")
    private String departmentName;

    @NotBlank(message="Department code cannot be empty")
    private String  departmentCode;
}
