package edu.virginia.its.canvas.lti.util;

import org.springframework.context.ApplicationContext;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

public final class AuthorizationUtils {
  private AuthorizationUtils() {}

  public static WebExpressionAuthorizationManager getAuthz(
      String accessString, ApplicationContext context) {
    DefaultHttpSecurityExpressionHandler expressionHandler =
        new DefaultHttpSecurityExpressionHandler();
    expressionHandler.setApplicationContext(context);
    WebExpressionAuthorizationManager webExpressionAuthorizationManager =
        new WebExpressionAuthorizationManager(accessString);
    webExpressionAuthorizationManager.setExpressionHandler(expressionHandler);
    return webExpressionAuthorizationManager;
  }
}
