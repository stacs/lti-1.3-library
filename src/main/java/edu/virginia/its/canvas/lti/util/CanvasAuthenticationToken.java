package edu.virginia.its.canvas.lti.util;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import edu.virginia.its.canvas.lti.exception.CanvasTokenException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
  private final Boolean isRootAccountAdmin;
  private final String canvasMembershipRoles;
  private final String timezone;
  private final Boolean isStudentView;
  private final String contextTitle;
  private final String postMessageToken;
  private final String canvasApiBaseUrl;
  private final String canvasApiDomain;
  private final String courseCanvasId;
  private final String courseName;
  private final String courseSisId;
  private final List<String> courseSectionCanvasIds;
  private final List<String> courseSectionSisIds;
  private final Boolean isSectionRestricted;
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
    this.userCanvasId =
        Optional.ofNullable(attributes.get(Constants.CANVAS_USER_ID))
            .map(Object::toString)
            .orElse(null);
    this.computingId =
        Optional.ofNullable(attributes.get(Constants.CANVAS_USER_LOGINID))
            .map(Object::toString)
            .orElse(null);
    this.isRootAccountAdmin =
        Optional.ofNullable(attributes.get(Constants.CANVAS_USER_ISROOTACCOUNTADMIN))
            .map(Object::toString)
            .map(Boolean::valueOf)
            .orElse(null);
    this.canvasMembershipRoles =
        Optional.ofNullable(attributes.get(Constants.CANVAS_ROLES))
            .map(Object::toString)
            .orElse(null);
    this.timezone =
        Optional.ofNullable(attributes.get(Constants.PERSON_ADDRESS_TIMEZONE))
            .map(Object::toString)
            .orElse(null);
    this.isStudentView =
        Optional.ofNullable(attributes.get(Constants.USER_STUDENT_VIEW))
            .map(Object::toString)
            .map(Boolean::valueOf)
            .orElse(null);
    this.contextTitle =
        Optional.ofNullable(attributes.get(Constants.CONTEXT_TITLE))
            .map(Object::toString)
            .orElse(null);
    this.postMessageToken =
        Optional.ofNullable(attributes.get(Constants.POST_MESSAGE_TOKEN))
            .map(Object::toString)
            .orElse(null);
    this.canvasApiBaseUrl =
        Optional.ofNullable(attributes.get(Constants.CANVAS_API_BASE_URL))
            .map(Object::toString)
            .orElse(null);
    this.canvasApiDomain =
        Optional.ofNullable(attributes.get(Constants.CANVAS_API_DOMAIN))
            .map(Object::toString)
            .orElse(null);
    this.courseCanvasId =
        Optional.ofNullable(attributes.get(Constants.CANVAS_COURSE_ID))
            .map(Object::toString)
            .orElse(null);
    this.courseName =
        Optional.ofNullable(attributes.get(Constants.CANVAS_COURSE_NAME))
            .map(Object::toString)
            .orElse(null);
    this.courseSisId =
        Optional.ofNullable(attributes.get(Constants.CANVAS_COURSE_SISID))
            .map(Object::toString)
            .orElse(null);
    this.courseSectionCanvasIds =
        Optional.ofNullable(attributes.get(Constants.CANVAS_COURSE_SECTION_IDS))
            .map(Object::toString)
            .map(s -> Arrays.asList(s.split(",")))
            .orElse(null);
    this.courseSectionSisIds =
        Optional.ofNullable(attributes.get(Constants.CANVAS_COURSE_SECTION_SISIDS))
            .map(Object::toString)
            .map(s -> Arrays.asList(s.split(",")))
            .orElse(null);
    this.isSectionRestricted =
        Optional.ofNullable(attributes.get(Constants.CANVAS_COURSE_SECTIONRESTRICTED))
            .map(Object::toString)
            .map(Boolean::valueOf)
            .orElse(null);
    this.courseGradingScheme =
        Optional.ofNullable(attributes.get(Constants.CANVAS_COURSE_GRADINGSCHEME))
            .map(Object::toString)
            .orElse(null);
    this.accountCanvasId =
        Optional.ofNullable(attributes.get(Constants.CANVAS_ACCOUNT_ID))
            .map(Object::toString)
            .orElse(null);
    this.accountName =
        Optional.ofNullable(attributes.get(Constants.CANVAS_ACCOUNT_NAME))
            .map(Object::toString)
            .orElse(null);
    this.accountSisId =
        Optional.ofNullable(attributes.get(Constants.CANVAS_ACCOUNT_SISID))
            .map(Object::toString)
            .orElse(null);
    this.rootAccountCanvasId =
        Optional.ofNullable(attributes.get(Constants.CANVAS_ROOTACCOUNT_ID))
            .map(Object::toString)
            .orElse(null);
    this.termCanvasId =
        Optional.ofNullable(attributes.get(Constants.CANVAS_TERM_ID))
            .map(Object::toString)
            .orElse(null);
    this.termName =
        Optional.ofNullable(attributes.get(Constants.CANVAS_TERM_NAME))
            .map(Object::toString)
            .orElse(null);
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

  public static CanvasAuthenticationToken getToken() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth instanceof OidcAuthenticationToken token) {
      return new CanvasAuthenticationToken(token);
    } else {
      throw new CanvasTokenException("Could not find OidcAuthenticationToken");
    }
  }
}
