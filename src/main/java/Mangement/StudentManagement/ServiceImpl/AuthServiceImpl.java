

package Mangement.StudentManagement.ServiceImpl;


import Mangement.StudentManagement.DTO.Request.LoginRequestDTO;
import Mangement.StudentManagement.DTO.Response.LoginResponseDTO;
import Mangement.StudentManagement.Entity.User;
import Mangement.StudentManagement.Repository.UserRepository;

import Mangement.StudentManagement.Service.AuthService;
import Mangement.StudentManagement.Service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepo,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager) {

        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid Email or Password"));

        String token = jwtService.generateToken(user);

        return new LoginResponseDTO(
                token,
                user.getRole().name(),
                user.getEmail()
        );
    }
}