package edu.virginia.its.canvas.lti.service;

import java.util.Arrays;
import java.util.List;
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
@Service
public class UserWhitelistAuthorizationService {

  @Value("${ltitool.allowedUserEmails:}")
  private String[] allowedUserEmails = new String[0];

  public boolean isUserAllowed(Object principalObject) {
    if (principalObject instanceof OAuth2User principal) {
      List<String> allowedUserEmailsList = Arrays.asList(allowedUserEmails);
      String email = principal.getAttributes().get("email").toString();
      return email != null && allowedUserEmailsList.contains(email);
    }
    return false;
  }
}
