package edu.virginia.its.canvas.lti.util;

import edu.virginia.its.canvas.lti.model.Message;
import edu.virginia.its.canvas.lti.repos.MessageRepo;
import java.text.MessageFormat;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.ObjectUtils;

@Slf4j
public class DatabaseMessageSource extends ResourceBundleMessageSource {

  @Autowired private MessageRepo messageRepo;

  @Value("${ltitool.toolName}")
  private String toolName;

  @Override
  public String resolveCodeWithoutArguments(String messageKey, Locale locale) {
    String message = getMessageFromDb(messageKey, locale);
    if (ObjectUtils.isEmpty(message)) {
      return super.resolveCodeWithoutArguments(messageKey, locale);
    } else {
      return message;
    }
  }

  @Override
  protected MessageFormat resolveCode(String messageKey, Locale locale) {
    String message = getMessageFromDb(messageKey, locale);
    if (ObjectUtils.isEmpty(message)) {
      return super.resolveCode(messageKey, locale);
    } else {
      return new MessageFormat(message, locale);
    }
  }

  private String getMessageFromDb(String messageKey, Locale locale) {
    // Try to fetch message from DB, if it comes back null then try again with the default locale
    Message message =
        messageRepo.findByToolNameAndMessageKeyAndLocale(toolName, messageKey, locale.toString());
    if (message == null) {
      message =
          messageRepo.findByToolNameAndMessageKeyAndLocale(
              toolName, messageKey, Constants.DEFAULT_LOCALE.toString());
      if (message != null) {
        return message.getMessage();
      }
    } else {
      return message.getMessage();
    }
    return null;
  }
}
