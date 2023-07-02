package sample.cafekiosk.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {
    // protected 로 mock bean 처리하여 테스트 환경을 통합할 수 있음.
}
