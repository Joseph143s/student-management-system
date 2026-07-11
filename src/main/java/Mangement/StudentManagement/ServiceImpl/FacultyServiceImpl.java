package Mangement.StudentManagement.ServiceImpl;

import Mangement.StudentManagement.DTO.Request.FacultyRequestDTO;
import Mangement.StudentManagement.DTO.Response.FacultyResponseDTO;
import Mangement.StudentManagement.Entity.Department;
import Mangement.StudentManagement.Entity.Faculty;
import Mangement.StudentManagement.Exception.DepartmentNotFoundException;
import Mangement.StudentManagement.Exception.FacultyNotFoundException;
import Mangement.StudentManagement.Mapper.FacultyMapper;
import Mangement.StudentManagement.Repository.DepartmentRepository;
import Mangement.StudentManagement.Repository.FacultyRepository;
import Mangement.StudentManagement.Service.FacultyService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {

    // ✅ Extracted as constant — easy to maintain
    private static final Map<String, List<String>> DEPARTMENT_SPECIALIZATIONS = Map.of(
            "CSE",        List.of("Java", "Python", "AI"),
            "ECE",        List.of("VLSI", "Embedded"),
            "Mechanical", List.of("CAD", "Thermodynamics")
    );

    private final FacultyRepository facultyrepo;
    private final DepartmentRepository departmentrepo;

    public FacultyServiceImpl(FacultyRepository facultyrepo,
                              DepartmentRepository departmentrepo) {
        this.facultyrepo = facultyrepo;
        this.departmentrepo = departmentrepo;
    }

    // ─── Validation Helper ────────────────────────────────────────

    private void validateSpecialization(Department department, String specialization) {
        List<String> allowed = DEPARTMENT_SPECIALIZATIONS
                .get(department.getDepartmentName());
        if (allowed == null || !allowed.contains(specialization)) {
            throw new RuntimeException(
                    "Invalid specialization for "
                            + department.getDepartmentName()
                            + " department");
        }
    }

    // ─── Save ─────────────────────────────────────────────────────

    @Override
    public FacultyResponseDTO saveFaculty(FacultyRequestDTO dto) {

        Department department = departmentrepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + dto.getDepartmentId()));

        validateSpecialization(department, dto.getSpecialization()); // ✅ extracted

        Faculty faculty = FacultyMapper.mapToFaculty(dto, department);
        Faculty saved = facultyrepo.save(faculty);
        return FacultyMapper.mapToFacultyResponseDTO(saved);
    }

    // ─── Get All ──────────────────────────────────────────────────

    @Override
    public List<FacultyResponseDTO> getAllFaculties() {
        return facultyrepo.findAll()
                .stream()
                .map(FacultyMapper::mapToFacultyResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Get By Id ────────────────────────────────────────────────

    @Override
    public FacultyResponseDTO getFacultyById(int facultyId) {
        Faculty faculty = facultyrepo.findById(facultyId)
                .orElseThrow(() -> new FacultyNotFoundException(
                        "Faculty not found with id: " + facultyId));
        return FacultyMapper.mapToFacultyResponseDTO(faculty);
    }

    // ─── Update ───────────────────────────────────────────────────

    @Override
    public FacultyResponseDTO updateFaculty(int facultyId, FacultyRequestDTO dto) {

        Faculty existing = facultyrepo.findById(facultyId)
                .orElseThrow(() -> new FacultyNotFoundException(
                        "Faculty not found with id: " + facultyId));

        Department department = departmentrepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + dto.getDepartmentId()));

        validateSpecialization(department, dto.getSpecialization()); // ✅ added

        existing.setFacultyName(dto.getFacultyName());
        existing.setEmail(dto.getEmail());
        existing.setSpecialization(dto.getSpecialization());
        existing.setDepartment(department);

        return FacultyMapper.mapToFacultyResponseDTO(facultyrepo.save(existing));
    }

    // ─── Delete ───────────────────────────────────────────────────

    @Override
    public void deleteFaculty(int facultyId) {
        Faculty faculty = facultyrepo.findById(facultyId)
                .orElseThrow(() -> new FacultyNotFoundException(
                        "Faculty not found with id: " + facultyId));
        facultyrepo.delete(faculty);
    }

    // ─── Exists / Count ───────────────────────────────────────────

    @Override
    public boolean existsById(int facultyId) {
        return facultyrepo.existsById(facultyId); // ✅ was calling itself — StackOverflowError
    }

    @Override
    public long countFaculities() {
        return facultyrepo.count();
    }

    // ─── By Department ────────────────────────────────────────────

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

    // ─── Search & Sort ────────────────────────────────────────────

    @Override
    public List<FacultyResponseDTO> searchFacultyByName(String name) {
        return facultyrepo.findByFacultyNameContainingIgnoreCase(name)
                .stream()
                .map(FacultyMapper::mapToFacultyResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FacultyResponseDTO> sortFacultyByName() {
        return facultyrepo.findAll(Sort.by("facultyName"))
                .stream()
                .map(FacultyMapper::mapToFacultyResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FacultyResponseDTO> sortFacultyBySpecialization() {
        return facultyrepo.findAll(Sort.by("specialization"))
                .stream()
                .map(FacultyMapper::mapToFacultyResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Specializations ──────────────────────────────────────────

    @Override
    public List<String> getFacultySpecializations(int departmentId) {
        departmentrepo.findById(departmentId)  // ✅ existence check added
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + departmentId));

        return facultyrepo.findByDepartmentDepartmentId(departmentId)
                .stream()
                .map(Faculty::getSpecialization)
                .distinct()
                .collect(Collectors.toList());
    }
}