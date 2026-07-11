package Mangement.StudentManagement.ServiceImpl;

import Mangement.StudentManagement.DTO.Request.CourseRequestDTO;
import Mangement.StudentManagement.DTO.Response.CourseResponseDTO;
import Mangement.StudentManagement.DTO.Response.StudentResponseDTO;
import Mangement.StudentManagement.Entity.Course;
import Mangement.StudentManagement.Entity.Department;
import Mangement.StudentManagement.Entity.Enrollment;
import Mangement.StudentManagement.Exception.CourseNotFoundException;
import Mangement.StudentManagement.Exception.DepartmentNotFoundException;
import Mangement.StudentManagement.Mapper.CourseMapper;
import Mangement.StudentManagement.Mapper.StudentMapper;
import Mangement.StudentManagement.Repository.CourseRepository;
import Mangement.StudentManagement.Repository.DepartmentRepository;
import Mangement.StudentManagement.Repository.EnrollmentRepository;
import Mangement.StudentManagement.Service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courserepo;
    private final DepartmentRepository departmentrepo;
    private final EnrollmentRepository enrollmentrepo; // ✅ replaces StudentRepository

    public CourseServiceImpl(CourseRepository courserepo,
                             DepartmentRepository departmentrepo,
                             EnrollmentRepository enrollmentrepo) {
        this.courserepo = courserepo;
        this.departmentrepo = departmentrepo;
        this.enrollmentrepo = enrollmentrepo;
    }

    // ─── Save ─────────────────────────────────────────────────────

    @Override

    public CourseResponseDTO saveCourse(CourseRequestDTO dto) {


        Department department = departmentrepo.findById(dto.getDepartmentId())
                .orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department not found with id : " + dto.getDepartmentId()
                        )
                );


        Course course = CourseMapper.mapToCourse(dto);


        course.setDepartment(department);


        Course saved = courserepo.save(course);

        //  Return response
        return CourseMapper.mapToCourseResponseDTO(saved);
    }
    // ─── Get All ──────────────────────────────────────────────────

    @Override
    public List<CourseResponseDTO> getAllCourses() {
        return courserepo.findAll()
                .stream()
                .map(CourseMapper::mapToCourseResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Get By Id ────────────────────────────────────────────────

    @Override
    public CourseResponseDTO getCourseById(int id) {
        Course course = courserepo.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Course not found with id: " + id));
        return CourseMapper.mapToCourseResponseDTO(course);
    }

    // ─── Update ───────────────────────────────────────────────────

    @Override
    public CourseResponseDTO updateCourse(int id, CourseRequestDTO dto) {
        Course existing = courserepo.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Course not found with id: " + id));

        existing.setCoursename(dto.getCoursename());
        existing.setDuration(dto.getDuration());
        existing.setFee(dto.getFee());

        return CourseMapper.mapToCourseResponseDTO(courserepo.save(existing));
    }

    // ─── Delete ───────────────────────────────────────────────────

    @Override
    public void deleteCourseById(int id) {
        Course course = courserepo.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Course not found with id: " + id));
        courserepo.delete(course);
    }

    // ─── Exists / Count ───────────────────────────────────────────

    @Override
    public boolean existsById(int id) {
        return courserepo.existsById(id);
    }

    @Override
    public long countCourses() {
        return courserepo.count();
    }

    // ─── By Department ────────────────────────────────────────────

    @Override
    public List<CourseResponseDTO> getCoursesByDepartment(int departmentId) {
        departmentrepo.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + departmentId));

        return courserepo.findByDepartmentDepartmentId(departmentId)
                .stream()
                .map(CourseMapper::mapToCourseResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Students In Course ───────────────────────────────────────

    @Override
    public List<StudentResponseDTO> getStudentsInCourse(int courseId) {
        courserepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Course not found with id: " + courseId));

        // ✅ Query through enrollments
        return enrollmentrepo.findByCourse_CourseId(courseId)
                .stream()
                .map(Enrollment::getStudent)
                .map(StudentMapper::mapToStudentResponseDTO)
                .collect(Collectors.toList());
    }
}