package Mangement.StudentManagement.Controller;
import Mangement.StudentManagement.Service.CourseService;
import org.springframework.web.bind.annotation.*;
import Mangement.StudentManagement.Utility.ApiResponseBuilder;
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

    public ResponseEntity<ApiResponse<CourseResponseDTO>> saveCourse(
            @Valid @RequestBody CourseRequestDTO dto){

        CourseResponseDTO response= courseservice.saveCourse(dto);

        return new ResponseEntity<>(
                ApiResponseBuilder.success(
                        201,
                        "Course created successfully",
                        response),HttpStatus.CREATED);
    }

  @Operation(summary=" get all Courses")

    @GetMapping

    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getAllCourses(){

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Course fetched successfully",courseservice.getAllCourses()
                ));
    }
    @Operation(summary="Get courses by Id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> getCourseById(@PathVariable int id){

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Course fetched successfully",
                        courseservice.getCourseById(id)
                ));
    }
    @Operation(summary = "Update Course")

    @PutMapping("/{id}")

    public ResponseEntity<ApiResponse<CourseResponseDTO>>
    updateCourse(

            @PathVariable int id,

            @Valid @RequestBody CourseRequestDTO dto){

        CourseResponseDTO updatedCourse =
                courseservice.updateCourse(id,dto);

        return ResponseEntity.ok(

                ApiResponseBuilder.success(
                        200,
                        "Course updated successfully",
                        updatedCourse
                )
        );
    }
    @Operation(summary="Delete Address")

    @DeleteMapping("/{id}")

    public ResponseEntity<ApiResponse<String>> deleteCourse(@PathVariable int id){
        courseservice.deleteCourseById(id);
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Course Deleted successfully",
                        null
                ));
    }
    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse<Boolean>>existsById(@PathVariable int id){
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Existence checked successfully",
                        courseservice.existsById(id)
                ));
    }
   @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countCourses(){
        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Courses count fetched successfully",
                        courseservice.countCourses()
                ));
   }
}
