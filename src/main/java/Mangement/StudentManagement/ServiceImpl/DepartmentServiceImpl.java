package Mangement.StudentManagement.ServiceImpl;

import Mangement.StudentManagement.DTO.Request.DepartmentRequestDTO;
import Mangement.StudentManagement.DTO.Response.DepartmentResponseDTO;
import Mangement.StudentManagement.DTO.Response.FacultyResponseDTO;
import Mangement.StudentManagement.DTO.Response.StudentResponseDTO;
import Mangement.StudentManagement.Entity.Department;
import Mangement.StudentManagement.Entity.Faculty;
import Mangement.StudentManagement.Entity.Student;
import Mangement.StudentManagement.Exception.DepartmentNotFoundException;
import Mangement.StudentManagement.Mapper.DepartmentMapper;
import Mangement.StudentManagement.Mapper.FacultyMapper;
import Mangement.StudentManagement.Mapper.StudentMapper;
import Mangement.StudentManagement.Repository.DepartmentRepository;
import Mangement.StudentManagement.Repository.FacultyRepository;
import Mangement.StudentManagement.Repository.StudentRepository;
import Mangement.StudentManagement.Service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentrepo;
    private final FacultyRepository facultyrepo;
    private final StudentRepository studentrepo;

    public DepartmentServiceImpl(DepartmentRepository departmentrepo,
                                 FacultyRepository facultyrepo,
                                 StudentRepository studentrepo) {
        this.departmentrepo = departmentrepo;
        this.facultyrepo = facultyrepo;
        this.studentrepo = studentrepo;
    }

    // ─── Save ─────────────────────────────────────────────────────

    @Override
    public DepartmentResponseDTO saveDepartment(DepartmentRequestDTO dto) {
        Department department = DepartmentMapper.mapToDepartment(dto);
        Department saved = departmentrepo.save(department);
        return DepartmentMapper.mapToDepartmentResponseDTO(saved);
    }

    // ─── Get By Id ────────────────────────────────────────────────

    @Override
    public DepartmentResponseDTO getDepartmentById(int id) {
        Department department = departmentrepo.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + id));
        return DepartmentMapper.mapToDepartmentResponseDTO(department);
    }

    // ─── Get All ──────────────────────────────────────────────────

    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentrepo.findAll()
                .stream()
                .map(DepartmentMapper::mapToDepartmentResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Update ───────────────────────────────────────────────────

    @Override
    public DepartmentResponseDTO updateDepartment(int id, DepartmentRequestDTO dto) {
        Department existing = departmentrepo.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + id));

        existing.setDepartmentCode(dto.getDepartmentCode());
        existing.setDepartmentName(dto.getDepartmentName());

        return DepartmentMapper.mapToDepartmentResponseDTO(
                departmentrepo.save(existing));
    }

    // ─── Delete ───────────────────────────────────────────────────

    @Override
    public void deleteDepartmentById(int id) {
        Department department = departmentrepo.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + id));
        departmentrepo.delete(department);
    }

    // ─── Exists / Count ───────────────────────────────────────────

    @Override
    public boolean existsById(int id) {
        return departmentrepo.existsById(id);
    }

    @Override
    public long countDepartments() {
        return departmentrepo.count();
    }

    // ─── Students By Department ───────────────────────────────────

    @Override
    public List<StudentResponseDTO> getStudentsByDepartment(int departmentId) {
        departmentrepo.findById(departmentId)  // ✅ existence check added
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + departmentId));

        return studentrepo.findByDepartmentDepartmentId(departmentId)
                .stream()
                .map(StudentMapper::mapToStudentResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Faculty By Department ────────────────────────────────────

    @Override
    public List<FacultyResponseDTO> getFacultyByDepartment(int departmentId) {
        departmentrepo.findById(departmentId)  // ✅ existence check added
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + departmentId));

        return facultyrepo.findByDepartmentDepartmentId(departmentId)
                .stream()
                .map(FacultyMapper::mapToFacultyResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Counts ───────────────────────────────────────────────────

    @Override
    public long countStudentsByDepartment(int departmentId) {
        departmentrepo.findById(departmentId)  // ✅ existence check added
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + departmentId));

        return studentrepo.countByDepartmentDepartmentId(departmentId); // ✅ efficient count query
    }

    @Override
    public long countFacultyByDepartment(int departmentId) {
        departmentrepo.findById(departmentId)  // ✅ existence check added
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + departmentId));

        return facultyrepo.countByDepartmentDepartmentId(departmentId); // ✅ efficient count query
    }
}