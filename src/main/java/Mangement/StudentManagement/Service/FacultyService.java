package Mangement.StudentManagement.Service;

import Mangement.StudentManagement.DTO.Request.FacultyRequestDTO;
import Mangement.StudentManagement.DTO.Response.FacultyResponseDTO;

import java.util.List;

public interface FacultyService {

    FacultyResponseDTO saveFaculty(FacultyRequestDTO dto);

    List<FacultyResponseDTO>getAllFaculties();

    FacultyResponseDTO getFacultyById(int facultyId);

    FacultyResponseDTO updateFaculty(int facultyId,FacultyRequestDTO dto);


    void deleteFaculty(int facultyId);

    long countFaculities();

    boolean existsById(int id);
    List<FacultyResponseDTO> getFacultyByDepartment(int departmentId);

    List<FacultyResponseDTO> searchFacultyByName(String name);

    List<FacultyResponseDTO> sortFacultyByName();

    List<FacultyResponseDTO> sortFacultyBySpecialization();

    List<String> getFacultySpecializations(int departmentId);
}
