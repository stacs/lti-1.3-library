package edu.virginia.its.canvas.lti.roles;

import static org.junit.jupiter.api.Assertions.*;

import edu.virginia.its.canvas.lti.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultCanvasRoleMappingsTest {

  private DefaultCanvasRoleMappings defaultMappings;
  private CanvasRolesMap rolesMap;

  @BeforeEach
  void setUp() {
    defaultMappings = new DefaultCanvasRoleMappings();
    rolesMap = defaultMappings.canvasRoleMappings();
  }

  @Test
  void canvasRoleMappings_returnsNonNullMap() {
    assertNotNull(rolesMap);
  }

  @Test
  void canvasRoleMappings_teacherEnrollmentMapsToInstructorRole() {
    assertEquals(Constants.INSTRUCTOR_ROLE, rolesMap.get(Constants.TEACHER_ENROLLMENT));
  }

  @Test
  void canvasRoleMappings_taEnrollmentMapsToTaRole() {
    assertEquals(Constants.TA_ROLE, rolesMap.get(Constants.TA_ENROLLMENT));
  }

  @Test
  void canvasRoleMappings_accountAdminMapsToAdminRole() {
    assertEquals(Constants.ADMIN_ROLE, rolesMap.get(Constants.ACCOUNT_ADMIN));
  }

  @Test
  void canvasRoleMappings_subaccountAdminMapsToAdminRole() {
    assertEquals(Constants.ADMIN_ROLE, rolesMap.get(Constants.SUBACCOUNT_ADMIN));
  }

  @Test
  void canvasRoleMappings_studentEnrollmentMapsToStudentRole() {
    assertEquals(Constants.STUDENT_ROLE, rolesMap.get(Constants.STUDENT_ENROLLMENT));
  }

  @Test
  void canvasRoleMappings_waitlistedStudentMapsToStudentRole() {
    assertEquals(Constants.STUDENT_ROLE, rolesMap.get(Constants.WAITLISTED_STUDENT));
  }

  @Test
  void canvasRoleMappings_librarianMapsToLibrarianRole() {
    assertEquals(Constants.LIBRARIAN_ROLE, rolesMap.get(Constants.LIBRARIAN));
  }

  @Test
  void canvasRoleMappings_designerEnrollmentMapsToDesignerRole() {
    assertEquals(Constants.DESIGNER_ROLE, rolesMap.get(Constants.DESIGNER_ENROLLMENT));
  }

  @Test
  void canvasRoleMappings_observerEnrollmentMapsToObserverRole() {
    assertEquals(Constants.OBSERVER_ROLE, rolesMap.get(Constants.OBSERVER_ENROLLMENT));
  }

  @Test
  void canvasRoleMappings_containsAllExpectedMappings() {
    assertEquals(9, rolesMap.size());
  }

  @Test
  void canvasRoleMappings_allMappingsAreNotNull() {
    rolesMap.values().forEach(role -> assertNotNull(role, "All role mappings should be non-null"));
  }
}
