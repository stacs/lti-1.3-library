package edu.virginia.its.canvas.lti.util;

import com.nimbusds.jose.shaded.json.JSONObject;
import edu.virginia.its.canvas.lti.exception.CanvasTokenException;
import java.util.Map;
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
public class CanvasAuthenticationToken extends OidcAuthenticationToken {

  private final Map<String, Object> attributes;
  private final String placementType;
  private final String email;
  private final String name;
  private final String givenName;
  private final String familyName;
  private final String pictureUrl;
  private final String locale;

  public CanvasAuthenticationToken(OidcAuthenticationToken token) {
    super(
        token.getPrincipal(),
        token.getAuthorities(),
        token.getAuthorizedClientRegistrationId(),
        token.getState());
    Map<String, Object> attributes = getPrincipal().getAttributes();
    this.attributes = attributes;
    this.placementType = attributes.get("https://www.instructure.com/placement").toString();
    this.email = attributes.get("email").toString();
    this.name = attributes.get("name").toString();
    this.givenName = attributes.get("given_name").toString();
    this.familyName = attributes.get("family_name").toString();
    this.pictureUrl = attributes.get("picture").toString();
    this.locale = attributes.get("locale").toString();
  }

  public String getCustomValue(String customKey) {
    Object customAttributes = attributes.get(Claims.CUSTOM);
    if (customAttributes instanceof JSONObject customJson) {
      return customJson.getAsString(customKey);
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
