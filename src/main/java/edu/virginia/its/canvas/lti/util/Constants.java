package edu.virginia.its.canvas.lti.util;

import java.util.Locale;

public final class Constants {
  private Constants() {}

  public static final String SYSTEM_USER = "SYSTEM";

  public static final String LTI_ADMIN =
      "http://purl.imsglobal.org/vocab/lis/v2/institution/person#Administrator";
  public static final String LTI_INSTRUCTOR =
      "http://purl.imsglobal.org/vocab/lis/v2/membership#Instructor";
  public static final String LTI_TEACHING_ASSISTANT =
      "http://purl.imsglobal.org/vocab/lis/v2/membership/Instructor#TeachingAssistant";
  public static final String LTI_LEARNER =
      "http://purl.imsglobal.org/vocab/lis/v2/membership#Learner";

  public static final String TEACHER_ENROLLMENT = "TeacherEnrollment";
  public static final String TA_ENROLLMENT = "TaEnrollment";
  public static final String STUDENT_ENROLLMENT = "StudentEnrollment";
  public static final String WAITLISTED_STUDENT = "Waitlisted Student";
  public static final String ACCOUNT_ADMIN = "Account Admin";
  public static final String SUBACCOUNT_ADMIN = "Subaccount Admin";
  public static final String LIBRARIAN = "Librarian";
  public static final String DESIGNER_ENROLLMENT = "DesignerEnrollment";
  public static final String OBSERVER_ENROLLMENT = "ObserverEnrollment";

  public static final String ADMIN_ROLE = "ROLE_ADMIN";
  public static final String INSTRUCTOR_ROLE = "ROLE_INSTRUCTOR";
  public static final String TA_ROLE = "ROLE_TA";
  public static final String STUDENT_ROLE = "ROLE_STUDENT";
  public static final String LIBRARIAN_ROLE = "ROLE_LIBRARIAN";
  public static final String DESIGNER_ROLE = "ROLE_DESIGNER";
  public static final String OBSERVER_ROLE = "ROLE_OBSERVER";

  public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

  public static final String CANVAS_LINK_HEADER = "link";
  public static final String USERNAME_CUSTOM_KEY = "username";
  public static final String COURSE_ID_CUSTOM_KEY = "course_id";
  public static final String CANVAS_ROLES_CUSTOM_KEY = "canvas_roles";
}
