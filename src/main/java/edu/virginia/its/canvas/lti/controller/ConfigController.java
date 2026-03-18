package edu.virginia.its.canvas.lti.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.its.canvas.lti.util.Constants;
import edu.virginia.lts.canvas.Config;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
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

  /**
   * This is a helper method that returns a pre-configured Map of global custom fields for Canvas.
   * This Map hooks into a Config or Placement object (found in the canvas-json-config project) and
   * will allow the user to call any of the methods found in the CanvasAuthenticationToken. Using
   * this Map is not required, the Config/Placement objects can be configured manually with the
   * fields that the app requires, in this instance calling methods from CanvasAuthenticationToken
   * that were not setup in the Config/Placement objects will return null.
   *
   * @return Map of global custom fields
   */
  @Bean
  public Map<String, String> globalCustomFields() {
    Map<String, String> globalCustomFields = new HashMap<>();
    globalCustomFields.put(Constants.CANVAS_USER_ID_CUSTOM_KEY, Constants.CANVAS_USER_ID);
    globalCustomFields.put(Constants.CANVAS_USER_LOGINID_CUSTOM_KEY, Constants.CANVAS_USER_LOGINID);
    globalCustomFields.put(
        Constants.CANVAS_USER_ISROOTACCOUNTADMIN_CUSTOM_KEY,
        Constants.CANVAS_USER_ISROOTACCOUNTADMIN);
    globalCustomFields.put(Constants.CANVAS_ROLES_CUSTOM_KEY, Constants.CANVAS_ROLES);
    globalCustomFields.put(
        Constants.PERSON_ADDRESS_TIMEZONE_CUSTOM_KEY, Constants.PERSON_ADDRESS_TIMEZONE);
    globalCustomFields.put(Constants.USER_STUDENT_VIEW_CUSTOM_KEY, Constants.USER_STUDENT_VIEW);
    globalCustomFields.put(Constants.CONTEXT_TITLE_CUSTOM_KEY, Constants.CONTEXT_TITLE);
    globalCustomFields.put(Constants.POST_MESSAGE_TOKEN_CUSTOM_KEY, Constants.POST_MESSAGE_TOKEN);
    globalCustomFields.put(Constants.CANVAS_API_BASE_URL_CUSTOM_KEY, Constants.CANVAS_API_BASE_URL);
    globalCustomFields.put(Constants.CANVAS_API_DOMAIN_CUSTOM_KEY, Constants.CANVAS_API_DOMAIN);
    globalCustomFields.put(Constants.CANVAS_COURSE_ID_CUSTOM_KEY, Constants.CANVAS_COURSE_ID);
    globalCustomFields.put(Constants.CANVAS_COURSE_NAME_CUSTOM_KEY, Constants.CANVAS_COURSE_NAME);
    globalCustomFields.put(Constants.CANVAS_COURSE_SISID_CUSTOM_KEY, Constants.CANVAS_COURSE_SISID);
    globalCustomFields.put(
        Constants.CANVAS_COURSE_SECTION_IDS_CUSTOM_KEY, Constants.CANVAS_COURSE_SECTION_IDS);
    globalCustomFields.put(
        Constants.CANVAS_COURSE_SECTION_SISIDS_CUSTOM_KEY, Constants.CANVAS_COURSE_SECTION_SISIDS);
    globalCustomFields.put(
        Constants.CANVAS_COURSE_SECTIONRESTRICTED_CUSTOM_KEY,
        Constants.CANVAS_COURSE_SECTIONRESTRICTED);
    globalCustomFields.put(
        Constants.CANVAS_COURSE_GRADINGSCHEME_CUSTOM_KEY, Constants.CANVAS_COURSE_GRADINGSCHEME);
    globalCustomFields.put(Constants.CANVAS_ACCOUNT_ID_CUSTOM_KEY, Constants.CANVAS_ACCOUNT_ID);
    globalCustomFields.put(Constants.CANVAS_ACCOUNT_NAME_CUSTOM_KEY, Constants.CANVAS_ACCOUNT_NAME);
    globalCustomFields.put(
        Constants.CANVAS_ACCOUNT_SISID_CUSTOM_KEY, Constants.CANVAS_ACCOUNT_SISID);
    globalCustomFields.put(
        Constants.CANVAS_ROOTACCOUNT_ID_CUSTOM_KEY, Constants.CANVAS_ROOTACCOUNT_ID);
    globalCustomFields.put(Constants.CANVAS_TERM_ID_CUSTOM_KEY, Constants.CANVAS_TERM_ID);
    globalCustomFields.put(Constants.CANVAS_TERM_NAME_CUSTOM_KEY, Constants.CANVAS_TERM_NAME);
    return globalCustomFields;
  }
}
