package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.entity.Location;
import com.example.monitoringsystem.entity.Userr;
import com.example.monitoringsystem.model.NewDepartment;
import com.example.monitoringsystem.repository.DepartmentRepository;
import com.example.monitoringsystem.repository.LocationRepository;
import com.example.monitoringsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public List<Department> getAllDepartmentsData(){
        return departmentRepository.findAllDepartments();
    }
    public void saveNewDepartment(NewDepartment newDepartment){
        Location location = Location.builder()
                .lon(newDepartment.getLon())
                .lat(newDepartment.getLat())
                .build();

        Department department = Department.builder()
                .address(newDepartment.getAddress())
                .departmentName(newDepartment.getDepartmentName())
                .location(location)
                .build();

        locationRepository.save(location);
        departmentRepository.save(department);

    }
    public Department findDepartmentOfUser(Long userId){
        Userr found = userRepository.getReferenceById(userId);
        return found.getDepartment();
    }
    public List<Department> findAllDepartments(){
        return departmentRepository.findAllDepartments();
    }

}
