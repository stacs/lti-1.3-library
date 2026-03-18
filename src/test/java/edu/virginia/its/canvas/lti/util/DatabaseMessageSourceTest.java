package edu.virginia.its.canvas.lti.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.virginia.its.canvas.lti.model.Message;
import edu.virginia.its.canvas.lti.repos.MessageRepo;
import java.text.MessageFormat;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseMessageSourceTest {

  private DatabaseMessageSource messageSource;
  private MessageRepo messageRepo;
  private static final String TOOL_NAME = "test-tool";

  @BeforeEach
  void setUp() {
    messageRepo = mock(MessageRepo.class);
    messageSource = new DatabaseMessageSource(messageRepo, TOOL_NAME);
  }

  @Test
  void resolveCodeWithoutArguments_returnsMessageFromDb() {
    String messageKey = "welcome.message";
    String messageText = "Welcome to the application";
    Message message = new Message(TOOL_NAME, messageKey, messageText, "", "en");

    when(messageRepo.findByToolNameAndMessageKeyAndLocale(TOOL_NAME, messageKey, "en"))
        .thenReturn(message);

    String result = messageSource.resolveCodeWithoutArguments(messageKey, Locale.ENGLISH);

    assertEquals(messageText, result);
  }

  @Test
  void resolveCodeWithoutArguments_fallsBackToDefaultLocaleWhenSpecificLocaleNotFound() {
    String messageKey = "welcome.message";
    String messageText = "Welcome";
    Message message = new Message(TOOL_NAME, messageKey, messageText, "", "en");

    when(messageRepo.findByToolNameAndMessageKeyAndLocale(TOOL_NAME, messageKey, "fr"))
        .thenReturn(null);
    when(messageRepo.findByToolNameAndMessageKeyAndLocale(TOOL_NAME, messageKey, "en"))
        .thenReturn(message);

    String result = messageSource.resolveCodeWithoutArguments(messageKey, Locale.FRENCH);

    assertEquals(messageText, result);
  }

  @Test
  void resolveCodeWithoutArguments_returnsNullWhenNotFoundInDb() {
    String messageKey = "nonexistent.message";

    when(messageRepo.findByToolNameAndMessageKeyAndLocale(TOOL_NAME, messageKey, "en"))
        .thenReturn(null);

    String result = messageSource.resolveCodeWithoutArguments(messageKey, Locale.ENGLISH);

    assertNull(result);
  }

  @Test
  void resolveCode_returnsMessageFormatFromDb() {
    String messageKey = "greeting.message";
    String messageText = "Hello {0}, welcome to {1}";
    Message message = new Message(TOOL_NAME, messageKey, messageText, "", "en");

    when(messageRepo.findByToolNameAndMessageKeyAndLocale(TOOL_NAME, messageKey, "en"))
        .thenReturn(message);

    MessageFormat result = messageSource.resolveCode(messageKey, Locale.ENGLISH);

    assertNotNull(result);
    assertEquals("Hello John, welcome to Canvas", result.format(new Object[] {"John", "Canvas"}));
  }

  @Test
  void resolveCode_fallsBackToDefaultLocaleWhenSpecificLocaleNotFound() {
    String messageKey = "greeting.message";
    String messageText = "Hello {0}";
    Message message = new Message(TOOL_NAME, messageKey, messageText, "", "en");

    when(messageRepo.findByToolNameAndMessageKeyAndLocale(TOOL_NAME, messageKey, "es"))
        .thenReturn(null);
    when(messageRepo.findByToolNameAndMessageKeyAndLocale(TOOL_NAME, messageKey, "en"))
        .thenReturn(message);

    MessageFormat result = messageSource.resolveCode(messageKey, Locale.forLanguageTag("es"));

    assertNotNull(result);
    assertEquals("Hello John", result.format(new Object[] {"John"}));
  }

  @Test
  void resolveCode_returnsNullWhenNotFoundInDb() {
    String messageKey = "nonexistent.message";

    when(messageRepo.findByToolNameAndMessageKeyAndLocale(TOOL_NAME, messageKey, "en"))
        .thenReturn(null);

    MessageFormat result = messageSource.resolveCode(messageKey, Locale.ENGLISH);

    assertNull(result);
  }

  @Test
  void resolveCodeWithoutArguments_handlesEmptyMessageString() {
    String messageKey = "empty.message";
    Message message = new Message(TOOL_NAME, messageKey, "", "", "en");

    when(messageRepo.findByToolNameAndMessageKeyAndLocale(TOOL_NAME, messageKey, "en"))
        .thenReturn(message);

    String result = messageSource.resolveCodeWithoutArguments(messageKey, Locale.ENGLISH);

    assertNull(result);
  }

  @Test
  void resolveCode_createsMessageFormatWithCorrectLocale() {
    String messageKey = "number.message";
    String messageText = "You have {0,number} items";
    Message message = new Message(TOOL_NAME, messageKey, messageText, "", "en");

    when(messageRepo.findByToolNameAndMessageKeyAndLocale(TOOL_NAME, messageKey, "en"))
        .thenReturn(message);

    MessageFormat result = messageSource.resolveCode(messageKey, Locale.ENGLISH);

    assertNotNull(result);
    assertEquals(Locale.ENGLISH, result.getLocale());
  }
}
