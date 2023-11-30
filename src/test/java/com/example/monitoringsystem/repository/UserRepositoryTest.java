package com.example.monitoringsystem.repository;

import com.example.monitoringsystem.AbstractContainerBaseTest;
import com.example.monitoringsystem.entity.RoleName;
import com.example.monitoringsystem.entity.Userr;
import com.example.monitoringsystem.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
public class UserRepositoryTest extends AbstractContainerBaseTest {
    private final Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    private UserRepository userRepository;

    @Test
    void findById(){

        Userr user = Userr.builder()
                .id("100")//id of user given by super admin
                .departmentId("100")
                .fullName("Sirojiddin Abdumavlonov")
                .password("Siroj12@")
                .roleName(RoleName.USER)
                .build();
        userRepository.save(user);

        Userr foundUser =
                userRepository.findById("100")
                        .orElseThrow(() -> new BadRequestException("Not found!"));

        assertThat(user).isEqualTo(foundUser);
        logger.info("Found user = {}", foundUser);


    }

}
