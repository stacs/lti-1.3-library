package edu.virginia.its.canvas.lti.repos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.virginia.its.canvas.lti.model.LtiProvider;
import edu.virginia.its.canvas.lti.model.LtiRegistration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class DBClientRegistrationRepositoryTest {

  @Autowired private DBClientRegistrationRepository repository;

  @MockitoBean private LtiRegistrationRepository ltiRegistrationRepo;

  @Value("${ltitool.baseUrl}")
  private String baseUrl;

  @Value("${server.servlet.context-path:}")
  private String contextPath;

  @Value("${ltitool.oauth2.redirectPath:/lti/login}")
  private String redirectPath;

  private static final String REGISTRATION_ID = "test-tool";

  @BeforeEach
  void setUp() {
    // MockitoBean is already injected, no setup needed
  }

  @Test
  void findByRegistrationId_returnsClientRegistrationWhenFound() {
    LtiProvider provider = createLtiProvider();
    LtiRegistration registration = createLtiRegistration(provider);

    when(ltiRegistrationRepo.findByName(REGISTRATION_ID)).thenReturn(registration);

    ClientRegistration result = repository.findByRegistrationId(REGISTRATION_ID);

    assertNotNull(result);
    assertEquals(REGISTRATION_ID, result.getRegistrationId());
    assertEquals("client-123", result.getClientId());
    assertEquals("secret-456", result.getClientSecret());
  }

  @Test
  void findByRegistrationId_returnsNullWhenNotFound() {
    when(ltiRegistrationRepo.findByName(REGISTRATION_ID)).thenReturn(null);

    ClientRegistration result = repository.findByRegistrationId(REGISTRATION_ID);

    assertNull(result);
  }

  @Test
  void findByRegistrationId_buildsCorrectRedirectUrl() {
    LtiProvider provider = createLtiProvider();
    LtiRegistration registration = createLtiRegistration(provider);

    when(ltiRegistrationRepo.findByName(REGISTRATION_ID)).thenReturn(registration);

    ClientRegistration result = repository.findByRegistrationId(REGISTRATION_ID);

    assertNotNull(result);
    // Values from test application.yaml: baseUrl=https://localhost, contextPath=/test,
    // redirectPath=/lti/login
    assertEquals(baseUrl + contextPath + redirectPath, result.getRedirectUri());
  }

  @Test
  void findByRegistrationId_setsProviderUrisCorrectly() {
    LtiProvider provider = createLtiProvider();
    LtiRegistration registration = createLtiRegistration(provider);

    when(ltiRegistrationRepo.findByName(REGISTRATION_ID)).thenReturn(registration);

    ClientRegistration result = repository.findByRegistrationId(REGISTRATION_ID);

    assertNotNull(result);
    assertEquals(
        "https://canvas.instructure.com/api/lti/authorize",
        result.getProviderDetails().getAuthorizationUri());
    assertEquals(
        "https://canvas.instructure.com/login/oauth2/jwks",
        result.getProviderDetails().getJwkSetUri());
    assertEquals("https://canvas.instructure.com", result.getProviderDetails().getIssuerUri());
    assertEquals(
        "https://canvas.instructure.com/login/oauth2/token",
        result.getProviderDetails().getTokenUri());
  }

  @Test
  void findByRegistrationId_setsOpenIdScope() {
    LtiProvider provider = createLtiProvider();
    LtiRegistration registration = createLtiRegistration(provider);

    when(ltiRegistrationRepo.findByName(REGISTRATION_ID)).thenReturn(registration);

    ClientRegistration result = repository.findByRegistrationId(REGISTRATION_ID);

    assertNotNull(result);
    assertTrue(result.getScopes().contains("openid"));
  }

  @Test
  void findByRegistrationId_setsUserNameAttributeName() {
    LtiProvider provider = createLtiProvider();
    LtiRegistration registration = createLtiRegistration(provider);

    when(ltiRegistrationRepo.findByName(REGISTRATION_ID)).thenReturn(registration);

    ClientRegistration result = repository.findByRegistrationId(REGISTRATION_ID);

    assertNotNull(result);
    assertEquals(
        "sub", result.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
  }

  private LtiProvider createLtiProvider() {
    LtiProvider provider = new LtiProvider();
    provider.setId(1L);
    provider.setName("Canvas");
    provider.setIssuerUri("https://canvas.instructure.com");
    provider.setAuthorizationUri("https://canvas.instructure.com/api/lti/authorize");
    provider.setJwkSetUri("https://canvas.instructure.com/login/oauth2/jwks");
    provider.setTokenUri("https://canvas.instructure.com/login/oauth2/token");
    return provider;
  }

  private LtiRegistration createLtiRegistration(LtiProvider provider) {
    LtiRegistration registration = new LtiRegistration();
    registration.setId(1L);
    registration.setName(REGISTRATION_ID);
    registration.setClientId("client-123");
    registration.setClientSecret("secret-456");
    registration.setLtiProvider(provider);
    return registration;
  }
}
