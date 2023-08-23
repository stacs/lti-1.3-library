package edu.virginia.its.canvas.lti;

import edu.virginia.lts.canvas.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

  @Bean
  public Config getConfig() {
    return Config.builder().title("Test Config Bean").build();
  }
}
