package Mangement.StudentManagement.Service;
import Mangement.StudentManagement.DTO.Request.StudentRequestDTO;
import Mangement.StudentManagement.DTO.Response.StudentResponseDTO;
import Mangement.StudentManagement.DTO.Response.AddressResponseDTO;
import Mangement.StudentManagement.Entity.*;

import java.util.*;
public interface StudentService {
    StudentResponseDTO saveStudent(StudentRequestDTO dto);

    List<StudentResponseDTO> getAllStudents();

    StudentResponseDTO getStudentById(int id);

    void deleteStudentById(int id);

    StudentResponseDTO updateStudent(int id, StudentRequestDTO student);

    boolean existsById(int id);

    long countStudents();

  //  AddressResponseDTO getStudentAddressById(int studentId);

    List<StudentResponseDTO> getStudentsByDepartment(int departmentId);

    List<StudentResponseDTO> getStudentsByCourse(int courseId);

    List<StudentResponseDTO> searchStudentsByName(String name);

    List<StudentResponseDTO> sortStudentsByName();

}
