package Mangement.StudentManagement.Service;

import Mangement.StudentManagement.DTO.Request.EnrollmentRequestDTO;
import Mangement.StudentManagement.DTO.Response.EnrollmentResponseDTO;
import Mangement.StudentManagement.Enum.EnrollmentStatus;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponseDTO enroll(EnrollmentRequestDTO dto);

    EnrollmentResponseDTO getEnrollmentById(int enrollmentId);

    List<EnrollmentResponseDTO> getAllEnrollments();

    void deleteEnrollment(int enrollmentId);

    EnrollmentResponseDTO dropCourse(int enrollmentId);

    EnrollmentResponseDTO completeCourse(int enrollmentId,String grade);
    EnrollmentResponseDTO updateStatus(int enrollmentId, EnrollmentStatus status);

    List<EnrollmentResponseDTO> getEnrollmentsByStudent(int studentId);

    List<EnrollmentResponseDTO> getEnrollmentsByCourse(int courseId);


    List<EnrollmentResponseDTO> getEnrollmentsByStatus(EnrollmentStatus status);

    void updateEnrollments(int studentid ,List<Integer> newCourseIds);

    long countEnrollmentsByCourse(int courseId);

}
