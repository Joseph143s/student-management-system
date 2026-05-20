package Mangement.StudentManagement.DTO.Request;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CourseRequestDTO {
    @NotBlank(message="Course name cannot be empty")
    private String coursename;

    @NotBlank(message="Duration cannot be empty")
    private String duration;

    @Positive(message = "Fee must be greater than 0")
    private double fee;
}
