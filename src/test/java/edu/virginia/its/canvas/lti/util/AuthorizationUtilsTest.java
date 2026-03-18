package edu.virginia.its.canvas.lti.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

class AuthorizationUtilsTest {

  @Test
  void getAuthz_createsWebExpressionAuthorizationManagerWithAccessString() {
    ApplicationContext context = new StaticApplicationContext();
    String accessString = "hasRole('ADMIN')";

    WebExpressionAuthorizationManager result = AuthorizationUtils.getAuthz(accessString, context);

    assertNotNull(result);
  }

  @Test
  void getAuthz_setsExpressionHandlerWithApplicationContext() {
    ApplicationContext context = new StaticApplicationContext();
    String accessString = "hasRole('USER')";

    WebExpressionAuthorizationManager result = AuthorizationUtils.getAuthz(accessString, context);

    assertNotNull(result);
  }

  @Test
  void getAuthz_handlesDifferentAccessStrings() {
    ApplicationContext context = new StaticApplicationContext();

    WebExpressionAuthorizationManager result1 =
        AuthorizationUtils.getAuthz("hasRole('ADMIN')", context);
    WebExpressionAuthorizationManager result2 =
        AuthorizationUtils.getAuthz("hasRole('USER')", context);
    WebExpressionAuthorizationManager result3 = AuthorizationUtils.getAuthz("permitAll()", context);

    assertNotNull(result1);
    assertNotNull(result2);
    assertNotNull(result3);
  }

  @Test
  void getAuthz_handlesComplexExpressions() {
    ApplicationContext context = new StaticApplicationContext();
    String complexExpression =
        "hasRole('ADMIN') or (hasRole('USER') and @authService.isAllowed(authentication))";

    WebExpressionAuthorizationManager result =
        AuthorizationUtils.getAuthz(complexExpression, context);

    assertNotNull(result);
  }
}
