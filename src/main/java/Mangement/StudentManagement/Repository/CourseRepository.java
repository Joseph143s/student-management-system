package Mangement.StudentManagement.Repository;
import Mangement.StudentManagement.Entity.Course;
import Mangement.StudentManagement.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CourseRepository
        extends JpaRepository<Course,Integer> {

    List<Course>findByDepartmentDepartmentId(int departmentId);

}
