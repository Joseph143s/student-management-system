package Mangement.StudentManagement.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import Mangement.StudentManagement.Entity.Student;

public interface StudentRepository extends JpaRepository<Student,Integer>{
}
