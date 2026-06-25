package Mangement.StudentManagement.ServiceImpl;

//import Mangement.StudentManagement.Entity.Address;
import Mangement.StudentManagement.Entity.Department;
import Mangement.StudentManagement.Entity.Student;
import Mangement.StudentManagement.Entity.Enrollment;
//import Mangement.StudentManagement.Mapper.AddressMapper;
import Mangement.StudentManagement.Repository.*;
import Mangement.StudentManagement.Service.StudentService;
import Mangement.StudentManagement.Service.EnrollmentService;
import org.springframework.stereotype.Service;
import Mangement.StudentManagement.DTO.Request.*;
import Mangement.StudentManagement.DTO.Response.*;
import Mangement.StudentManagement.Mapper.StudentMapper;
import java.util.stream.Collectors;
import Mangement.StudentManagement.Exception.*;
import java.util.*;
import org.springframework.data.domain.Sort;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentrepo;
    //private final AddressRepository addressrepo;
    private final DepartmentRepository departmentrepo;
    private final EnrollmentRepository enrollmentrepo;
    private final EnrollmentService enrollmentService;

    public StudentServiceImpl(StudentRepository studentrepo,
                              //AddressRepository addressrepo,
                              DepartmentRepository departmentrepo,
                              EnrollmentRepository enrollmentrepo,
                              EnrollmentService enrollmentService) {
        this.studentrepo = studentrepo;
        //this.addressrepo = addressrepo;
        this.departmentrepo = departmentrepo;
        this.enrollmentrepo = enrollmentrepo;
        this.enrollmentService = enrollmentService;
    }

    // ─── Save ─────────────────────────────────────────────────────

    @Override
    public StudentResponseDTO saveStudent(StudentRequestDTO dto) {

        /*Address address = addressrepo.findById(dto.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException(
                        "Address not found"));*/

        Department department = departmentrepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found"));

        //  No courses passed — mapper signature updated
        Student student = StudentMapper.mapToStudent(dto,department, null);
        Student savedStudent = studentrepo.save(student);

        //  Enroll after student is saved
        if (dto.getCourseIds() != null && !dto.getCourseIds().isEmpty()) {
            enrollmentService.updateEnrollments(savedStudent.getId(), dto.getCourseIds());
        }

        return StudentMapper.mapToStudentResponseDTO(savedStudent);
    }

    // ─── Get All ──────────────────────────────────────────────────

    @Override
    public List<StudentResponseDTO> getAllStudents() {

        return studentrepo.findAll()
                .stream()
                .map(StudentMapper::mapToStudentResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Get By Id ────────────────────────────────────────────────

    @Override
    public StudentResponseDTO getStudentById(int id) {

        Student student = studentrepo.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id: " + id));

        return StudentMapper.mapToStudentResponseDTO(student);
    }

    // ─── Update ───────────────────────────────────────────────────

    @Override
    public StudentResponseDTO updateStudent(int id, StudentRequestDTO dto) {

        Student existingStudent = studentrepo.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id: " + id));

        /*Address address = addressrepo.findById(dto.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException(
                        "Address not found"));*/

        Department department = departmentrepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found"));

        existingStudent.setName(dto.getName());
        existingStudent.setEmail(dto.getEmail());

       // existingStudent.setAddress(address);

        existingStudent.setDepartment(department);
        // No setCourses() — handled via enrollments below

        Student updated = studentrepo.save(existingStudent);


        if (dto.getCourseIds() != null && !dto.getCourseIds().isEmpty()) {
            enrollmentService.updateEnrollments(updated.getId(), dto.getCourseIds());
        }

        return StudentMapper.mapToStudentResponseDTO(updated);
    }

    // ─── Delete ───────────────────────────────────────────────────

    @Override
    public void deleteStudentById(int id) {

        if (!studentrepo.existsById(id)) {
            throw new StudentNotFoundException(
                    "Student not found with id: " + id);
        }
        studentrepo.deleteById(id);
    }

    // ─── Exists / Count ───────────────────────────────────────────

    @Override
    public boolean existsById(int id) {
        return studentrepo.existsById(id);
    }

    @Override
    public long countStudents() {
        return studentrepo.count();
    }

    // ─── Address ──────────────────────────────────────────────────

   /*
    @Override
    public AddressResponseDTO getStudentAddressById(int studentId) {

        Student student = studentrepo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id: " + studentId));

        return AddressMapper.mapToAddressResponseDTO(student.getAddress());
    }

    */

    // ─── Department ───────────────────────────────────────────────

    @Override
    public List<StudentResponseDTO> getStudentsByDepartment(int departmentId) {

        departmentrepo.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + departmentId));

        return studentrepo.findByDepartmentDepartmentId(departmentId)
                .stream()
                .map(StudentMapper::mapToStudentResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Course ───────────────────────────────────────────────────

    @Override
    public List<StudentResponseDTO> getStudentsByCourse(int courseId) {

        // ✅ Query through enrollments, not direct ManyToMany
        return enrollmentrepo.findByCourse_CourseId(courseId)
                .stream()
                .map(Enrollment::getStudent)
                .map(StudentMapper::mapToStudentResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Search & Sort ────────────────────────────────────────────

    @Override
    public List<StudentResponseDTO> searchStudentsByName(String name) {

        return studentrepo.findByNameContainingIgnoreCase(name)
                .stream()
                .map(StudentMapper::mapToStudentResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDTO> sortStudentsByName() {

        return studentrepo.findAll(Sort.by("name"))
                .stream()
                .map(StudentMapper::mapToStudentResponseDTO)
                .collect(Collectors.toList());
    }
}