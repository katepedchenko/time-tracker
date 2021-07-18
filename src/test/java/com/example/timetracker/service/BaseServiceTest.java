package com.example.timetracker.service;

import com.example.timetracker.util.TestObjectFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Sql(statements = {
        "delete from activity",
        "delete from project",
        "delete from app_user"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class BaseServiceTest {

    @Autowired
    protected TestObjectFactory testObjectFactory;
}
