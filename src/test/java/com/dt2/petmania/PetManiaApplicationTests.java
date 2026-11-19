package com.dt2.petmania;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:petmania_context_test")
class PetManiaApplicationTests {

    @Test
    void contextLoads() {
    }

}
