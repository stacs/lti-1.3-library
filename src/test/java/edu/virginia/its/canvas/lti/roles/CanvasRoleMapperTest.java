package edu.virginia.its.canvas.lti.roles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import edu.virginia.its.canvas.lti.util.Constants;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import uk.ac.ox.ctl.lti13.lti.Claims;

class CanvasRoleMapperTest {

  private CanvasRolesMap canvasRolesMap;
  private CanvasRoleMapper mapper;

  @BeforeEach
  void setUp() {
    canvasRolesMap = new CanvasRolesMap();
    canvasRolesMap.put(Constants.TEACHER_ENROLLMENT, Constants.INSTRUCTOR_ROLE);
    canvasRolesMap.put(Constants.TA_ENROLLMENT, Constants.TA_ROLE);
    canvasRolesMap.put(Constants.STUDENT_ENROLLMENT, Constants.STUDENT_ROLE);
    mapper = new CanvasRoleMapper(canvasRolesMap);
  }

  @Test
  void mapAuthorities_mapsSingleCanvasRole() {
    LinkedTreeMap<String, Object> customMap = new LinkedTreeMap<>();
    customMap.put(Constants.CANVAS_ROLES_CUSTOM_KEY, Constants.TEACHER_ENROLLMENT);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put(Claims.CUSTOM, customMap);

    OidcUserAuthority authority = createOidcUserAuthority(attributes);
    Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);

    Collection<? extends GrantedAuthority> result = mapper.mapAuthorities(authorities);

    assertEquals(2, result.size());
    assertTrue(containsAuthorityWithRole(result, Constants.INSTRUCTOR_ROLE));
  }

  @Test
  void mapAuthorities_mapsMultipleCommaSeparatedCanvasRoles() {
    LinkedTreeMap<String, Object> customMap = new LinkedTreeMap<>();
    customMap.put(
        Constants.CANVAS_ROLES_CUSTOM_KEY,
        Constants.TEACHER_ENROLLMENT + "," + Constants.TA_ENROLLMENT);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put(Claims.CUSTOM, customMap);

    OidcUserAuthority authority = createOidcUserAuthority(attributes);
    Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);

    Collection<? extends GrantedAuthority> result = mapper.mapAuthorities(authorities);

    assertEquals(3, result.size());
    assertTrue(containsAuthorityWithRole(result, Constants.INSTRUCTOR_ROLE));
    assertTrue(containsAuthorityWithRole(result, Constants.TA_ROLE));
  }

  @Test
  void mapAuthorities_preservesOriginalAuthorities() {
    LinkedTreeMap<String, Object> customMap = new LinkedTreeMap<>();
    customMap.put(Constants.CANVAS_ROLES_CUSTOM_KEY, Constants.STUDENT_ENROLLMENT);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put(Claims.CUSTOM, customMap);

    OidcUserAuthority authority = createOidcUserAuthority(attributes);
    Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);

    Collection<? extends GrantedAuthority> result = mapper.mapAuthorities(authorities);

    assertTrue(result.contains(authority));
  }

  @Test
  void mapAuthorities_handlesMissingCustomClaims() {
    Map<String, Object> attributes = new HashMap<>();

    OidcUserAuthority authority = createOidcUserAuthority(attributes);
    Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);

    Collection<? extends GrantedAuthority> result = mapper.mapAuthorities(authorities);

    assertEquals(1, result.size());
    assertTrue(result.contains(authority));
  }

  @Test
  void mapAuthorities_handlesEmptyEnrollmentRoles() {
    LinkedTreeMap<String, Object> customMap = new LinkedTreeMap<>();
    customMap.put(Constants.CANVAS_ROLES_CUSTOM_KEY, "");

    Map<String, Object> attributes = new HashMap<>();
    attributes.put(Claims.CUSTOM, customMap);

    OidcUserAuthority authority = createOidcUserAuthority(attributes);
    Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);

    Collection<? extends GrantedAuthority> result = mapper.mapAuthorities(authorities);

    assertEquals(1, result.size());
  }

  @Test
  void mapAuthorities_handlesUnmappedRoles() {
    LinkedTreeMap<String, Object> customMap = new LinkedTreeMap<>();
    customMap.put(Constants.CANVAS_ROLES_CUSTOM_KEY, "UnknownRole");

    Map<String, Object> attributes = new HashMap<>();
    attributes.put(Claims.CUSTOM, customMap);

    OidcUserAuthority authority = createOidcUserAuthority(attributes);
    Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);

    Collection<? extends GrantedAuthority> result = mapper.mapAuthorities(authorities);

    assertEquals(1, result.size());
    assertFalse(containsAuthorityWithRole(result, "UnknownRole"));
  }

  @Test
  void mapAuthorities_handlesCustomClaimsNotLinkedTreeMap() {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put(Claims.CUSTOM, "not a map");

    OidcUserAuthority authority = createOidcUserAuthority(attributes);
    Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);

    Collection<? extends GrantedAuthority> result = mapper.mapAuthorities(authorities);

    assertEquals(1, result.size());
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
