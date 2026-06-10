package edu.virginia.its.canvas.lti.repos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import uk.ac.ox.ctl.lti13.security.oauth2.client.lti.web.LTIAuthorizationGrantType;

@SpringBootTest
class ClientRegistrationRepositoryTest {

  private static final String REGISTRATION_ID = "canvas";

  @Autowired private ClientRegistrationRepository clientRegistrationRepository;

  @Test
  void findByRegistrationId() {
    assertNull(clientRegistrationRepository.findByRegistrationId("does-not-exist"));

    ClientRegistration result = clientRegistrationRepository.findByRegistrationId(REGISTRATION_ID);
    assertNotNull(result);
    assertEquals(REGISTRATION_ID, result.getRegistrationId());
    assertEquals("client-123", result.getClientId());
    assertEquals(LTIAuthorizationGrantType.IMPLICIT, result.getAuthorizationGrantType());
    assertTrue(result.getScopes().contains("openid"));
    assertEquals("{baseUrl}/lti/login", result.getRedirectUri());
    assertEquals(
        "https://sso.canvaslms.com/api/lti/authorize_redirect",
        result.getProviderDetails().getAuthorizationUri());
    assertEquals(
        "https://sso.canvaslms.com/api/lti/security/jwks",
        result.getProviderDetails().getJwkSetUri());
    assertEquals(
        "https://sso.canvaslms.com/login/oauth2/token", result.getProviderDetails().getTokenUri());
    assertEquals(
        "sub", result.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
  }
}
