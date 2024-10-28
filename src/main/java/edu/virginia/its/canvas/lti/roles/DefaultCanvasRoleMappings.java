package edu.virginia.its.canvas.lti.roles;

import edu.virginia.its.canvas.lti.util.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * This class provides some default role mappings between Canvas enrollments and Spring Security
 * roles. A tool can override this by providing their own @Bean named 'canvasRoleMappings'.
 */
@Component
public final class DefaultCanvasRoleMappings {

  @Bean
  @ConditionalOnMissingBean
  public CanvasRolesMap canvasRoleMappings() {
    CanvasRolesMap roleMappings = new CanvasRolesMap();
    roleMappings.put(Constants.TEACHER_ENROLLMENT, Constants.INSTRUCTOR_ROLE);
    roleMappings.put(Constants.TA_ENROLLMENT, Constants.TA_ROLE);
    roleMappings.put(Constants.ACCOUNT_ADMIN, Constants.ADMIN_ROLE);
    roleMappings.put(Constants.SUBACCOUNT_ADMIN, Constants.ADMIN_ROLE);
    roleMappings.put(Constants.STUDENT_ENROLLMENT, Constants.STUDENT_ROLE);
    roleMappings.put(Constants.WAITLISTED_STUDENT, Constants.STUDENT_ROLE);
    roleMappings.put(Constants.LIBRARIAN, Constants.LIBRARIAN_ROLE);
    roleMappings.put(Constants.DESIGNER_ENROLLMENT, Constants.DESIGNER_ROLE);
    roleMappings.put(Constants.OBSERVER_ENROLLMENT, Constants.OBSERVER_ROLE);
    return roleMappings;
  }
}
