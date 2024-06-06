//package com.example.monitoringsystem.repository;
//
//import com.example.monitoringsystem.AbstractContainerBaseTest;
//import com.example.monitoringsystem.entity.Department;
//import com.example.monitoringsystem.entity.Location;
//import com.example.monitoringsystem.exception.BadRequestException;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//
//@SpringBootTest
//public class DepartmentRepositoryTest extends AbstractContainerBaseTest {
//    private final Logger logger = LoggerFactory.getLogger(DepartmentRepositoryTest.class);
//
//
//    @Autowired
//    private DepartmentRepository autowiredDepartment;
//    @BeforeAll
//    static void setUp(){
//
//
//    }
//
//    @Test
//    void shouldCreateDepartment(){
//
//
//        Location location = Location.builder()
//                .lat(22.5)
//                .lon(33.4)
//                .build();
//        Department department =
//                Department.builder()
//                        .id("100")
//                        .departmentName("Chilonzor")
//                        .idOfMainBranch(null)
//                        .address("Chilonzor")
//                        .location(location)
//                        .build();
//        autowiredDepartment.save(department);
//
//        Department foundDepartment =
//                autowiredDepartment.findDepartmentById("100");
//
//
//        assertThat("100").isEqualTo(foundDepartment.getId());
//        logger.info("Found department with {} id", foundDepartment.getId());
//    }
//    @Test
//    void checkByAddress(){
//        Department department =
//                autowiredDepartment.findByDepartmentName("Chilonzor").
//                        orElseThrow(() -> new BadRequestException("Error"));
//        assertThat("Chilonzor").isEqualTo(department.getDepartmentName());
//    }
//    @Test
//    void checkByMainBranchId(){
//
//        Location location2 = Location.builder()
//                .lat(22.5)
//                .lon(33.4)
//                .build();
//        Department department2 =
//                Department.builder()
//                        .id("101")
//                        .departmentName("Yunusobod")
//                        .idOfMainBranch("100")
//                        .address("Yunusobod")
//                        .location(location2)
//                        .build();
//        autowiredDepartment.save(department2);
//
//        Department department =
//                autowiredDepartment.findDepartmentById("100");
//
//        logger.info("Department found at 100 {}", department);
//
//        Department department1 =
//                autowiredDepartment.findDepartmentById("101");
//
//        logger.info("Department found at 101 {}", department1);
//
//        assertThat(department.getId()).isEqualTo(department1.getIdOfMainBranch());
////        boolean departmentExists =
////                autowiredDepartment.existsByIdOfMainBranchAndId
////                        (department.getIdOfMainBranch(), department1.getId());
////        assertThat(departmentExists).isTrue();
//    }
//    @Test
//    void existsById(){
//
//        Location location2 = Location.builder()
//                .lat(122.5)
//                .lon(333.4)
//                .build();
//        Department department2 =
//                Department.builder()
//                        .id("102")
//                        .departmentName("Yashnobod")
//                        .idOfMainBranch("100")
//                        .address("Yashnobod")
//                        .location(location2)
//                        .build();
//        autowiredDepartment.save(department2);
//
//        logger.info("Checking existence by id 102");
//        logger.info("Checking existence by id 100");
//
//        boolean existence102 =
//                autowiredDepartment.existsById("102");
//        boolean existence100 =
//                autowiredDepartment.existsById("100");
//
//        assertThat(true).isEqualTo(existence102);
//
//        assertThat(true).isEqualTo(existence100);
//
//    }
//
//    @Test
//    void deleteAllValues(){
//        autowiredDepartment.deleteAll();
//    }
//
//}
