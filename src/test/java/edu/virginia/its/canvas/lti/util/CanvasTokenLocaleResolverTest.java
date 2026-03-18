package edu.virginia.its.canvas.lti.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import uk.ac.ox.ctl.lti13.security.oauth2.client.lti.authentication.OidcAuthenticationToken;

class CanvasTokenLocaleResolverTest {

  private CanvasTokenLocaleResolver resolver;
  private HttpServletRequest request;
  private HttpServletResponse response;
  private SecurityContext securityContext;

  @BeforeEach
  void setUp() {
    resolver = new CanvasTokenLocaleResolver();
    resolver.setDefaultLocale(Locale.ENGLISH);
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    securityContext = mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void resolveLocale_returnsLocaleFromCanvasToken() {
    Map<String, Object> attributes = createAttributesWithLocale("es");
    OidcAuthenticationToken token = createMockToken(attributes);
    when(securityContext.getAuthentication()).thenReturn(token);

    Locale result = resolver.resolveLocale(request);

    assertEquals(Locale.forLanguageTag("es"), result);
  }

  @Test
  void resolveLocale_returnsDefaultLocaleWhenTokenNotFound() {
    when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));

    Locale result = resolver.resolveLocale(request);

    assertEquals(Locale.ENGLISH, result);
  }

  @Test
  void resolveLocale_returnsDefaultLocaleWhenAuthenticationIsNull() {
    when(securityContext.getAuthentication()).thenReturn(null);

    Locale result = resolver.resolveLocale(request);

    assertEquals(Locale.ENGLISH, result);
  }

  @Test
  void resolveLocale_handlesVariousLocaleStrings() {
    testLocaleString("en-US", Locale.forLanguageTag("en-US"));
    testLocaleString("fr-FR", Locale.forLanguageTag("fr-FR"));
    testLocaleString("zh-CN", Locale.forLanguageTag("zh-CN"));
    testLocaleString("pt-BR", Locale.forLanguageTag("pt-BR"));
  }

  @Test
  void setLocale_throwsUnsupportedOperationException() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> resolver.setLocale(request, response, Locale.FRENCH));
  }

  private void testLocaleString(String localeString, Locale expectedLocale) {
    Map<String, Object> attributes = createAttributesWithLocale(localeString);
    OidcAuthenticationToken token = createMockToken(attributes);
    when(securityContext.getAuthentication()).thenReturn(token);

    Locale result = resolver.resolveLocale(request);

    assertEquals(expectedLocale, result);
  }

  private Map<String, Object> createAttributesWithLocale(String locale) {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("https://www.instructure.com/placement", "course_navigation");
    attributes.put("email", "test@example.com");
    attributes.put("name", "Test User");
    attributes.put("given_name", "Test");
    attributes.put("family_name", "User");
    attributes.put("picture", "https://example.com/picture.jpg");
    attributes.put("locale", locale);
    return attributes;
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
