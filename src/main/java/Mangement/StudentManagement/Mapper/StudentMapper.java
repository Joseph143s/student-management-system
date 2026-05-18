package Mangement.StudentManagement.Mapper;
import Mangement.StudentManagement.DTO.Request.StudentRequestDTO;
import Mangement.StudentManagement.DTO.Response.StudentResponseDTO;
import Mangement.StudentManagement.Entity.*;
import lombok.Getter;
import lombok.Setter;


public class StudentMapper {
    public static Student mapToStudent(StudentRequestDTO dto,Course course,Address address){
        Student student=new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setCourse(course);
        student.setAddress(address);
        return student;

    }
    public static StudentResponseDTO mapToStudentResponseDTO(Student student){
        StudentResponseDTO dto=
                new StudentResponseDTO(
                      student.getId(),
                        student.getName(),
                        student.getEmail(),
                        student.getCourse().getCoursename(),
                        student.getAddress().getCity()
                );
        return dto;

    }
}
