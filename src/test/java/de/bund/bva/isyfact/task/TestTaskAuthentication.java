package de.bund.bva.isyfact.task;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.test.context.ActiveProfiles;

import de.bund.bva.isyfact.task.test.TestTaskRunAssertion;
import de.bund.bva.isyfact.task.test.config.TestConfig;

import io.micrometer.core.instrument.MeterRegistry;

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
class TestTaskAuthentication extends AbstractOidcProviderTest {

    @Autowired
    private MeterRegistry registry;

    @Value("${spring.security.oauth2.client.registration.cc-testclient.client-id}")
    private String ccClientId;

    @Value("${spring.security.oauth2.client.registration.cc-testclient.client-secret}")
    private String ccClientSecret;

    @Value("${spring.security.oauth2.client.registration.cc-insufficient-testclient.client-id}")
    private String ccInsufficientClientId;

    @Value("${spring.security.oauth2.client.registration.cc-insufficient-testclient.client-secret}")
    private String ccInsufficientClientSecret;

    @BeforeEach
    public void setup() {
        embeddedOidcProvider.removeAllClients();
        embeddedOidcProvider.removeAllUsers();
        embeddedOidcProvider.addClient(
                ccClientId,
                ccClientSecret,
                Collections.singleton("Rolle1")
        );
        embeddedOidcProvider.addClient(
                ccInsufficientClientId,
                ccInsufficientClientSecret,
                Collections.singleton("Rolle2")
        );
    }

    @Test
    void testTaskSecured() throws Exception {
        String className = TestTaskAuthenticationTasks.class.getSimpleName();
        String annotatedMethodName = "scheduledTaskSecured";

        SECONDS.sleep(5);

        TestTaskRunAssertion.assertTaskSuccess(className, annotatedMethodName, registry,
                AuthenticationCredentialsNotFoundException.class.getSimpleName());
    }

    @Test
    void testTaskSecuredInsufficientRights() throws Exception {
        String className = TestTaskAuthenticationTasks.class.getSimpleName();
        String annotatedMethodName = "scheduledTaskSecuredInsufficientRights";

        SECONDS.sleep(5);

        TestTaskRunAssertion.assertTaskFailure(className, annotatedMethodName, registry,
                AuthorizationDeniedException.class.getSimpleName());
    }

    @Test
    void testTaskSecuredNoAuthentication() throws Exception {
        embeddedOidcProvider.removeAllClients();
        embeddedOidcProvider.removeAllUsers();

        SECONDS.sleep(5);

        assertThat(TestTaskAuthenticationTasks.wasCalled.get()).isFalse();
    }

    @Test
    void testTaskSecuredMissingRoles() throws Exception {
        embeddedOidcProvider.removeAllClients();
        embeddedOidcProvider.removeAllUsers();
        embeddedOidcProvider.addClient(
            ccClientId,
            ccClientSecret,
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
}
