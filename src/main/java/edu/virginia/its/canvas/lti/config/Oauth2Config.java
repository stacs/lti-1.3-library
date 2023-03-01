package edu.virginia.its.canvas.lti.config;

import edu.virginia.its.canvas.lti.repos.DBClientRegistrationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class Oauth2Config {

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
    return new DBClientRegistrationRepository();
  }
}
