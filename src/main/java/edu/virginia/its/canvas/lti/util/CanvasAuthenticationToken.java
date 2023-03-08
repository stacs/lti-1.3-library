package edu.virginia.its.canvas.lti.util;

import java.util.Map;

import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.Getter;
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
  }

  public String getCustomValue(String customKey) {
    Object customAttributes = attributes.get(Claims.CUSTOM);
    if (customAttributes instanceof JSONObject customJson) {
      return customJson.getAsString(customKey);
    }
    return null;
  }
}