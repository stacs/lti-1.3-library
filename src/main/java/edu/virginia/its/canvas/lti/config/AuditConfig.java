package edu.virginia.its.canvas.lti.config;

import edu.virginia.its.canvas.lti.exception.CanvasTokenException;
import edu.virginia.its.canvas.lti.util.CanvasAuthenticationToken;
import edu.virginia.its.canvas.lti.util.Constants;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider", auditorAwareRef = "auditorProvider")
public class AuditConfig {

  @Bean
  public DateTimeProvider dateTimeProvider() {
    return () -> Optional.of(OffsetDateTime.now());
  }

  @Bean
  public AuditorAware<String> auditorProvider() {
    return new AuditorAwareImpl();
  }

  public static class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
      try {
        String computingId =
            CanvasAuthenticationToken.getToken().getCustomValue(Constants.USERNAME_CUSTOM_KEY);
        if (computingId != null) {
          return Optional.of(computingId);
        } else {
          return Optional.ofNullable(CanvasAuthenticationToken.getToken().getEmail());
        }
      } catch (CanvasTokenException ex) {
        return Optional.empty();
      }
    }
  }
}
