package Mangement.StudentManagement.Mapper;
import Mangement.StudentManagement.Entity.*;
import Mangement.StudentManagement.DTO.Request.DepartmentRequestDTO;
import Mangement.StudentManagement.DTO.Request.FacultyRequestDTO;
import Mangement.StudentManagement.Entity.Faculty;
import Mangement.StudentManagement.DTO.Response.*;

public class FacultyMapper {


    public static Faculty mapToFaculty(
            FacultyRequestDTO dto, Department department
    ){
        Faculty faculty=new Faculty();
        faculty.setFacultyName(dto.getFacultyName());
        faculty.setEmail(dto.getEmail());
        faculty.setSpecialization(dto.getSpecialization());
        faculty.setDepartment(department);

        return faculty;
    }

    public static FacultyResponseDTO mapToFacultyResponseDTO(Faculty faculty){

        return new FacultyResponseDTO(
                faculty.getFacultyId(),
                faculty.getFacultyName(),
                faculty.getEmail(),
                faculty.getSpecialization(),
                faculty.getDepartment().getDepartmentCode()
        );
    }
}
