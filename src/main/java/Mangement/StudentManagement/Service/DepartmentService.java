package Mangement.StudentManagement.Service;
import Mangement.StudentManagement.DTO.Request.*;
import Mangement.StudentManagement.DTO.Response.*;
import java.util.*;
public interface DepartmentService {

    DepartmentResponseDTO saveDepartment(DepartmentRequestDTO dto);

    DepartmentResponseDTO getDepartmentById(int id);

    List<DepartmentResponseDTO> getAllDepartments();

    DepartmentResponseDTO updateDepartment(int id , DepartmentRequestDTO dto);

    void deleteDepartmentById(int id);

    boolean existsById(int id);

    long countDepartments();

    List<StudentResponseDTO> getStudentsByDepartment(int departmentId);

    List<FacultyResponseDTO> getFacultyByDepartment(int departmentId);

    long countFacultyByDepartment(int departmentId);

    long countStudentsByDepartment(int departmentId);


}
