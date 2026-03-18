package edu.virginia.its.canvas.lti.roles;

import static org.junit.jupiter.api.Assertions.*;

import edu.virginia.its.canvas.lti.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultLtiRoleMappingsTest {

  private DefaultLtiRoleMappings defaultMappings;
  private LtiRolesMap rolesMap;

  @BeforeEach
  void setUp() {
    defaultMappings = new DefaultLtiRoleMappings();
    rolesMap = defaultMappings.ltiRoleMappings();
  }

  @Test
  void ltiRoleMappings_returnsNonNullMap() {
    assertNotNull(rolesMap);
  }

  @Test
  void ltiRoleMappings_ltiAdminMapsToAdminRole() {
    assertEquals(Constants.ADMIN_ROLE, rolesMap.get(Constants.LTI_ADMIN));
  }

  @Test
  void ltiRoleMappings_ltiInstructorMapsToInstructorRole() {
    assertEquals(Constants.INSTRUCTOR_ROLE, rolesMap.get(Constants.LTI_INSTRUCTOR));
  }

  @Test
  void ltiRoleMappings_ltiTeachingAssistantMapsToTaRole() {
    assertEquals(Constants.TA_ROLE, rolesMap.get(Constants.LTI_TEACHING_ASSISTANT));
  }

  @Test
  void ltiRoleMappings_ltiLearnerMapsToStudentRole() {
    assertEquals(Constants.STUDENT_ROLE, rolesMap.get(Constants.LTI_LEARNER));
  }

  @Test
  void ltiRoleMappings_containsAllExpectedMappings() {
    assertEquals(4, rolesMap.size());
  }

  @Test
  void ltiRoleMappings_allMappingsAreNotNull() {
    rolesMap.values().forEach(role -> assertNotNull(role, "All role mappings should be non-null"));
  }

  @Test
  void ltiRoleMappings_allKeysAreValidLtiRoleUris() {
    rolesMap
        .keySet()
        .forEach(
            key -> {
              assertTrue(
                  key.startsWith("http://purl.imsglobal.org/"),
                  "All LTI role keys should be valid URIs");
            });
  }

  @Test
  void ltiRoleMappings_allValuesAreSpringSecurityRoles() {
    rolesMap
        .values()
        .forEach(
            role -> {
              assertTrue(
                  role.startsWith("ROLE_"), "All mapped values should be Spring Security roles");
            });
  }
}
