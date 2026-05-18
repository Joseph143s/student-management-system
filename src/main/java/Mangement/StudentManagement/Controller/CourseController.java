package Mangement.StudentManagement.Controller;
import Mangement.StudentManagement.Controller.CourseController;
import Mangement.StudentManagement.Service.CourseService;
import Mangement.StudentManagement.ServiceImpl.CourseServiceImpl;
import lombok.Getter;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import Mangement.StudentManagement.Entity.*;
import Mangement.StudentManagement.DTO.Request.*;
import Mangement.StudentManagement.DTO.Response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.*;
@RestController
@RequestMapping("/courses")
@Tag(
        name="Course APIs",
        description = "Operations related to Course Management"
)
public class CourseController {

    private final CourseService courseservice;
    public CourseController(CourseService courseservice){
        this.courseservice=courseservice;
    }
    @Operation(summary= " save Course")
    @PostMapping
    public ResponseEntity<CourseResponseDTO> saveCourse( @Valid @RequestBody CourseRequestDTO dto){
        CourseResponseDTO response= courseservice.saveCourse(dto);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
  @Operation(summary=" get all Courses")
    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses(){

        return ResponseEntity.ok(courseservice.getAllCourses());
    }
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable int id){

        return ResponseEntity.ok(courseservice.getCourseById(id));
    }
    @Operation(summary="Delete Address")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id){
        courseservice.deleteCourseById(id);
        return ResponseEntity.ok("Course Deleted Successfully");
    }
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean>existsById(@PathVariable int id){
        return ResponseEntity.ok(courseservice.existsById(id));
    }
   @GetMapping("/count")
    public ResponseEntity<Long> countCourses(){
        return ResponseEntity.ok(courseservice.countCourses());
   }
}
