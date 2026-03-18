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

  public static final String PLACEMENT_ATTRIBUTE = "https://www.instructure.com/placement";
  public static final String EMAIL_ATTRIBUTE = "email";
  public static final String NAME_ATTRIBUTE = "name";
  public static final String GIVEN_NAME_ATTRIBUTE = "given_name";
  public static final String FAMILY_NAME_ATTRIBUTE = "family_name";
  public static final String PICTURE_ATTRIBUTE = "picture";
  public static final String LOCALE_ATTRIBUTE = "locale";

  public static final String CANVAS_USER_ID = "$Canvas.user.id";
  public static final String CANVAS_USER_LOGINID = "$Canvas.user.loginId";
  public static final String CANVAS_USER_ISROOTACCOUNTADMIN = "$Canvas.user.isRootAccountAdmin";
  public static final String CANVAS_ROLES = "$Canvas.memberships.roles";
  public static final String PERSON_ADDRESS_TIMEZONE = "$Person.address.timezone";
  public static final String USER_STUDENT_VIEW = "$com.instructure.User.student_view";
  public static final String CONTEXT_TITLE = "$Context.title";
  public static final String POST_MESSAGE_TOKEN = "$com.instructure.PostMessageToken";
  public static final String CANVAS_API_BASE_URL = "$Canvas.api.baseUrl";
  public static final String CANVAS_API_DOMAIN = "$Canvas.api.domain";
  public static final String CANVAS_COURSE_ID = "$Canvas.course.id";
  public static final String CANVAS_COURSE_NAME = "$Canvas.course.name";
  public static final String CANVAS_COURSE_SISID = "$Canvas.course.sisSourceId";
  public static final String CANVAS_COURSE_SECTION_IDS = "$Canvas.course.sectionIds";
  public static final String CANVAS_COURSE_SECTION_SISIDS = "$Canvas.course.sectionSisSourceIds";
  public static final String CANVAS_COURSE_SECTIONRESTRICTED = "$Canvas.course.sectionRestricted";
  public static final String CANVAS_COURSE_GRADINGSCHEME = "$com.instructure.Course.gradingScheme";
  public static final String CANVAS_ACCOUNT_ID = "$Canvas.account.id";
  public static final String CANVAS_ACCOUNT_NAME = "$Canvas.account.name";
  public static final String CANVAS_ACCOUNT_SISID = "$Canvas.account.sisSourceId";
  public static final String CANVAS_ROOTACCOUNT_ID = "$Canvas.rootAccount.id";
  public static final String CANVAS_TERM_ID = "$Canvas.term.id";
  public static final String CANVAS_TERM_NAME = "$Canvas.term.name";

  public static final String CANVAS_USER_ID_CUSTOM_KEY = "canvas_user_id";
  public static final String CANVAS_USER_LOGINID_CUSTOM_KEY = "canvas_user_loginid";
  public static final String CANVAS_USER_ISROOTACCOUNTADMIN_CUSTOM_KEY =
      "canvas_user_isrootaccountadmin";
  public static final String CANVAS_ROLES_CUSTOM_KEY = "canvas_roles";
  public static final String PERSON_ADDRESS_TIMEZONE_CUSTOM_KEY = "person_address_timezone";
  public static final String USER_STUDENT_VIEW_CUSTOM_KEY = "user_student_view";
  public static final String CONTEXT_TITLE_CUSTOM_KEY = "context_title";
  public static final String POST_MESSAGE_TOKEN_CUSTOM_KEY = "post_message_token";
  public static final String CANVAS_API_BASE_URL_CUSTOM_KEY = "canvas_api_base_url";
  public static final String CANVAS_API_DOMAIN_CUSTOM_KEY = "canvas_api_domain";
  public static final String CANVAS_COURSE_ID_CUSTOM_KEY = "canvas_course_id";
  public static final String CANVAS_COURSE_NAME_CUSTOM_KEY = "canvas_course_name";
  public static final String CANVAS_COURSE_SISID_CUSTOM_KEY = "canvas_course_sisid";
  public static final String CANVAS_COURSE_SECTION_IDS_CUSTOM_KEY = "canvas_course_section_ids";
  public static final String CANVAS_COURSE_SECTION_SISIDS_CUSTOM_KEY =
      "canvas_course_section_sisids";
  public static final String CANVAS_COURSE_SECTIONRESTRICTED_CUSTOM_KEY =
      "canvas_course_sectionrestricted";
  public static final String CANVAS_COURSE_GRADINGSCHEME_CUSTOM_KEY = "canvas_course_gradingscheme";
  public static final String CANVAS_ACCOUNT_ID_CUSTOM_KEY = "canvas_account_id";
  public static final String CANVAS_ACCOUNT_NAME_CUSTOM_KEY = "canvas_account_name";
  public static final String CANVAS_ACCOUNT_SISID_CUSTOM_KEY = "canvas_account_sisid";
  public static final String CANVAS_ROOTACCOUNT_ID_CUSTOM_KEY = "canvas_rootaccount_id";
  public static final String CANVAS_TERM_ID_CUSTOM_KEY = "canvas_term_id";
  public static final String CANVAS_TERM_NAME_CUSTOM_KEY = "canvas_term_name";
}
