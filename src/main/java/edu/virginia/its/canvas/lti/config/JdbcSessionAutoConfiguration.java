package edu.virginia.its.canvas.lti.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.PropertySource;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;

@AutoConfiguration
@ConditionalOnClass(JdbcIndexedSessionRepository.class)
@PropertySource("classpath:jdbc-session-defaults.properties")
public class JdbcSessionAutoConfiguration {}
