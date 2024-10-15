package edu.virginia.its.canvas.lti;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ComponentScan("edu.virginia.its.canvas")
@EnableJpaRepositories("edu.virginia.its.canvas.lti.repos")
@EntityScan("edu.virginia.its.canvas.lti.model")
public class LtiConfig {}
