package edu.virginia.its.canvas.lti.util;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import edu.virginia.its.canvas.lti.exception.CanvasTokenException;
import java.util.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.ac.ox.ctl.lti13.lti.Claims;
import uk.ac.ox.ctl.lti13.security.oauth2.client.lti.authentication.OidcAuthenticationToken;

/**
 * An extension of OidcAuthenticationToken, this class lets us access any Canvas specific values we
 * may want within a Tool.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class CanvasAuthenticationToken extends OidcAuthenticationToken {

  private final Map<String, Object> attributes;
  private final String placementType;
  private final String email;
  private final String name;
  private final String givenName;
  private final String familyName;
  private final String pictureUrl;
  private final String locale;
  private final String userCanvasId;
  private final String computingId;
  private final boolean isRootAccountAdmin;
  private final String canvasMembershipRoles;
  private final String timezone;
  private final boolean isStudentView;
  private final String contextTitle;
  private final String postMessageToken;
  private final String canvasApiBaseUrl;
  private final String canvasApiDomain;
  private final String courseCanvasId;
  private final String courseName;
  private final String courseSisId;
  private final List<String> courseSectionCanvasIds;
  private final List<String> courseSectionSisIds;
  private final boolean isSectionRestricted;
  private final String courseGradingScheme;
  private final String accountCanvasId;
  private final String accountName;
  private final String accountSisId;
  private final String rootAccountCanvasId;
  private final String termCanvasId;
  private final String termName;

  public CanvasAuthenticationToken(OidcAuthenticationToken token) {
    super(
        token.getPrincipal(),
        token.getAuthorities(),
        token.getAuthorizedClientRegistrationId(),
        token.getState());
    this.attributes = getPrincipal().getAttributes();
    this.placementType =
        Optional.ofNullable(attributes.get(Constants.PLACEMENT_ATTRIBUTE))
            .map(Object::toString)
            .orElse(null);
    this.email =
        Optional.ofNullable(attributes.get(Constants.EMAIL_ATTRIBUTE))
            .map(Object::toString)
            .orElse(null);
    this.name =
        Optional.ofNullable(attributes.get(Constants.NAME_ATTRIBUTE))
            .map(Object::toString)
            .orElse(null);
    this.givenName =
        Optional.ofNullable(attributes.get(Constants.GIVEN_NAME_ATTRIBUTE))
            .map(Object::toString)
            .orElse(null);
    this.familyName =
        Optional.ofNullable(attributes.get(Constants.FAMILY_NAME_ATTRIBUTE))
            .map(Object::toString)
            .orElse(null);
    this.pictureUrl =
        Optional.ofNullable(attributes.get(Constants.PICTURE_ATTRIBUTE))
            .map(Object::toString)
            .orElse(null);
    this.locale =
        Optional.ofNullable(attributes.get(Constants.LOCALE_ATTRIBUTE))
            .map(Object::toString)
            .orElse(null);
    this.userCanvasId = getCustomValue(Constants.CANVAS_USER_ID_CUSTOM_KEY);
    this.computingId = getCustomValue(Constants.CANVAS_USER_LOGINID_CUSTOM_KEY);
    this.isRootAccountAdmin =
        getCustomBooleanValue(Constants.CANVAS_USER_ISROOTACCOUNTADMIN_CUSTOM_KEY);
    this.canvasMembershipRoles = getCustomValue(Constants.CANVAS_ROLES_CUSTOM_KEY);
    this.timezone = getCustomValue(Constants.PERSON_ADDRESS_TIMEZONE_CUSTOM_KEY);
    this.isStudentView = getCustomBooleanValue(Constants.USER_STUDENT_VIEW_CUSTOM_KEY);
    this.contextTitle = getCustomValue(Constants.CONTEXT_TITLE_CUSTOM_KEY);
    this.postMessageToken = getCustomValue(Constants.POST_MESSAGE_TOKEN_CUSTOM_KEY);
    this.canvasApiBaseUrl = getCustomValue(Constants.CANVAS_API_BASE_URL_CUSTOM_KEY);
    this.canvasApiDomain = getCustomValue(Constants.CANVAS_API_DOMAIN_CUSTOM_KEY);
    this.courseCanvasId = getCustomValue(Constants.CANVAS_COURSE_ID_CUSTOM_KEY);
    this.courseName = getCustomValue(Constants.CANVAS_COURSE_NAME_CUSTOM_KEY);
    this.courseSisId = getCustomValue(Constants.CANVAS_COURSE_SISID_CUSTOM_KEY);
    this.courseSectionCanvasIds =
        getCustomListValue(Constants.CANVAS_COURSE_SECTION_IDS_CUSTOM_KEY);
    this.courseSectionSisIds =
        getCustomListValue(Constants.CANVAS_COURSE_SECTION_SISIDS_CUSTOM_KEY);
    this.isSectionRestricted =
        getCustomBooleanValue(Constants.CANVAS_COURSE_SECTIONRESTRICTED_CUSTOM_KEY);
    this.courseGradingScheme = getCustomValue(Constants.CANVAS_COURSE_GRADINGSCHEME_CUSTOM_KEY);
    this.accountCanvasId = getCustomValue(Constants.CANVAS_ACCOUNT_ID_CUSTOM_KEY);
    this.accountName = getCustomValue(Constants.CANVAS_ACCOUNT_NAME_CUSTOM_KEY);
    this.accountSisId = getCustomValue(Constants.CANVAS_ACCOUNT_SISID_CUSTOM_KEY);
    this.rootAccountCanvasId = getCustomValue(Constants.CANVAS_ROOTACCOUNT_ID_CUSTOM_KEY);
    this.termCanvasId = getCustomValue(Constants.CANVAS_TERM_ID_CUSTOM_KEY);
    this.termName = getCustomValue(Constants.CANVAS_TERM_NAME_CUSTOM_KEY);
  }

  public String getCustomValue(String customKey) {
    Object customAttributes = attributes.get(Claims.CUSTOM);
    if (customAttributes instanceof LinkedTreeMap customMap) {
      // LTI 1.3 spec section 5.4.6 says all Custom values need to be a String
      Object obj = customMap.get(customKey);
      if (obj != null) {
        return String.valueOf(obj);
      }
    }
    return null;
  }

  public boolean getCustomBooleanValue(String customKey) {
    String value = getCustomValue(customKey);
    return Boolean.parseBoolean(value);
  }

  public List<String> getCustomListValue(String customKey) {
    String value = getCustomValue(customKey);
    if (value == null) {
      return new ArrayList<>();
    }
    return Arrays.asList(value.split(","));
  }

  public static CanvasAuthenticationToken getToken() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth instanceof OidcAuthenticationToken token) {
      return new CanvasAuthenticationToken(token);
    } else {
      throw new CanvasTokenException("Could not find OidcAuthenticationToken");
    }
  }
}
