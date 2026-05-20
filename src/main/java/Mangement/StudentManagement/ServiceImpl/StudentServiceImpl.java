package Mangement.StudentManagement.ServiceImpl;
import Mangement.StudentManagement.Entity.Address;
import Mangement.StudentManagement.Entity.Student;
import Mangement.StudentManagement.Entity.Course;
import Mangement.StudentManagement.Mapper.AddressMapper;
import Mangement.StudentManagement.Repository.*;
import Mangement.StudentManagement.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Mangement.StudentManagement.DTO.Request.*;
import Mangement.StudentManagement.DTO.Response.*;
import Mangement.StudentManagement.Mapper.StudentMapper;

import java.util.stream.Collectors;
import Mangement.StudentManagement.Exception.*;
import java.util.*;
@Service
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentrepo;
    private final CourseRepository courserepo;
    private final AddressRepository addressrepo;

    public StudentServiceImpl(StudentRepository studentrepo,
                              CourseRepository courserepo,
                              AddressRepository addressrepo) {
        this.studentrepo = studentrepo;
        this.courserepo = courserepo;
        this.addressrepo = addressrepo;
    }
    @Override
    public StudentResponseDTO saveStudent(StudentRequestDTO dto){
       Course course=courserepo.findById(dto.getCourseId())
               .orElseThrow(()->new CourseNotFoundException("Course not Found"));
       Address address=addressrepo.findById(dto.getAddressId())
               .orElseThrow(()->new AddressNotFoundException("Address not Found"));
       Student student=StudentMapper.mapToStudent(dto,course,address);
        Student savestudent=studentrepo.save(student);
        return StudentMapper.mapToStudentResponseDTO(savestudent);
    }
    @Override
    public List<StudentResponseDTO>getAllStudents(){
        List<Student>student=studentrepo.findAll();
        return student.stream().map(StudentMapper::mapToStudentResponseDTO).collect(Collectors.toList());
    }
    @Override
    public StudentResponseDTO getStudentById(int id){
        Student student=studentrepo.findById(id).orElseThrow(() -> new StudentNotFoundException("Student not found with id :" + id));
        return StudentMapper.mapToStudentResponseDTO(student);
    }
    @Override
    public StudentResponseDTO updateStudent(int id,StudentRequestDTO dto){
        Student existingstudent=studentrepo.findById(id).orElseThrow(()->new StudentNotFoundException("Student not found"));
        Course course=courserepo.findById(dto.getCourseId()).orElseThrow(()->new CourseNotFoundException("Course not Found"));
        Address address=addressrepo.findById(dto.getAddressId()).orElseThrow(()->new AddressNotFoundException("Address not Found"));
        existingstudent.setName(dto.getName());
        existingstudent.setEmail(dto.getEmail());
        existingstudent.setCourse(course);
        existingstudent.setAddress(address);
        Student updated= studentrepo.save(existingstudent);
        return StudentMapper.mapToStudentResponseDTO(updated);

    }
   @Override
    public boolean existsById(int id)
   {
        return studentrepo.existsById(id);
    }
    @Override
    public long countStudents(){

        return studentrepo.count();
    }
   @Override

   public AddressResponseDTO getStudentAddressById(int studentId) {

       Student student = studentrepo.findById(studentId)
               .orElseThrow(() -> new StudentNotFoundException("Student not found"));

       return AddressMapper.mapToAddressResponseDTO(student.getAddress());
   }
    @Override
    public void deleteStudentById(int id){

        if (!studentrepo.existsById(id)) {
            throw new StudentNotFoundException("Student not found");
        }
        studentrepo.deleteById(id);
    }
}
