package Mangement.StudentManagement.Mapper;
import Mangement.StudentManagement.Entity.*;
import Mangement.StudentManagement.DTO.Response.*;
import Mangement.StudentManagement.DTO.Request.DepartmentRequestDTO;
public class DepartmentMapper {

    public static Department mapToDepartment(DepartmentRequestDTO dto){
        Department department=new Department();
        department.setDepartmentName(dto.getDepartmentName());
        department.setDepartmentCode(dto.getDepartmentCode());
        return department;
    }

    public static DepartmentResponseDTO mapToDepartmentResponseDTO(Department department){

        return new DepartmentResponseDTO(department.getDepartmentId(),department.getDepartmentName(),department.getDepartmentCode()
                  );
    }
}
