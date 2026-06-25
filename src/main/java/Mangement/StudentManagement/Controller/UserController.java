package Mangement.StudentManagement.Controller;

import Mangement.StudentManagement.DTO.Request.LoginRequestDTO;
import Mangement.StudentManagement.DTO.Request.UserRequestDTO;
import Mangement.StudentManagement.DTO.Response.ApiResponse;
import Mangement.StudentManagement.DTO.Response.LoginResponseDTO;
import Mangement.StudentManagement.DTO.Response.UserResponseDTO;
import Mangement.StudentManagement.Service.UserService;
import Mangement.StudentManagement.Utility.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication APIs",
        description = "Operations related to User Registration and Login"
)
public class UserController {

    private final UserService userservice;

    public UserController(UserService userservice) {
        this.userservice = userservice;
    }

    // ─── Register ──────────────────────────────────────────

    @Operation(summary = "Register Student")
    @PostMapping("/register/student")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerStudent(
            @Valid @RequestBody UserRequestDTO dto) {

        return new ResponseEntity<>(
                ApiResponseBuilder.success(
                        201,
                        "Student registered successfully",
                        userservice.registerStudent(dto)
                ), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register Faculty")
    @PostMapping("/register/faculty")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerFaculty(
            @Valid @RequestBody UserRequestDTO dto) {

        return new ResponseEntity<>(
                ApiResponseBuilder.success(
                        201,
                        "Faculty registered successfully",
                        userservice.registerFaculty(dto)
                ), HttpStatus.CREATED);
    }


    @Operation(summary = "Register Admin")
    @PostMapping("/register/admin")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerAdmin(
            @Valid @RequestBody UserRequestDTO dto) {

        return new ResponseEntity<>(
                ApiResponseBuilder.success(
                        201,
                        "Admin registered successfully",
                        userservice.registerAdmin(dto)
                ), HttpStatus.CREATED);
    }



}