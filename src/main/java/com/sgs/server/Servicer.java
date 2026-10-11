package com.sgs.server;

import com.sgs.server.Objects.*;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Servicer {

    private StudentRepository studentRepository;
    private EmployeeRepository employeeRepository;
    private AnnouncementRepository announcementRepository;
    private HashingServicer hashingServicer;

    Servicer(
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
    public void updateStudent(Student student, StudentUpdateDTO dto) {

        if (dto.getName() != null)
            student.setName(dto.getName());

        if (dto.getDob() != null)
            student.setDob(dto.getDob());

        if (dto.getRollNumber() != null)
            student.setRollNumber(dto.getRollNumber());

        if (dto.getStandard() != null)
            student.setStandard(Standard.valueOf(dto.getStandard()));

        if (dto.getTitle() != null)
            student.setTitle(Title.valueOf(dto.getTitle()));

        if (dto.getSection() != null)
            student.setSection(dto.getSection());

        if (dto.getPassword() != null)
            student.setHashedPassword(hashingServicer.hash(dto.getPassword()));
    }

    @Transactional
    public void updateEmployee(Employee employee, EmployeeUpdateDTO dto) {

        if (dto.getIsAdmin() != null)
            employee.setAdmin(dto.getIsAdmin());

        if (dto.getName() != null)
            employee.setName(dto.getName());

        if (dto.getDob() != null)
            employee.setDob(dto.getDob());

        if (dto.getPassword() != null)
            employee.setHashedPassword(hashingServicer.hash(dto.getPassword()));

        if (dto.getIsEmployed() != null)
            employee.setEmployed(dto.getIsEmployed());

        if (dto.getSalary() != null)
            employee.setSalary(dto.getSalary());
    }

    public void createAnnouncement(String title, String content, Long authorID) {

        Announcement announcement = new Announcement();

        announcement.title = title;
        announcement.author = authorID;
        announcement.content = content;

        announcementRepository.save(announcement);
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

}
