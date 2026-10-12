
package com.sgs.server;

import com.sgs.server.Objects.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServicer {

    private final JwtServicer jwtServicer;
    private final HashingServicer hashingServicer;
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;

    public AuthenticationServicer(
            JwtServicer jwtServicer,
            HashingServicer hashingServicer,
            StudentRepository studentRepository,
            EmployeeRepository employeeRepository
    ) {
        this.jwtServicer = jwtServicer;
        this.hashingServicer = hashingServicer;
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
    }

    // Returns scholar number
    @Transactional
    public Long create(StudentCreateDTO dto) {
        Student student = new Student();

        student.name = dto.getName();
        student.dob = dto.getDob();
        student.rollNumber = dto.getRollNumber();
        student.standard = Standard.valueOf(dto.getStandard());
        student.title = Title.valueOf(dto.getTitle());
        student.section = dto.getSection();
        student.isEnrolled = true;
        student.hashedPassword = hashingServicer.hash(dto.getPassword());

        Student savedStudent = studentRepository.save(student);

        return savedStudent.scholarNumber;
    }

    // Returns employee ID
    @Transactional
    public Long create(EmployeeCreateDTO dto) {
        Employee employee = new Employee();

        employee.name = dto.getName();
        employee.dob = dto.getDob();
        employee.salary = dto.getSalary();

        // New employees must not become administrators
        // simply because a client submitted isAdmin=true.
        employee.isAdmin = false;
        employee.isEmployed = true;

        employee.hashedPassword = hashingServicer.hash(dto.getPassword());

        Employee savedEmployee = employeeRepository.save(employee);

        return savedEmployee.idNumber;
    }

    public String login(LoginDTO dto) {

        if (dto.getUserType() == null || dto.getPassword() == null) {
            throw new InvalidCredentialsException();
        }

        if (dto.getUserType() == UserType.EMPLOYEE) {

            Employee employee = employeeRepository
                    .findByIdNumber(dto.getId())
                    .orElseThrow(InvalidCredentialsException::new);

            if (employee.getHashedPassword() == null
                    || !hashingServicer.matches(
                    dto.getPassword(),
                    employee.getHashedPassword()
            )) {
                throw new InvalidCredentialsException();
            }

            return jwtServicer.generateToken(
                    employee.getIdNumber(),
                    UserType.EMPLOYEE
            );
        }

        if (dto.getUserType() == UserType.STUDENT) {

            Student student = studentRepository
                    .findByScholarNumber(dto.getId())
                    .orElseThrow(InvalidCredentialsException::new);

            if (student.getHashedPassword() == null
                    || !hashingServicer.matches(
                    dto.getPassword(),
                    student.getHashedPassword()
            )) {
                throw new InvalidCredentialsException();
            }

            return jwtServicer.generateToken(
                    student.getScholarNumber(),
                    UserType.STUDENT
            );
        }

        throw new InvalidCredentialsException();
    }
}
