package com.example.ems.service;

import com.example.ems.dto.EmployeeRequest;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.model.Employee;
import com.example.ems.repository.EmployeeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> findAll(String search) {
        if (search == null || search.isBlank()) return repository.findAll();
        String term = search.trim();
        return repository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartmentContainingIgnoreCase(
            term, term, term, term);
    }

    public Employee findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
    }

    @Transactional
    public Employee create(EmployeeRequest request) {
        if (repository.existsByEmailIgnoreCase(request.getEmail().trim())) {
            throw new IllegalArgumentException("An employee with this email already exists.");
        }
        Employee employee = new Employee();
        apply(employee, request);
        try {
            return repository.save(employee);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("An employee with this email already exists.");
        }
    }

    @Transactional
    public Employee update(Long id, EmployeeRequest request) {
        Employee employee = findById(id);
        if (repository.existsByEmailIgnoreCaseAndIdNot(request.getEmail().trim(), id)) {
            throw new IllegalArgumentException("An employee with this email already exists.");
        }
        apply(employee, request);
        try {
            return repository.save(employee);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("An employee with this email already exists.");
        }
    }

    @Transactional
    public void delete(Long id) {
        Employee employee = findById(id);
        repository.delete(employee);
    }

    private void apply(Employee employee, EmployeeRequest request) {
        employee.setFirstName(request.getFirstName().trim());
        employee.setLastName(request.getLastName().trim());
        employee.setEmail(request.getEmail().trim().toLowerCase());
        employee.setDepartment(request.getDepartment().trim());
        employee.setJobTitle(request.getJobTitle().trim());
        employee.setSalary(request.getSalary());
    }
}
