package Mangement.StudentManagement.Service;

import Mangement.StudentManagement.DTO.Response.*;
import Mangement.StudentManagement.DTO.Request.*;

import java.util.*;
public interface CourseService {
    CourseResponseDTO saveCourse(CourseRequestDTO dto);
    List <CourseResponseDTO>getAllCourses();
    CourseResponseDTO getCourseById(int courseId);
    void deleteCourseById(int courseId);
    CourseResponseDTO updateCourse(int id,CourseRequestDTO course);
    boolean existsById(int id);
    long countCourses();
}
