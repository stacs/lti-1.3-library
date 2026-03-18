package edu.virginia.its.canvas.lti.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import java.io.InputStream;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import uk.ac.ox.ctl.lti13.lti.Claims;
import uk.ac.ox.ctl.lti13.security.oauth2.client.lti.authentication.OidcAuthenticationToken;

class CanvasAuthenticationTokenTest {

  @Test
  void getCustomValue_returnsValueWhenKeyExists() throws Exception {
    Map<String, Object> attributes = loadCanvasIdTokenJson();
    LinkedTreeMap<String, Object> customMap = new LinkedTreeMap<>();
    customMap.put("username", "testuser");
    customMap.put("course_id", "12345");
    attributes.put(Claims.CUSTOM, customMap);

    OidcAuthenticationToken mockToken = createMockToken(attributes);
    CanvasAuthenticationToken token = new CanvasAuthenticationToken(mockToken);

    assertEquals("testuser", token.getCustomValue("username"));
    assertEquals("12345", token.getCustomValue("course_id"));
  }

  @Test
  void getCustomValue_returnsNullWhenKeyDoesNotExist() throws Exception {
    Map<String, Object> attributes = loadCanvasIdTokenJson();
    LinkedTreeMap<String, Object> customMap = new LinkedTreeMap<>();
    customMap.put("username", "testuser");
    attributes.put(Claims.CUSTOM, customMap);

    OidcAuthenticationToken mockToken = createMockToken(attributes);
    CanvasAuthenticationToken token = new CanvasAuthenticationToken(mockToken);

    assertNull(token.getCustomValue("nonexistent"));
  }

  @Test
  void getCustomValue_returnsNullWhenCustomClaimsNotLinkedTreeMap() throws Exception {
    Map<String, Object> attributes = loadCanvasIdTokenJson();
    attributes.put(Claims.CUSTOM, "not a map");

    OidcAuthenticationToken mockToken = createMockToken(attributes);
    CanvasAuthenticationToken token = new CanvasAuthenticationToken(mockToken);

    assertNull(token.getCustomValue("username"));
  }

  @Test
  void getCustomValue_returnsNullWhenCustomClaimsMissing() throws Exception {
    Map<String, Object> attributes = loadCanvasIdTokenJson();

    OidcAuthenticationToken mockToken = createMockToken(attributes);
    CanvasAuthenticationToken token = new CanvasAuthenticationToken(mockToken);

    assertNull(token.getCustomValue("username"));
  }

  @Test
  void getCustomValue_convertsNonStringValuesToString() throws Exception {
    Map<String, Object> attributes = loadCanvasIdTokenJson();
    LinkedTreeMap<String, Object> customMap = new LinkedTreeMap<>();
    customMap.put("number", 42);
    customMap.put("boolean", true);
    attributes.put(Claims.CUSTOM, customMap);

    OidcAuthenticationToken mockToken = createMockToken(attributes);
    CanvasAuthenticationToken token = new CanvasAuthenticationToken(mockToken);

    assertEquals("42", token.getCustomValue("number"));
    assertEquals("true", token.getCustomValue("boolean"));
  }

  @Test
  void testTokenWithRealCanvasIdToken() throws Exception {
    Map<String, Object> claims = loadCanvasIdTokenJson();

    // Create OIDC token with the claims
    OidcIdToken idToken =
        new OidcIdToken(
            "mock-token-value",
            Instant.ofEpochSecond((Integer) claims.get("iat")),
            Instant.ofEpochSecond((Integer) claims.get("exp")),
            claims);

    OidcUserInfo userInfo = new OidcUserInfo(claims);
    DefaultOidcUser oidcUser =
        new DefaultOidcUser(List.of(new OidcUserAuthority(idToken, userInfo)), idToken, userInfo);

    OidcAuthenticationToken authToken = mock(OidcAuthenticationToken.class);
    when(authToken.getPrincipal()).thenReturn(oidcUser);
    when(authToken.getAuthorities())
        .thenReturn((Collection<GrantedAuthority>) oidcUser.getAuthorities());
    when(authToken.getAuthorizedClientRegistrationId()).thenReturn("canvas");

    // Create Canvas token
    CanvasAuthenticationToken token = new CanvasAuthenticationToken(authToken);

    // Assert values match the JSON - tests the Optional NPE safety implementation
    assertEquals("tj4u@virginia.edu", token.getEmail());
    assertEquals("Thomas Jefferson", token.getName());
    assertEquals("Thomas", token.getGivenName());
    assertEquals("Jefferson", token.getFamilyName());
    assertEquals(
        "https://canvas.instructure.com/images/messages/avatar-50.png", token.getPictureUrl());
    assertEquals("en", token.getLocale());
    assertEquals("account_navigation", token.getPlacementType());
  }

  @Test
  void testTokenWithMissingOptionalFields_noNPE() {
    // Test that missing fields don't cause NPE - validates Optional implementation
    Map<String, Object> minimalClaims = new HashMap<>();
    minimalClaims.put("sub", "test-user-id");
    minimalClaims.put("iss", "https://canvas.instructure.com");
    minimalClaims.put("aud", "client-id");
    minimalClaims.put("exp", 1773421820);
    minimalClaims.put("iat", 1773418220);

    OidcIdToken idToken =
        new OidcIdToken(
            "mock-token-value",
            Instant.ofEpochSecond(1773418220),
            Instant.ofEpochSecond(1773421820),
            minimalClaims);

    OidcUserInfo userInfo = new OidcUserInfo(minimalClaims);
    DefaultOidcUser oidcUser =
        new DefaultOidcUser(List.of(new OidcUserAuthority(idToken, userInfo)), idToken, userInfo);

    OidcAuthenticationToken authToken = mock(OidcAuthenticationToken.class);
    when(authToken.getPrincipal()).thenReturn(oidcUser);
    when(authToken.getAuthorities())
        .thenReturn((Collection<GrantedAuthority>) oidcUser.getAuthorities());
    when(authToken.getAuthorizedClientRegistrationId()).thenReturn("canvas");

    // This should not throw NPE even with missing fields
    CanvasAuthenticationToken token = new CanvasAuthenticationToken(authToken);

    // All optional fields should be null, not cause NPE
    assertNull(token.getEmail());
    assertNull(token.getName());
    assertNull(token.getGivenName());
    assertNull(token.getFamilyName());
    assertNull(token.getPictureUrl());
    assertNull(token.getLocale());
    assertNull(token.getPlacementType());
  }

  private Map<String, Object> loadCanvasIdTokenJson() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    InputStream is = getClass().getResourceAsStream("/canvasIdTokenSample.json");
    return mapper.readValue(is, new TypeReference<>() {});
  }

  private OidcAuthenticationToken createMockToken(Map<String, Object> attributes) {
    OidcAuthenticationToken mockToken = mock(OidcAuthenticationToken.class);
    DefaultOidcUser mockUser = mock(DefaultOidcUser.class);
    when(mockUser.getAttributes()).thenReturn(attributes);
    when(mockToken.getPrincipal()).thenReturn(mockUser);
    when(mockToken.getAuthorities()).thenReturn(java.util.Collections.emptyList());
    when(mockToken.getAuthorizedClientRegistrationId()).thenReturn("canvas");
    when(mockToken.getState()).thenReturn("state");
    return mockToken;
  }
}
