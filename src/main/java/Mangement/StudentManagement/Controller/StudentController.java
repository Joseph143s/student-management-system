package Mangement.StudentManagement.Controller;

import Mangement.StudentManagement.DTO.Request.StudentRequestDTO;
import Mangement.StudentManagement.DTO.Response.AddressResponseDTO;
import Mangement.StudentManagement.DTO.Response.ApiResponse;
import Mangement.StudentManagement.DTO.Response.StudentResponseDTO;
import Mangement.StudentManagement.Service.StudentService;
import Mangement.StudentManagement.Utility.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@Tag(
        name = "Student APIs",
        description = "Operations related to Student Management"
)
public class StudentController {

    private final StudentService studentservice;

    public StudentController(StudentService studentservice) {
        this.studentservice = studentservice;
    }

    // ─── Save ─────────────────────────────────────────────────────
  @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Save Student")
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> saveStudent(
            @Valid @RequestBody StudentRequestDTO dto) {

        StudentResponseDTO response = studentservice.saveStudent(dto);

        return new ResponseEntity<>(
                ApiResponseBuilder.success(
                        201,
                        "Student created successfully",
                        response),
                HttpStatus.CREATED);
    }

    // ─── Get All ──────────────────────────────────────────────────
  @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @Operation(summary = "Get all Students")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents() {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Students fetched successfully",
                        studentservice.getAllStudents()
                ));
    }

    // ─── Get By Id ────────────────────────────────────────────────
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    @Operation(summary = "Get Student by Id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(
            @PathVariable int id) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Student fetched successfully",
                        studentservice.getStudentById(id)
                ));
    }

   /*
    //  Get Address

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @Operation(summary = "Get Student Address by Student Id")
    @GetMapping("/{id}/address")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> getStudentAddressById(
            @PathVariable int id) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Student address fetched successfully",
                        studentservice.getStudentAddressById(id)
                ));
    }

    */

    //     Update
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update Student")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateStudent(
            @PathVariable int id,
            @Valid @RequestBody StudentRequestDTO dto) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Student updated successfully",
                        studentservice.updateStudent(id, dto)
                ));
    }

    // ─── Delete ───────────────────────────────────────────────────
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Student")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteStudent(
            @PathVariable int id) {

        studentservice.deleteStudentById(id);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Student deleted successfully",
                        null
                ));
    }

    // ─── Exists / Count ───────────────────────────────────────────
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Check if Student exists")
    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse<Boolean>> existsById(
            @PathVariable int id) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Existence checked successfully",
                        studentservice.existsById(id)
                ));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Count all Students")
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countStudents() {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Student count fetched successfully",
                        studentservice.countStudents()
                ));
    }

    // ─── By Department ────────────────────────────────────────────
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get Students by Department Id")
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getStudentsByDepartment(
            @PathVariable int departmentId) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Students fetched successfully",
                        studentservice.getStudentsByDepartment(departmentId)
                ));
    }

    // ─── By Course ────────────────────────────────────────────────
    @PreAuthorize("hasAnyRole('ADMIN','Faculty')")
    @Operation(summary = "Get Students by Course Id")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getStudentsByCourse(
            @PathVariable int courseId) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Students fetched successfully",
                        studentservice.getStudentsByCourse(courseId)
                ));
    }

    // ─── Search & Sort ────────────────────────────────────────────
    @PreAuthorize("hasAnyRole('ADMIN','Faculty')")
    @Operation(summary = "Search Students by Name")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> searchStudentsByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Students fetched successfully",
                        studentservice.searchStudentsByName(name)
                ));
    }
    @PreAuthorize("hasAnyRole('ADMIN','Faculty')")
    @Operation(summary = "Sort Students by Name")
    @GetMapping("/sort")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> sortStudentsByName() {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Students sorted successfully",
                        studentservice.sortStudentsByName()
                ));
    }
}