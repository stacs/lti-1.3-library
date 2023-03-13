package edu.virginia.its.canvas.lti.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "ltitool")
public class LtiToolConfigProperties {
  private String baseUrl;
  private List<String> allowedUserEmails;
}
