package edu.virginia.its.canvas.lti.service;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * This class can be used inside @PreAuthorize to allow authorization only to specific users. An
 * example use case is when adding a tool to account_navigation for our root account, that also adds
 * the tool to all sub-accounts as well. We don't want sub-account admins to access the tool, but
 * Canvas sends the Admin role regardless if the admin is a root admin or sub-account admin, so we
 * check against the user email whitelist we create to make sure they are allowed access.
 */
@Slf4j
@Service
public class UserWhitelistAuthorizationService {

  @Value("${ltitool.allowedUserEmails:}")
  private List<String> allowedUserEmails = new ArrayList<>();

  @Value("${ltitool.allowedUserEmails:}")
  private List<String> c;

  @Value("${ltitool.allowedUserEmails:}")
  private String d;

  public boolean isUserAllowed(Object principalObject) {
    log.info("isUserAllowed()");
    log.info("allowedUserEmails: {}", allowedUserEmails);
    log.info("c: {}", c);
    log.info("d: {}", d);
    log.info("principalObject: {}", principalObject);
    if (principalObject instanceof OAuth2User principal) {
      log.info("principal.getAttributes(): {}", principal.getAttributes());
      String email = principal.getAttributes().get("email").toString();
      log.info(
          "email != null && allowedUserEmails.contains(email): {}",
          (email != null && allowedUserEmails.contains(email)));
      return email != null && allowedUserEmails.contains(email);
    }
    return false;
  }
}
