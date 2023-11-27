package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.AbstractContainerBaseTest;
import com.example.monitoringsystem.controller.AuthenticationController;
import com.example.monitoringsystem.entity.Department;
import com.example.monitoringsystem.entity.Location;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
public class DepartmentRepositoryTest extends AbstractContainerBaseTest {
    private final Logger logger = LoggerFactory.getLogger(DepartmentRepositoryTest.class);


    @Autowired
    private DepartmentRepository departmentRepository;


    @Test
    void shouldCreateDepartment(){
        Location location = Location.builder()
                .lat(22.5)
                .lon(33.4)
                .build();
        Department department =
                Department.builder()
                        .id("100")
                        .departmentName("Chilonzor")
                        .idOfMainBranch(null)
                        .address("Chilonzor")
                        .location(location)
                        .build();
        departmentRepository.save(department);

        Department foundDepartment =
                departmentRepository.findDepartmentById("100");


        assertThat("100").isEqualTo(foundDepartment.getId());
        logger.info("Found department with {} id", foundDepartment.getId());
    }
    @Test
    void checkFromDb(){


    }
}
