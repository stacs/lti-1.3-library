package edu.virginia.its.canvas.lti.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.lts.canvas.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ConfigController {

  private final ObjectMapper mapper;
  private final Config config;

  public ConfigController(Config config) {
    this.config = config;
    this.mapper = new ObjectMapper();
    this.mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    log.info("OIDC Initiation URL: {}", config.getOidcInitiationUrl());
    log.info("Target Link URI: {}", config.getTargetLinkUri());
    log.info("Public JWK URL: {}", config.getPublicJwkUrl());
  }

  @GetMapping(value = "/config.json", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getConfigJson() {
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
    } catch (JsonProcessingException e) {
      log.error("Error while parsing Config object", e);
      return "Error with configuration";
    }
  }
}
