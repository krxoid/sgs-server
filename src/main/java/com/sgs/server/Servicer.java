
package com.sgs.server;

import com.sgs.server.Objects.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class Servicer {

    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final AnnouncementRepository announcementRepository;
    private final HashingServicer hashingServicer;

    public Servicer(
            StudentRepository studentRepository,
            EmployeeRepository employeeRepository,
            AnnouncementRepository announcementRepository,
            HashingServicer hashingServicer
    ) {
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
        this.announcementRepository = announcementRepository;
        this.hashingServicer = hashingServicer;
    }

    @Transactional
    public void updateStudent(
            Student student,
            StudentUpdateDTO dto
    ) {
        if (dto.getName() != null) {
            student.setName(dto.getName());
        }

        if (dto.getDob() != null) {
            student.setDob(dto.getDob());
        }

        if (dto.getRollNumber() != null) {
            student.setRollNumber(dto.getRollNumber());
        }

        if (dto.getStandard() != null) {
            student.setStandard(
                    Standard.valueOf(dto.getStandard())
            );
        }

        if (dto.getTitle() != null) {
            student.setTitle(
                    Title.valueOf(dto.getTitle())
            );
        }

        if (dto.getSection() != null) {
            student.setSection(dto.getSection());
        }

        if (dto.getPassword() != null
                && !dto.getPassword().isBlank()) {
            student.setHashedPassword(
                    hashingServicer.hash(dto.getPassword())
            );
        }
    }

    @Transactional
    public void updateEmployee(
            Employee employee,
            EmployeeUpdateDTO dto
    ) {
        // Admin privileges are intentionally not modified here.
        // Use a separate admin-authorized operation for that.

        if (dto.getName() != null) {
            employee.setName(dto.getName());
        }

        if (dto.getDob() != null) {
            employee.setDob(dto.getDob());
        }

        if (dto.getPassword() != null
                && !dto.getPassword().isBlank()) {
            employee.setHashedPassword(
                    hashingServicer.hash(dto.getPassword())
            );
        }

        if (dto.getIsEmployed() != null) {
            employee.setEmployed(dto.getIsEmployed());
        }

        if (dto.getSalary() != null) {
            employee.setSalary(dto.getSalary());
        }
    }

    @Transactional
    public void createAnnouncement(
            AnnouncementDTO dto,
            Long employeeId
    ) {
        Announcement announcement = new Announcement();

        announcement.author = employeeId;
        announcement.title = dto.getTitle();
        announcement.content = dto.getContent();
        announcement.classes = dto.getClasses();
        announcement.createdAt = LocalDateTime.now();

        announcementRepository.save(announcement);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Announcement not found"
                ));

        announcementRepository.delete(announcement);
    }

    public List<Announcement> getAnnouncements() {
        return announcementRepository.findAll();
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long idNumber) {
        return studentRepository.findByScholarNumber(idNumber);
    }

    public Optional<Employee> getEmployeeById(Long idNumber) {
        return employeeRepository.findByIdNumber(idNumber);
    }

    public Optional<Announcement> getAnnouncementById(Long idNumber) {
        return announcementRepository.findById(idNumber);
    }
}
