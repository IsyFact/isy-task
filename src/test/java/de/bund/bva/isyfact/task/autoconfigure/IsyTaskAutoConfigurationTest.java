package de.bund.bva.isyfact.task.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import de.bund.bva.isyfact.task.konfiguration.HostHandler;
import de.bund.bva.isyfact.task.konfiguration.impl.LocalHostHandlerImpl;
import de.bund.bva.isyfact.task.monitoring.IsyTaskAspect;
import de.bund.bva.isyfact.task.security.AuthenticatorFactory;
import de.bund.bva.isyfact.task.security.impl.NoOpAuthenticatorFactory;
import de.bund.bva.isyfact.task.test.config.TestConfig;

@SpringBootTest(classes = TestConfig.class,
    properties = { "isy.task.authentication.enabled=false" })
public class IsyTaskAutoConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private HostHandler hostHandler;

    @Autowired
    private AuthenticatorFactory authenticatorFactory;

    @Autowired
    private IsyTaskAspect isyTaskAspect;

    @Test
    void testAllBeansAreLoadedCorrectlyWithDefaultConfig() {
        assertThat(applicationContext.containsBean("isyTaskConfigurationProperties")).isTrue();
        assertThat(applicationContext.containsBean("localHostHandler")).isTrue();
        assertThat(applicationContext.containsBean("isyTaskAspect")).isTrue();
        assertThat(applicationContext.containsBean("timedAspect")).isTrue();
        assertThat(applicationContext.containsBean("authenticatorFactoryNoOp")).isTrue();
        assertThat(applicationContext.containsBean("isyTaskMessageSource")).isTrue();

        assertThat(hostHandler).isInstanceOf(LocalHostHandlerImpl.class);
        assertThat(authenticatorFactory).isInstanceOf(NoOpAuthenticatorFactory.class);

        assertThat(isyTaskAspect).isNotNull();
    }

    @Test
    void testNoSecurityAuthenticatorLoadedWhenDisabled() {
        assertThat(applicationContext.containsBean("authenticatorFactoryNoOp")).isTrue();
        assertThat(applicationContext.containsBean("authenticatorFactoryIsySecurity")).isFalse();
    }
}