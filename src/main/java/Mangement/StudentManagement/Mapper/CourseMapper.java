package Mangement.StudentManagement.Mapper;

import Mangement.StudentManagement.DTO.Request.CourseRequestDTO;
import Mangement.StudentManagement.DTO.Response.CourseResponseDTO;
import Mangement.StudentManagement.Entity.*;
public class CourseMapper {

    public static Course mapToCourse(CourseRequestDTO dto){
         Course course=new Course();
         course.setCoursename(dto.getCoursename());
         course.setDuration(dto.getDuration());
         course.setFee(dto.getFee());
         return course;
    }

    public static CourseResponseDTO mapToCourseResponseDTO(Course course){
        CourseResponseDTO dto=new CourseResponseDTO(
                     course.getCourseId(),
                course.getCoursename(),
                course.getDuration(),
                course.getFee()

        );
        return dto;
    }
}
