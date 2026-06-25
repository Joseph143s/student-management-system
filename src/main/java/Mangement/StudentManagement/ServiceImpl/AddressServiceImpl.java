package Mangement.StudentManagement.ServiceImpl;

import Mangement.StudentManagement.DTO.Request.AddressRequestDTO;
import Mangement.StudentManagement.DTO.Response.AddressResponseDTO;
import Mangement.StudentManagement.Entity.Address;
import Mangement.StudentManagement.Entity.Student;
import Mangement.StudentManagement.Exception.AddressNotFoundException;
import Mangement.StudentManagement.Exception.StudentNotFoundException;
import Mangement.StudentManagement.Mapper.AddressMapper;
import Mangement.StudentManagement.Repository.AddressRepository;
import Mangement.StudentManagement.Repository.StudentRepository;
import Mangement.StudentManagement.Service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressrepo;
    private final StudentRepository studentrepo;  // add this

    public AddressServiceImpl(AddressRepository addressrepo,
                              StudentRepository studentrepo) {
        this.addressrepo = addressrepo;
        this.studentrepo = studentrepo;
    }

    @Override
    public AddressResponseDTO saveAddress(AddressRequestDTO dto) {
        // fetch student
        Student student = studentrepo.findById(dto.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with id: " + dto.getStudentId()));

        Address address = AddressMapper.mapToAddress(dto);
        address.setStudent(student);  // link address to student
        Address saved = addressrepo.save(address);
        return AddressMapper.mapToAddressResponseDTO(saved);
    }

    // ─── Get All ──────────────────────────────────────────────────

    @Override
    public List<AddressResponseDTO> getAllAddresses() {
        return addressrepo.findAll()
                .stream()
                .map(AddressMapper::mapToAddressResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Get By Id ────────────────────────────────────────────────

    @Override
    public AddressResponseDTO getAddressById(int id) {
        Address address = addressrepo.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(
                        "Address not found with that id: " + id)); // ✅ fixed exception + typo
        return AddressMapper.mapToAddressResponseDTO(address);
    }

    // ─── Update ───────────────────────────────────────────────────

    @Override
    public AddressResponseDTO updateAddress(int id, AddressRequestDTO dto) {
        Address existing = addressrepo.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(
                        "Address not found with that id: " + id));

        existing.setCity(dto.getCity());
        existing.setState(dto.getState());
        existing.setPincode(dto.getPincode());

        return AddressMapper.mapToAddressResponseDTO(addressrepo.save(existing));
    }

    // ─── Delete ───────────────────────────────────────────────────

    @Override
    public void deleteAddressById(int id) {
        Address address = addressrepo.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(
                        "Address not found with that id: " + id));
        addressrepo.delete(address);
    }

    // ─── Exists / Count ───────────────────────────────────────────

    @Override
    public boolean existsById(int id) {
        return addressrepo.existsById(id);
    }

    @Override
    public long countAddresses() {
        return addressrepo.count();
    }
}