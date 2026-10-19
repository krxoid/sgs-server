
package com.sgs.server;

import com.sgs.server.Objects.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class Servicer {

    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final AnnouncementRepository announcementRepository;
    private final HomeworkRepository homeworkRepository;
    private final HashingServicer hashingServicer;

    public Servicer(
            StudentRepository studentRepository,
            EmployeeRepository employeeRepository,
            AnnouncementRepository announcementRepository,
            HomeworkRepository homeworkRepository,
            HashingServicer hashingServicer
    ) {
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
        this.announcementRepository = announcementRepository;
        this.homeworkRepository = homeworkRepository;
        this.hashingServicer = hashingServicer;
    }

    @Transactional
    public void updateStudent(
            Long scholarNumber,
            StudentUpdateDTO dto
    ) {

        Student student = studentRepository.findByScholarNumber(scholarNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found with Scholar Number: " + scholarNumber
                ));

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
            Long employeeId,
            EmployeeUpdateDTO dto
    ) {
        // Admin privileges are intentionally not modified here.
        // Use a separate admin-authorized operation for that.

        Employee employee = employeeRepository.findByIdNumber(employeeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Employee not found with ID: " + employeeId
                ));

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
    public Long createAnnouncement(
            AnnouncementDTO dto
    ) {
        Announcement announcement = new Announcement();

        announcement.author = SecurityHolderContext.getCurrentUserId();
        announcement.title = dto.getTitle();
        announcement.content = dto.getContent();
        announcement.classes = dto.getClasses() == null
                ? new Standard[0]
                : Arrays.stream(dto.getClasses())
                .map(Standard::valueOf)
                .toArray(Standard[]::new);
        announcement.sections = dto.getSections() == null
                ? new Byte[0]
                : dto.getSections();

        return announcementRepository.save(announcement).getId();
    }

    public void updateAnnouncement(
            Long id,
            AnnouncementDTO dto
    ) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Employee not found with ID: " + id
                ));

        announcement.title = dto.getTitle();
        announcement.content = dto.getContent();
        announcement.classes = dto.getClasses() == null
                ? new Standard[0]
                : Arrays.stream(dto.getClasses())
                .map(Standard::valueOf)
                .toArray(Standard[]::new);
        announcement.sections = dto.getSections() == null
                ? new Byte[0]
                : dto.getSections();

        announcementRepository.save(announcement);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Employee not found with ID: " + id
                ));

        announcementRepository.delete(announcement);
    }

    public List<Announcement> getVisibleAnnouncements() {
        Student student = studentRepository.findByScholarNumber(SecurityHolderContext.getCurrentUserId()).get();

        return announcementRepository.findVisibleAnnouncements(
                student.getClass().getName(),
                student.getSection()
        );
    }

    public List<Announcement> getAnnouncements() {
        return announcementRepository.findAll();
    }

    public Long createHomework(
            HomeworkDTO dto
    ) {
        Homework homework = new Homework();

        homework.author = SecurityHolderContext.getCurrentUserId();
        homework.section = dto.getSection();
        homework.standard = Standard.valueOf(dto.getStandard());
        homework.work = dto.getWork();

        return homeworkRepository.save(homework).getId();
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long idNumber) {
        return studentRepository.findByScholarNumber(idNumber);
    }

    public Optional<Announcement> getAnnouncementById(Long idNumber) {
        return announcementRepository.findById(idNumber);
    }
}
