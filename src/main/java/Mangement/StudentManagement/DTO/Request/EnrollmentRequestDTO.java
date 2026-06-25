package Mangement.StudentManagement.DTO.Request;

import Mangement.StudentManagement.Enum.EnrollmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter

@Setter

@AllArgsConstructor

@NoArgsConstructor
public class EnrollmentRequestDTO {

    @Positive(message = "Student Id must be greater than 0")
    private int studentId;

    @Positive(message = "Course Id must be greater than 0")
    private int courseId;

    private EnrollmentStatus status;
}
