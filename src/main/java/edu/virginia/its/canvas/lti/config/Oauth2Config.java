package edu.virginia.its.canvas.lti.config;

import edu.virginia.its.canvas.lti.repos.DBClientRegistrationRepository;
import edu.virginia.its.canvas.lti.repos.LtiRegistrationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class Oauth2Config {

  private final LtiRegistrationRepository repo;
  private final String baseUrl;
  private final String contextPath;
  private final String redirectPath;

  public Oauth2Config(
      LtiRegistrationRepository repo,
      @Value("${ltitool.baseUrl}") String baseUrl,
      @Value("${server.servlet.context-path:}") String contextPath,
      @Value("${ltitool.oauth2.redirectPath:/lti/login}") String redirectPath) {
    this.repo = repo;
    this.baseUrl = baseUrl;
    this.contextPath = contextPath;
    this.redirectPath = redirectPath;
  }

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
    return new DBClientRegistrationRepository(baseUrl, contextPath, redirectPath, repo);
  }
}
