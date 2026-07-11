package Mangement.StudentManagement.Controller;

import Mangement.StudentManagement.DTO.Request.LoginRequestDTO;
import Mangement.StudentManagement.DTO.Response.ApiResponse;
import Mangement.StudentManagement.DTO.Response.LoginResponseDTO;
import Mangement.StudentManagement.Service.AuthService;
import Mangement.StudentManagement.Utility.ApiResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @RequestBody LoginRequestDTO dto) {

        LoginResponseDTO response = authService.login(dto);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(
                        200,
                        "Login successful",
                        response
                )
        );
    }
}