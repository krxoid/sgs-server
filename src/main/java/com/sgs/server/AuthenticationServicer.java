package com.sgs.server;

import com.sgs.server.Objects.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServicer {

    private JWTServicer jwtServicer;
    private HashingServicer hashingServicer;
    private StudentRepository studentRepository;
    private EmployeeRepository employeeRepository;

    AuthenticationServicer(
            JWTServicer jwtServicer,
            HashingServicer hashingServicer,
            StudentRepository studentRepository,
            EmployeeRepository employeeRepository
    ) {
        this.jwtServicer = jwtServicer;
        this.hashingServicer = hashingServicer;
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
    }

    //Returns scholar number
    @Transactional
    public Long create(StudentCreateDTO dto) {
        Student student = new Student();

        student.name = dto.getName();
        student.dob = dto.getDob();
        student.admissionDate = dto.getAdmissionDate();
        student.rollNumber = dto.getRollNumber();
        student.standard = Standard.valueOf(dto.getStandard());
        student.title = Title.valueOf(dto.getTitle());
        student.section = dto.getSection();
        student.isEnrolled = true;
        student.hashedPassword = hashingServicer.hash(dto.getPassword());

        studentRepository.save(student);

        return student.scholarNumber;
    }

    @Transactional
    public Long create(EmployeeCreateDTO dto) {
        Employee employee = new Employee();

        employee.name = dto.getName();
        employee.dob = dto.getDob();
        employee.hiringDate = dto.getHiringDate();
        employee.salary = dto.getSalary();
        employee.isAdmin = dto.isAdmin();
        employee.isEmployed = true;

        employeeRepository.save(employee);

        return employee.idNumber;
    }

    public String login(LoginDTO dto) {

        if (dto.getUserType() == UserType.EMPLOYEE) {

            Employee employee = employeeRepository
                    .findByIdNumber(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

            if (!hashingServicer.matches(
                    dto.getPassword(),
                    employee.getHashedPassword()
            )) {
                throw new RuntimeException("Invalid credentials");
            }

            return jwtServicer.generateToken(
                    employee.getIdNumber(),
                    UserType.EMPLOYEE
            );
        }

        if (dto.getUserType() == UserType.STUDENT) {

            Student student = studentRepository
                    .findByScholarNumber(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

            if (!hashingServicer.matches(
                    dto.getPassword(),
                    student.getHashedPassword()
            )) {
                throw new RuntimeException("Invalid credentials");
            }

            return jwtServicer.generateToken(
                    student.getScholarNumber(),
                    UserType.STUDENT
            );
        }

        throw new RuntimeException("Invalid credentials");
    }

}
