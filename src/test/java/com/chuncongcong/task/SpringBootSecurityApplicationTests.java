package com.chuncongcong.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootSecurityApplicationTests {


    @Test
    public void test() {

        System.out.println(LocalDate.now());
    }

}
