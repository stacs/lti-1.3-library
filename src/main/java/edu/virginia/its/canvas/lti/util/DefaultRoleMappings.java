package edu.virginia.its.canvas.lti.util;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * This class provides some default role mappings between LTI roles and Spring Security roles. A
 * tool can override this by providing their own @Bean named 'roleMappings'.
 */
@Component
public final class DefaultRoleMappings {

  @Bean
  @ConditionalOnMissingBean
  public RolesMap roleMappings() {
    RolesMap roleMappings = new RolesMap();
    // Canvas does not differentiate between Account Admins and Subaccount Admins
    roleMappings.put(Constants.LTI_ADMIN, Constants.ADMIN_ROLE);
    // Canvas gives this role to TAs in addition to Teachers
    roleMappings.put(Constants.LTI_INSTRUCTOR, Constants.INSTRUCTOR_ROLE);
    roleMappings.put(Constants.LTI_TEACHING_ASSISTANT, Constants.TA_ROLE);
    roleMappings.put(Constants.LTI_LEARNER, Constants.STUDENT_ROLE);
    return roleMappings;
  }
}
