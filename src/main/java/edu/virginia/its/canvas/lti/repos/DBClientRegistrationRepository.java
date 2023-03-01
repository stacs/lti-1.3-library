package edu.virginia.its.canvas.lti.repos;

import edu.virginia.its.canvas.lti.model.LtiProvider;
import edu.virginia.its.canvas.lti.model.LtiRegistration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

/**
 * An implementation of ClientRegistrationRepository that can get a ClientRegistration from a
 * database. This can be used instead of InMemoryClientRegistrationRepository when you want to allow
 * the registrations to be updated dynamically without restarting the service.
 */
@Slf4j
public class DBClientRegistrationRepository implements ClientRegistrationRepository {

  @Value("${ltitool.baseUrl}")
  private String baseUrl;

  @Value("${server.servlet.context-path:}")
  private String contextPath;

  @Value("${ltitool.oauth2.redirectPath:/lti/login}")
  private String redirectPath;

  public static final String OPENID_SCOPE = "openid";

  @Autowired private LtiRegistrationRepository repo;

  @Override
  public ClientRegistration findByRegistrationId(String registrationId) {
    String redirectUrl = baseUrl + contextPath + redirectPath;
    // The default registration ID is the maven artifact ID of the Tool
    LtiRegistration ltiRegistration = repo.findByName(registrationId);
    if (ltiRegistration != null) {
      LtiProvider ltiProvider = ltiRegistration.getLtiProvider();
      // This configures the registration information that the Tool (our LTI app) must store about
      // the Platform (Canvas)
      return ClientRegistration.withRegistrationId(registrationId)
          // The toolKey String needs to match the end path value for the oidc_initiation_url
          // configured in Canvas
          .authorizationGrantType(AuthorizationGrantType.IMPLICIT)
          .scope(OPENID_SCOPE)
          .userNameAttributeName(IdTokenClaimNames.SUB)
          .clientId(ltiRegistration.getClientId())
          .clientSecret(ltiRegistration.getClientId())
          .redirectUri(redirectUrl)
          .authorizationUri(ltiProvider.getAuthorizationUri())
          .jwkSetUri(ltiProvider.getJwkSetUri())
          .issuerUri(ltiProvider.getIssuerUri())
          .tokenUri(ltiProvider.getTokenUri())
          .build();
    } else {
      return null;
    }
  }
}
