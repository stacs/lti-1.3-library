package edu.virginia.its.canvas.lti.util;

import edu.virginia.its.canvas.lti.exception.CanvasTokenException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

@Slf4j
public class CanvasTokenLocaleResolver extends AbstractLocaleResolver {

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    try {
      String localeString = CanvasAuthenticationToken.getToken().getLocale();
      Locale locale = Locale.forLanguageTag(localeString);
      if (locale != null) {
        return locale;
      } else {
        log.warn("Could not find Locale object for locale string '{}'", localeString);
      }
    } catch (CanvasTokenException ex) {
      log.warn("Could not find Canvas token to determine locale, returning default", ex);
    }
    return getDefaultLocale();
  }

  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    throw new UnsupportedOperationException("Cannot change the locale coming from Canvas");
  }
}
