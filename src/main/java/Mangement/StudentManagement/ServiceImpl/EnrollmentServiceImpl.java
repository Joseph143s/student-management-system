package Mangement.StudentManagement.ServiceImpl;

import Mangement.StudentManagement.DTO.Request.EnrollmentRequestDTO;
import Mangement.StudentManagement.DTO.Response.EnrollmentResponseDTO;
import Mangement.StudentManagement.Entity.Course;
import Mangement.StudentManagement.Entity.Enrollment;
import Mangement.StudentManagement.Entity.Student;
import Mangement.StudentManagement.Enum.EnrollmentStatus;
import Mangement.StudentManagement.Exception.CourseNotFoundException;
import Mangement.StudentManagement.Exception.EnrollmentNotFoundException;
import Mangement.StudentManagement.Exception.StudentNotFoundException;
import Mangement.StudentManagement.Mapper.EnrollmentMapper;
import Mangement.StudentManagement.Repository.CourseRepository;
import Mangement.StudentManagement.Repository.EnrollmentRepository;
import Mangement.StudentManagement.Repository.StudentRepository;
import Mangement.StudentManagement.Service.EnrollmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentrepo;
    private final StudentRepository studentrepo;
    private final CourseRepository courserepo;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentrepo,
                                 StudentRepository studentrepo,
                                 CourseRepository courserepo) {
        this.enrollmentrepo = enrollmentrepo;
        this.studentrepo = studentrepo;
        this.courserepo = courserepo;
    }

    // ─── Core CRUD ────────────────────────────────────────────────

    @Override
    public EnrollmentResponseDTO enroll(EnrollmentRequestDTO dto) {

        Student student = studentrepo.findById(dto.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id: " + dto.getStudentId()));

        Course course = courserepo.findById(dto.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException(
                        "Course not found with id: " + dto.getCourseId()));

        // Prevent duplicate active enrollment
        Optional<Enrollment> existing =
                enrollmentrepo.findByStudentAndCourse(student, course);
        if (existing.isPresent() &&
                existing.get().getStatus() == EnrollmentStatus.ACTIVE) {
            throw new RuntimeException(
                    "Student is already actively enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        Enrollment saved = enrollmentrepo.save(enrollment);
        return EnrollmentMapper.mapToEnrollmentResponseDTO(saved);
    }

    @Override
    public EnrollmentResponseDTO getEnrollmentById(int enrollmentId) {

        Enrollment enrollment = enrollmentrepo.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Enrollment not found with id: " + enrollmentId));

        return EnrollmentMapper.mapToEnrollmentResponseDTO(enrollment);
    }

    @Override
    public List<EnrollmentResponseDTO> getAllEnrollments() {

        return enrollmentrepo.findAll()
                .stream()
                .map(EnrollmentMapper::mapToEnrollmentResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEnrollment(int enrollmentId) {

        if (!enrollmentrepo.existsById(enrollmentId)) {
            throw new EnrollmentNotFoundException(
                    "Enrollment not found with id: " + enrollmentId);
        }
        enrollmentrepo.deleteById(enrollmentId);
    }

    // ─── Status Management ────────────────────────────────────────

    @Override
    public EnrollmentResponseDTO dropCourse(int enrollmentId) {

        Enrollment enrollment = enrollmentrepo.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Enrollment not found with id: " + enrollmentId));

        if (enrollment.getStatus() == EnrollmentStatus.COMPLETED) {
            throw new RuntimeException(
                    "Cannot drop a course that is already completed");
        }

        enrollment.setStatus(EnrollmentStatus.DROPPED);
        Enrollment updated = enrollmentrepo.save(enrollment);
        return EnrollmentMapper.mapToEnrollmentResponseDTO(updated);
    }

    @Override
    public EnrollmentResponseDTO completeCourse(int enrollmentId, String grade) {

        Enrollment enrollment = enrollmentrepo.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Enrollment not found with id: " + enrollmentId));

        if (enrollment.getStatus() == EnrollmentStatus.DROPPED) {
            throw new RuntimeException(
                    "Cannot complete a course that has been dropped");
        }

        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollment.setGrade(grade);
        Enrollment updated = enrollmentrepo.save(enrollment);
        return EnrollmentMapper.mapToEnrollmentResponseDTO(updated);
    }

    @Override
    public EnrollmentResponseDTO updateStatus(int enrollmentId,
                                              EnrollmentStatus status) {

        Enrollment enrollment = enrollmentrepo.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Enrollment not found with id: " + enrollmentId));

        enrollment.setStatus(status);
        Enrollment updated = enrollmentrepo.save(enrollment);
        return EnrollmentMapper.mapToEnrollmentResponseDTO(updated);
    }

    // ─── Queries ──────────────────────────────────────────────────

    @Override
    public List<EnrollmentResponseDTO> getEnrollmentsByStudent(int studentId) {

        Student student = studentrepo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id: " + studentId));

        return enrollmentrepo.findByStudent(student)
                .stream()
                .map(EnrollmentMapper::mapToEnrollmentResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentResponseDTO> getEnrollmentsByCourse(int courseId) {

        Course course = courserepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Course not found with id: " + courseId));

        return enrollmentrepo.findByCourse(course)
                .stream()
                .map(EnrollmentMapper::mapToEnrollmentResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentResponseDTO> getEnrollmentsByStatus(
            EnrollmentStatus status) {

        return enrollmentrepo.findByStatus(status)
                .stream()
                .map(EnrollmentMapper::mapToEnrollmentResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Update Enrollments ───────────────────────────────────────

    @Override
    public void updateEnrollments(int studentId, List<Integer> newCourseIds) {

        Student student = studentrepo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id: " + studentId));

        List<Enrollment> existing = enrollmentrepo.findByStudent(student);

        // Drop enrollments not in new list
        existing.stream()
                .filter(e -> !newCourseIds.contains(
                        e.getCourse().getCourseId()))
                .forEach(e -> {
                    e.setStatus(EnrollmentStatus.DROPPED);
                    enrollmentrepo.save(e);
                });

        // Enroll in new courses not already active
        List<Integer> existingCourseIds = existing.stream()
                .map(e -> e.getCourse().getCourseId())
                .collect(Collectors.toList());

        newCourseIds.stream()
                .filter(id -> !existingCourseIds.contains(id))
                .forEach(id -> {
                    EnrollmentRequestDTO dto = new EnrollmentRequestDTO();
                    dto.setStudentId(studentId);
                    dto.setCourseId(id);
                    enroll(dto);
                });
    }

    // ─── Count ────────────────────────────────────────────────────

    @Override
    public long countEnrollmentsByCourse(int courseId) {

        Course course = courserepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Course not found with id: " + courseId));

        return enrollmentrepo.findByCourse(course).size();
    }
}