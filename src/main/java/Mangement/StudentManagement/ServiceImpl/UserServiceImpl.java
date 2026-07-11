package Mangement.StudentManagement.ServiceImpl;

import Mangement.StudentManagement.DTO.Request.UserRequestDTO;
import Mangement.StudentManagement.DTO.Response.UserResponseDTO;
import Mangement.StudentManagement.Entity.User;
import Mangement.StudentManagement.Enum.Role;
import Mangement.StudentManagement.Exception.UserAlreadyExistsException;
import Mangement.StudentManagement.Mapper.UserMapper;
import Mangement.StudentManagement.Repository.UserRepository;
import Mangement.StudentManagement.Service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userrepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userrepo,
                           PasswordEncoder passwordEncoder) {
        this.userrepo = userrepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ─── Register ─────────────────────────────────────────────────

    @Override
    public UserResponseDTO registerStudent(UserRequestDTO dto) {
        return registerWithRole(dto, Role.STUDENT);
    }

    @Override
    public UserResponseDTO registerFaculty(UserRequestDTO dto) {
        return registerWithRole(dto, Role.FACULTY);
    }

    @Override
    public UserResponseDTO registerAdmin(UserRequestDTO dto) {
        return registerWithRole(dto, Role.ADMIN);
    }

    // ─── Private Helper ───────────────────────────────────────────

    private UserResponseDTO registerWithRole(UserRequestDTO dto, Role role) {

        // ✅ Check for duplicate email
        if (userrepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(
                    "Email already exists: " + dto.getEmail());
        }

        User user = UserMapper.mapToUser(dto, role);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userrepo.save(user);
        return UserMapper.mapToUserResponseDTO(saved);
    }
}