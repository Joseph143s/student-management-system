package Mangement.StudentManagement.DTO.Response;

import Mangement.StudentManagement.Enum.EnrollmentStatus;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponseDTO {

    private int enrollmentId;

    private int studentId;

    private String studentName;

    private int courseId;

    private String courseName;

    private LocalDate enrolmentDate;

    private String grade;

    private EnrollmentStatus status;
}
