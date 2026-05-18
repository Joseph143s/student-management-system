package Mangement.StudentManagement.ServiceImpl;
import Mangement.StudentManagement.Entity.Course;
import Mangement.StudentManagement.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Mangement.StudentManagement.Mapper.CourseMapper;
import Mangement.StudentManagement.Repository.CourseRepository;
import Mangement.StudentManagement.DTO.Request.CourseRequestDTO;
import Mangement.StudentManagement.DTO.Response.CourseResponseDTO;
import java.util.*;
import java.util.stream.Collectors;
import Mangement.StudentManagement.Exception.CourseNotFoundException;
@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courserepo;

    public CourseServiceImpl(CourseRepository courserepo) {
        this.courserepo = courserepo;
    }
    @Override
    public CourseResponseDTO saveCourse(CourseRequestDTO dto){
        Course course =CourseMapper.mapToCourse(dto);
        Course saveCourse = courserepo.save(course);
        return CourseMapper.mapToCourseResponseDTO(saveCourse);
    }
    @Override
    public List<CourseResponseDTO>getAllCourses(){
         List<Course>courses=courserepo.findAll();
          return courses.stream().map(CourseMapper::mapToCourseResponseDTO).collect(Collectors.toList());
    }
    @Override
    public CourseResponseDTO getCourseById(int id){
        Course course=courserepo.findById(id).orElseThrow(()-> new CourseNotFoundException("Course not found"));
      return CourseMapper.mapToCourseResponseDTO(course);
    }
    @Override
    public void deleteCourseById(int id) {
        Course course = courserepo.findById(id).orElseThrow(() -> new CourseNotFoundException("Course not found"));
        courserepo.delete(course);
    }

    @Override
    public CourseResponseDTO updateCourse(int id,CourseRequestDTO dto){
        Course existingcourse= courserepo.findById(id).orElseThrow(()->new CourseNotFoundException("Course not found"));

        existingcourse.setCoursename(dto.getCoursename());
        existingcourse.setDuration(dto.getDuration());
        existingcourse.setFee(dto.getFee());

        Course updatedCourse= courserepo.save(existingcourse);
        return CourseMapper.mapToCourseResponseDTO(updatedCourse);

    }

    @Override
    public boolean existsById(int id)
    {
        return courserepo.existsById(id);
    }
@Override
    public long countCourses(){

        return courserepo.count();
}
}
