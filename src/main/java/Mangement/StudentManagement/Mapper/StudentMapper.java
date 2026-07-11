package Mangement.StudentManagement.Mapper;

import Mangement.StudentManagement.DTO.Request.StudentRequestDTO;
import Mangement.StudentManagement.DTO.Response.StudentResponseDTO;

import Mangement.StudentManagement.Entity.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StudentMapper {

    public static Student mapToStudent(
            StudentRequestDTO dto,

            Department department,
            User user){

        Student student = new Student();

        student.setName(dto.getName());

        student.setEmail(dto.getEmail());


       // student.setAddress(address);

        student.setDepartment(department);

        student.setUser(user);

        return student;
    }

    public static StudentResponseDTO
    mapToStudentResponseDTO(Student student){


        List<String> courseNames=student.getEnrollments()==null
                ? Collections.emptyList()
                :  student.getEnrollments()
                .stream()
                .map(enrollment->enrollment.getCourse().getCoursename())
                .collect(Collectors.toList());
        String city = student.getAddress() != null
                ? student.getAddress().getCity()
                : null;
        return new StudentResponseDTO(

                student.getId(),

                student.getName(),

                student.getEmail(),

              courseNames,

                city,
                student.getDepartment() != null
                        ? student.getDepartment().getDepartmentName()
                        : null
        );
    }
}