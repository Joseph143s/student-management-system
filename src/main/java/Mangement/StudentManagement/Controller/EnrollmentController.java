package Mangement.StudentManagement.Controller;

import Mangement.StudentManagement.DTO.Request.EnrollmentRequestDTO;
import Mangement.StudentManagement.DTO.Response.ApiResponse;
import Mangement.StudentManagement.DTO.Response.EnrollmentResponseDTO;
import Mangement.StudentManagement.Enum.EnrollmentStatus;
import Mangement.StudentManagement.Service.EnrollmentService;
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
@RequestMapping("/enrollments")
@Tag(
        name = "Enrollment APIs",
        description = "Operations related to Enrollment Management"
)
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // ─── Enroll ───────────────────────────────────────────────────
    @PreAuthorize("hasRole('STUDENT')")

    @Operation(summary = "Enroll a Student in a Course")
    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> enroll(
            @Valid @RequestBody EnrollmentRequestDTO dto) {

        EnrollmentResponseDTO response = enrollmentService.enroll(dto);

        return new ResponseEntity<>(
                ApiResponseBuilder.success(
                        201,
                        "Student enrolled successfully",
                        response),
                HttpStatus.CREATED);
    }

    // ─── Get All ──────────────────────────────────────────────────
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all Enrollments")
    @GetMapping
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getAllEnrollments() {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Enrollments fetched successfully",
                        enrollmentService.getAllEnrollments()
                ));
    }

    // ─── Get By Id ────────────────────────────────────────────────
    @PreAuthorize("hasAnyRole('STUDENT','FACULTY','ADMIN')")
    @Operation(summary = "Get Enrollment by Id")
    @GetMapping("/{enrollmentId}")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> getEnrollmentById(
            @PathVariable int enrollmentId) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Enrollment fetched successfully",
                        enrollmentService.getEnrollmentById(enrollmentId)
                ));
    }

    // ─── Delete ───────────────────────────────────────────────────
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Enrollment by Id")
    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<ApiResponse<String>> deleteEnrollment(
            @PathVariable int enrollmentId) {

        enrollmentService.deleteEnrollment(enrollmentId);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Enrollment deleted successfully",
                        null
                ));
    }

    // ─── Status Management ────────────────────────────────────────
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Drop a Course")
    @PatchMapping("/{enrollmentId}/drop")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> dropCourse(
            @PathVariable int enrollmentId) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Course dropped successfully",
                        enrollmentService.dropCourse(enrollmentId)
                ));
    }

    @PreAuthorize("hasAnyRole('FACULTY','ADMIN')")
    @Operation(summary = "Complete a Course")
    @PatchMapping("/{enrollmentId}/complete")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> completeCourse(
            @PathVariable int enrollmentId,
            @RequestParam String grade) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Course completed successfully",
                        enrollmentService.completeCourse(enrollmentId, grade)
                ));
    }

    @PreAuthorize("hasAnyRole('FACULTY','ADMIN')")
    @Operation(summary = "Update Enrollment Status")
    @PatchMapping("/{enrollmentId}/status")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> updateStatus(
            @PathVariable int enrollmentId,
            @RequestParam EnrollmentStatus status) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Enrollment status updated successfully",
                        enrollmentService.updateStatus(enrollmentId, status)
                ));
    }

    // ─── Queries ──────────────────────────────────────────────────
    @PreAuthorize("hasAnyRole('STUDENT','FACULTY','ADMIN')")
    @Operation(summary = "Get Enrollments by Student Id")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByStudent(
            @PathVariable int studentId) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Enrollments fetched successfully",
                        enrollmentService.getEnrollmentsByStudent(studentId)
                ));
    }

    @PreAuthorize("hasAnyRole('FACULTY','ADMIN')")
    @Operation(summary = "Get Enrollments by Course Id")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByCourse(
            @PathVariable int courseId) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Enrollments fetched successfully",
                        enrollmentService.getEnrollmentsByCourse(courseId)
                ));
    }

    @PreAuthorize("hasAnyRole('FACULTY','ADMIN')")
    @Operation(summary = "Get Enrollments by Status")
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByStatus(
            @RequestParam EnrollmentStatus status) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Enrollments fetched successfully",
                        enrollmentService.getEnrollmentsByStatus(status)
                ));
    }

    // ─── Count ────────────────────────────────────────────────────
    @PreAuthorize("hasAnyRole('FACULTY','ADMIN')")
    @Operation(summary = "Count Students enrolled in a Course")
    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<ApiResponse<Long>> countEnrollmentsByCourse(
            @PathVariable int courseId) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Enrollment count fetched successfully",
                        enrollmentService.countEnrollmentsByCourse(courseId)
                ));
    }
}