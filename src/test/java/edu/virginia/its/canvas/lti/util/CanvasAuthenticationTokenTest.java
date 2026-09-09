package edu.virginia.its.canvas.lti.util;

import static org.junit.jupiter.api.Assertions.*;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import uk.ac.ox.ctl.lti13.lti.Claims;
import uk.ac.ox.ctl.lti13.security.oauth2.client.lti.authentication.OidcAuthenticationToken;

/**
 * Tests for CanvasAuthenticationToken, focused on reading LTI custom claims.
 *
 * <p>The concrete Map implementation behind the custom claim is not part of any contract: it
 * depends on which parser handled the JWT, and it changes again when the token is serialized. So
 * getCustomValue must work for any Map - these tests pin that.
 */
class CanvasAuthenticationTokenTest {

  private static final String COURSE_ID = "123";
  private static final String USERNAME = "user";

  @Test
  void getCustomValueReadsClaims() {
    CanvasAuthenticationToken token = buildToken(customClaims(new LinkedTreeMap<>()));

    assertEquals(COURSE_ID, token.getCustomValue(Constants.COURSE_ID_CUSTOM_KEY));
    assertEquals(USERNAME, token.getCustomValue(Constants.USERNAME_CUSTOM_KEY));
  }

  /**
   * Spring Session JDBC stores each session attribute as bytes from these two converters, so the
   * token makes a full Java serialization round trip on every request. Shaded-Gson's LinkedTreeMap
   * declares writeReplace(), so the claim comes back as a LinkedHashMap: the data is intact but the
   * type is not, and getCustomValue must not depend on it.
   */
  @Test
  void getCustomValueSurvivesSessionSerialization() {
    CanvasAuthenticationToken token = buildToken(customClaims(new LinkedTreeMap<>()));
    assertEquals(
        COURSE_ID,
        token.getCustomValue(Constants.COURSE_ID_CUSTOM_KEY),
        "precondition: claims readable before serialization");

    CanvasAuthenticationToken restored = throughSessionStore(token);

    // Asserted so a failure below reads as a type change rather than as missing data.
    assertInstanceOf(
        LinkedHashMap.class,
        restored.getAttributes().get(Claims.CUSTOM),
        "serialization is expected to downgrade the claim to a LinkedHashMap");

    assertEquals(COURSE_ID, restored.getCustomValue(Constants.COURSE_ID_CUSTOM_KEY));
    assertEquals(USERNAME, restored.getCustomValue(Constants.USERNAME_CUSTOM_KEY));
  }

  @Test
  void getCustomValueAcceptsAnyMapImplementation() {
    assertEquals(
        COURSE_ID,
        buildToken(customClaims(new LinkedTreeMap<>()))
            .getCustomValue(Constants.COURSE_ID_CUSTOM_KEY));
    assertEquals(
        COURSE_ID,
        buildToken(customClaims(new HashMap<>())).getCustomValue(Constants.COURSE_ID_CUSTOM_KEY));
    assertEquals(
        COURSE_ID,
        buildToken(customClaims(new LinkedHashMap<>()))
            .getCustomValue(Constants.COURSE_ID_CUSTOM_KEY));
  }

  @Test
  void getCustomValueReturnsNullWhenClaimIsAbsent() {
    CanvasAuthenticationToken token = buildToken(customClaims(new LinkedTreeMap<>()));

    assertNull(token.getCustomValue("not_a_key"));
  }

  @Test
  void getCustomValueReturnsNullWhenCustomClaimIsMissingEntirely() {
    CanvasAuthenticationToken token = buildToken(null);

    assertNull(token.getCustomValue(Constants.COURSE_ID_CUSTOM_KEY));
  }

  /** Canvas does not always send custom values as Strings, despite LTI 1.3 section 5.4.6. */
  @Test
  void getCustomValueConvertsNonStringValues() {
    Map<String, Object> claims = new LinkedTreeMap<>();
    claims.put(Constants.COURSE_ID_CUSTOM_KEY, 123);
    CanvasAuthenticationToken token = buildToken(claims);

    assertEquals("123", token.getCustomValue(Constants.COURSE_ID_CUSTOM_KEY));
  }

  /** Round trips the token the way Spring Session JDBC stores and reloads it. */
  private CanvasAuthenticationToken throughSessionStore(CanvasAuthenticationToken token) {
    SecurityContext context = new SecurityContextImpl(token);
    byte[] bytes = (byte[]) new SerializingConverter().convert(context);
    SecurityContext restored = (SecurityContext) new DeserializingConverter().convert(bytes);
    return (CanvasAuthenticationToken) restored.getAuthentication();
  }

  private Map<String, Object> customClaims(Map<String, Object> target) {
    target.put(Constants.COURSE_ID_CUSTOM_KEY, COURSE_ID);
    target.put(Constants.USERNAME_CUSTOM_KEY, USERNAME);
    return target;
  }

  /** A null customClaims omits the claim, as a launch with no custom fields configured would. */
  private CanvasAuthenticationToken buildToken(Map<String, Object> customClaims) {
    String nameAttributeKey = "sub";
    Map<String, Object> attributes = new HashMap<>();
    attributes.put(nameAttributeKey, USERNAME);
    // The constructor dereferences every one of these, so they all have to be present.
    attributes.put("https://www.instructure.com/placement", "myPlacement");
    attributes.put("email", "myEmail");
    attributes.put("name", "myName");
    attributes.put("given_name", "myGivenName");
    attributes.put("family_name", "myFamilyName");
    attributes.put("picture", "myPicture");
    attributes.put("locale", "en");
    if (customClaims != null) {
      attributes.put(Claims.CUSTOM, customClaims);
    }

    OAuth2User principal =
        new DefaultOAuth2User(
            AuthorityUtils.createAuthorityList("ROLE_USER"), attributes, nameAttributeKey);
    OidcAuthenticationToken oidcToken =
        new OidcAuthenticationToken(
            principal,
            AuthorityUtils.createAuthorityList("ROLE_USER"),
            "authorizedClientRegistrationId",
            "state");
    return new CanvasAuthenticationToken(oidcToken);
  }
}
