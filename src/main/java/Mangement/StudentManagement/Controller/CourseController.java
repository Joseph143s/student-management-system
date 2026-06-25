package Mangement.StudentManagement.Controller;

import Mangement.StudentManagement.DTO.Request.CourseRequestDTO;
import Mangement.StudentManagement.DTO.Response.ApiResponse;
import Mangement.StudentManagement.DTO.Response.CourseResponseDTO;
import Mangement.StudentManagement.DTO.Response.StudentResponseDTO;
import Mangement.StudentManagement.Service.CourseService;
import Mangement.StudentManagement.Utility.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@Tag(
        name = "Course APIs",
        description = "Operations related to Course Management"
)
public class CourseController {

    private final CourseService courseservice;

    // ✅ StudentService removed — not needed here
    public CourseController(CourseService courseservice) {
        this.courseservice = courseservice;
    }

    // ─── @Save ─────────────────────────────────────────────────────

     @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @Operation(summary = "Save Course")
    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponseDTO>> saveCourse(
            @Valid @RequestBody CourseRequestDTO dto) {

        CourseResponseDTO response = courseservice.saveCourse(dto);

        return new ResponseEntity<>(
                ApiResponseBuilder.success(
                        201,
                        "Course created successfully",
                        response),
                HttpStatus.CREATED);
    }

    // ─── Get All ──────────────────────────────────────────────────
@PreAuthorize("hasAnyRole('STUDENT','FACULTY','ADMIN')")
    @Operation(summary = "Get all Courses")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getAllCourses() {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Courses fetched successfully",
                        courseservice.getAllCourses()
                ));
    }

    // ─── Get By Id ────────────────────────────────────────────────


    @PreAuthorize("hasAnyRole('STUDENT','FACULTY','ADMIN')")
    @Operation(summary = "Get Course by Id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> getCourseById(
            @PathVariable int id) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Course fetched successfully",
                        courseservice.getCourseById(id)
                ));
    }

    // ─── Update ───────────────────────────────────────────────────


    @PreAuthorize("hasAnyRole('FACULTY','ADMIN')")
    @Operation(summary = "Update Course")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> updateCourse(
            @PathVariable int id,
            @Valid @RequestBody CourseRequestDTO dto) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Course updated successfully",
                        courseservice.updateCourse(id, dto)
                ));
    }

    // ─── Delete ───────────────────────────────────────────────────
 @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Course") // ✅ was "Delete Address"
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCourse(
            @PathVariable int id) {

        courseservice.deleteCourseById(id);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Course deleted successfully",
                        null
                ));
    }

    // ─── Exists / Count ───────────────────────────────────────────
 @PreAuthorize("hasAnyRole('STUDENT','FACULTY','ADMIN')")
    @Operation(summary = "Check if Course exists")
    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse<Boolean>> existsById(
            @PathVariable int id) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Existence checked successfully",
                        courseservice.existsById(id)
                ));
    }


    @PreAuthorize("hasAnyRole('STUDENT','FACULTY','ADMIN')")
    @Operation(summary = "Count all Courses")
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countCourses() {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Course count fetched successfully",
                        courseservice.countCourses()
                ));
    }

    // ─── By Department ────────────────────────────────────────────

    @PreAuthorize("hasAnyRole('STUDENT','FACULTY','ADMIN')")
    @Operation(summary = "Get Courses by Department Id")
    @GetMapping("/department/{departmentId}") // ✅ was /course/departments/{Id}
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getCoursesByDepartment(
            @PathVariable int departmentId) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Courses fetched successfully",
                        courseservice.getCoursesByDepartment(departmentId)
                ));
    }

    // ─── Students In Course ───────────────────────────────────────

    @PreAuthorize("hasAnyRole('FACULTY','ADMIN')")
    @Operation(summary = "Get Students enrolled in a Course")
    @GetMapping("/{courseId}/students") // ✅ was /course/{courseId}
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getStudentsInCourse(
            @PathVariable int courseId) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Students fetched successfully",
                        courseservice.getStudentsInCourse(courseId)
                ));
    }
}