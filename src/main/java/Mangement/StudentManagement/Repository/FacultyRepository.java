package Mangement.StudentManagement.Repository;

import Mangement.StudentManagement.Entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface FacultyRepository extends JpaRepository<Faculty,Integer> {
    List<Faculty> findByDepartmentDepartmentId(
            int departmentId);

    List<Faculty>
    findByFacultyNameContainingIgnoreCase(
            String name);

    long countByDepartmentDepartmentId(int departmentId);
}
