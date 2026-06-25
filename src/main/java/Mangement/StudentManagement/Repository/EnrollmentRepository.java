package Mangement.StudentManagement.Repository;

import Mangement.StudentManagement.Entity.Course;
import Mangement.StudentManagement.Entity.Enrollment;
import Mangement.StudentManagement.Entity.Student;
import Mangement.StudentManagement.Enum.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    List<Enrollment> findByStudent(Student student);

    List<Enrollment> findByCourse(Course course);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);


    List<Enrollment> findByCourse_CourseId(int courseId);

    // Bonus — useful for similar student-side query
    List<Enrollment> findByStudent_Id(int studentId);

}