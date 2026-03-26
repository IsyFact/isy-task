package de.bund.bva.isyfact.task;

import de.bund.bva.isyfact.task.test.TestTaskRunAssertion;
import de.bund.bva.isyfact.task.test.config.TestConfig;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("security-test")
@SpringBootTest(
        classes = {
                TestConfig.class,
                TestTaskAuthenticationTasks.class
        },
        properties = {
                "isy.task.authentication.enabled=true",
                "spring.task.scheduling.pool.size=2",
        })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TestTaskAuthentication extends AbstractOidcProviderTest {

    private static final String CC_CLIENT_ID = "cc-testclient-id";
    private static final String CC_CLIENT_SECRET = "cc-testclient-secret";

    private static final String CC_INSUFFICIENT_CLIENT_ID = "cc-insufficient-testclient-id";
    private static final String CC_INSUFFICIENT_CLIENT_SECRET = "cc-insufficient-testclient-secret";

    @Autowired
    private MeterRegistry registry;

    @BeforeEach
    public void setup() {
        embeddedOidcProvider.removeAllClients();
        embeddedOidcProvider.removeAllUsers();
    }

    @Test
    void testTaskSecured() throws Exception {
        embeddedOidcProvider.addClient(
                CC_CLIENT_ID,
                CC_CLIENT_SECRET,
                Collections.singleton("Rolle1")
        );
        String className = TestTaskAuthenticationTasks.class.getSimpleName();
        String annotatedMethodName = "scheduledTaskSecured";

        SECONDS.sleep(5);

        TestTaskRunAssertion.assertTaskSuccess(className, annotatedMethodName, registry,
                AuthenticationCredentialsNotFoundException.class.getSimpleName());
    }

    @Test
    void testTaskSecuredInsufficientRights() throws Exception {
        embeddedOidcProvider.addClient(
                CC_INSUFFICIENT_CLIENT_ID,
                CC_INSUFFICIENT_CLIENT_SECRET,
                Collections.singleton("Rolle2")
        );
        String className = TestTaskAuthenticationTasks.class.getSimpleName();
        String annotatedMethodName = "scheduledTaskSecuredInsufficientRights";

        SECONDS.sleep(5);

        TestTaskRunAssertion.assertTaskFailure(className, annotatedMethodName, registry,
                AuthorizationDeniedException.class.getSimpleName());
    }

    @Test
    void testTaskSecuredMissingRoles() throws Exception {
        embeddedOidcProvider.addClient(
                CC_CLIENT_ID,
                CC_CLIENT_SECRET,
                Collections.emptySet()
        );
        String className = TestTaskAuthenticationTasks.class.getSimpleName();
        String annotatedMethodName = "scheduledTaskSecured";

        SECONDS.sleep(5);

        TestTaskRunAssertion.assertTaskFailure(
            className, annotatedMethodName, registry,
            AuthorizationDeniedException.class.getSimpleName()
        );
    }

    @Test
    void testTaskSecuredNoAuthentication() throws Exception {
        SECONDS.sleep(5);

        assertThat(TestTaskAuthenticationTasks.wasCalled.get()).isFalse();
    }

}
