package org.pingle.pingleserver;

import org.junit.jupiter.api.extension.ExtendWith;
import org.pingle.pingleserver.util.DatabaseCleanerExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(DatabaseCleanerExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@ActiveProfiles("test")
public abstract class ServiceSliceTest {
}

