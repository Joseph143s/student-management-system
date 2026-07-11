package Mangement.StudentManagement.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import Mangement.StudentManagement.Entity.Student;
import java.util.*;
public interface StudentRepository extends JpaRepository<Student,Integer>{
  List<Student>findByDepartmentDepartmentId(int departmentId);

  List<Student> findByNameContainingIgnoreCase(String name);


  long countByDepartmentDepartmentId(int departmentId);


}
