package Mangement.StudentManagement.Mapper;

import Mangement.StudentManagement.DTO.Response.EnrollmentResponseDTO;
import Mangement.StudentManagement.Entity.Enrollment;
import Mangement.StudentManagement.Enum.EnrollmentStatus;

import java.time.LocalDate;

public class EnrollmentMapper {

    public static Enrollment mapToEnrollment(int studentId,int courseId){

        Enrollment enrollment=new Enrollment();
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        return enrollment;
    }

    public static EnrollmentResponseDTO mapToEnrollmentResponseDTO(Enrollment enrollment){

        return new EnrollmentResponseDTO(
                enrollment.getEnrollmentId(),
                enrollment.getStudent().getId(),
                enrollment.getStudent().getName(),
                enrollment.getCourse().getCourseId(),
                enrollment.getCourse().getCoursename(),
                enrollment.getEnrollmentDate(),
                enrollment.getGrade(),
                enrollment.getStatus()
        );
    }
}
