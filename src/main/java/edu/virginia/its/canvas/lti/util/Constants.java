package edu.virginia.its.canvas.lti.util;

public final class Constants {
  private Constants() {}

  public static final String LTI_ADMIN =
      "http://purl.imsglobal.org/vocab/lis/v2/institution/person#Administrator";
  public static final String LTI_INSTRUCTOR =
      "http://purl.imsglobal.org/vocab/lis/v2/institution/person#Instructor";
  public static final String LTI_TEACHING_ASSISTANT =
      "http://purl.imsglobal.org/vocab/lis/v2/membership/Instructor#TeachingAssistant";
  public static final String LTI_STUDENT =
      "http://purl.imsglobal.org/vocab/lis/v2/institution/person#Student";

  public static final String ADMIN_ROLE = "ROLE_ADMIN";
  public static final String INSTRUCTOR_ROLE = "ROLE_INSTRUCTOR";
  public static final String TA_ROLE = "ROLE_TA";
  public static final String STUDENT_ROLE = "ROLE_STUDENT";
}
