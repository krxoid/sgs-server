package com.sgs.server;

import com.sgs.server.Objects.Employee;
import com.sgs.server.Objects.EmployeeRepository;
import com.sgs.server.Objects.Student;
import com.sgs.server.Objects.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Servicer {

    private StudentRepository studentRepository;
    private EmployeeRepository employeeRepository;

    Servicer(
            StudentRepository studentRepository,
            EmployeeRepository employeeRepository
    ) {
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;

    }

    public ResponseEntity<String> getStatus(){
        return ResponseEntity.ok().body("Server is running");
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
