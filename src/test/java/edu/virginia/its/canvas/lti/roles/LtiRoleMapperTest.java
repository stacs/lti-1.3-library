package edu.virginia.its.canvas.lti.roles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.virginia.its.canvas.lti.util.Constants;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import uk.ac.ox.ctl.lti13.lti.Claims;

class LtiRoleMapperTest {

  private LtiRoleMapper mapper;

  @BeforeEach
  void setUp() {
    LtiRolesMap ltiRolesMap = new LtiRolesMap();
    ltiRolesMap.put(Constants.LTI_ADMIN, Constants.ADMIN_ROLE);
    ltiRolesMap.put(Constants.LTI_INSTRUCTOR, Constants.INSTRUCTOR_ROLE);
    ltiRolesMap.put(Constants.LTI_TEACHING_ASSISTANT, Constants.TA_ROLE);
    ltiRolesMap.put(Constants.LTI_LEARNER, Constants.STUDENT_ROLE);
    mapper = new LtiRoleMapper(ltiRolesMap);
  }

  @Test
  void mapAuthorities_mapsLtiRolesAndPreservesOriginal() {
    // Test single role
    Map<String, Object> singleRoleAttrs = new HashMap<>();
    singleRoleAttrs.put(Claims.ROLES, List.of(Constants.LTI_INSTRUCTOR));
    OidcUserAuthority singleAuthority = createOidcUserAuthority(singleRoleAttrs);
    Collection<? extends GrantedAuthority> singleResult =
        mapper.mapAuthorities(Collections.singletonList(singleAuthority));

    assertEquals(2, singleResult.size());
    assertTrue(containsAuthorityWithRole(singleResult, Constants.INSTRUCTOR_ROLE));
    assertTrue(singleResult.contains(singleAuthority), "Should preserve original authority");

    // Test multiple roles
    Map<String, Object> multiRoleAttrs = new HashMap<>();
    multiRoleAttrs.put(
        Claims.ROLES, List.of(Constants.LTI_INSTRUCTOR, Constants.LTI_TEACHING_ASSISTANT));
    OidcUserAuthority multiAuthority = createOidcUserAuthority(multiRoleAttrs);
    Collection<? extends GrantedAuthority> multiResult =
        mapper.mapAuthorities(Collections.singletonList(multiAuthority));

    assertEquals(3, multiResult.size());
    assertTrue(containsAuthorityWithRole(multiResult, Constants.INSTRUCTOR_ROLE));
    assertTrue(containsAuthorityWithRole(multiResult, Constants.TA_ROLE));
  }

  @Test
  void mapAuthorities_handlesEdgeCasesGracefully() {
    // Missing roles claim
    Map<String, Object> missingRoles = new HashMap<>();
    OidcUserAuthority missingAuth = createOidcUserAuthority(missingRoles);
    Collection<? extends GrantedAuthority> missingResult =
        mapper.mapAuthorities(Collections.singletonList(missingAuth));
    assertEquals(1, missingResult.size());

    // Empty roles list
    Map<String, Object> emptyRoles = new HashMap<>();
    emptyRoles.put(Claims.ROLES, Collections.emptyList());
    OidcUserAuthority emptyAuth = createOidcUserAuthority(emptyRoles);
    Collection<? extends GrantedAuthority> emptyResult =
        mapper.mapAuthorities(Collections.singletonList(emptyAuth));
    assertEquals(1, emptyResult.size());

    // Unmapped roles
    Map<String, Object> unknownRoles = new HashMap<>();
    unknownRoles.put(Claims.ROLES, List.of("http://unknown.role"));
    OidcUserAuthority unknownAuth = createOidcUserAuthority(unknownRoles);
    Collection<? extends GrantedAuthority> unknownResult =
        mapper.mapAuthorities(Collections.singletonList(unknownAuth));
    assertEquals(1, unknownResult.size());
    assertFalse(containsAuthorityWithRole(unknownResult, "http://unknown.role"));
  }

  @Test
  void mapAuthorities_handlesInvalidRoleData() {
    // Roles claim is not a list
    Map<String, Object> notListAttrs = new HashMap<>();
    notListAttrs.put(Claims.ROLES, "not a list");
    OidcUserAuthority notListAuth = createOidcUserAuthority(notListAttrs);
    Collection<? extends GrantedAuthority> notListResult =
        mapper.mapAuthorities(Collections.singletonList(notListAuth));
    assertEquals(1, notListResult.size());

    // Non-string values in roles list
    Map<String, Object> nonStringAttrs = new HashMap<>();
    nonStringAttrs.put(Claims.ROLES, List.of(123, 456));
    OidcUserAuthority nonStringAuth = createOidcUserAuthority(nonStringAttrs);
    Collection<? extends GrantedAuthority> nonStringResult =
        mapper.mapAuthorities(Collections.singletonList(nonStringAuth));
    assertEquals(1, nonStringResult.size());

    // Mixed valid and invalid roles
    Map<String, Object> mixedAttrs = new HashMap<>();
    mixedAttrs.put(Claims.ROLES, List.of(Constants.LTI_ADMIN, "unknown_role", 123));
    OidcUserAuthority mixedAuth = createOidcUserAuthority(mixedAttrs);
    Collection<? extends GrantedAuthority> mixedResult =
        mapper.mapAuthorities(Collections.singletonList(mixedAuth));
    assertTrue(containsAuthorityWithRole(mixedResult, Constants.ADMIN_ROLE));
    assertEquals(2, mixedResult.size());
  }

  private OidcUserAuthority createOidcUserAuthority(Map<String, Object> attributes) {
    // OidcUserAuthority requires 'sub' claim
    Map<String, Object> allAttributes = new HashMap<>(attributes);
    allAttributes.putIfAbsent("sub", "test-user-123");

    OidcIdToken idToken = mock(OidcIdToken.class);
    OidcUserInfo userInfo = new OidcUserInfo(allAttributes);
    return new OidcUserAuthority(idToken, userInfo);
  }

  private boolean containsAuthorityWithRole(
      Collection<? extends GrantedAuthority> authorities, String role) {
    return authorities.stream().anyMatch(auth -> auth.getAuthority().equals(role));
  }
}
