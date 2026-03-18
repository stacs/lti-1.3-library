package edu.virginia.its.canvas.lti.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MessageTest {

  @Test
  void constructor_setsAllFieldsCorrectly() {
    String toolName = "test-tool";
    String messageKey = "welcome.message";
    String message = "Welcome to the application";
    String defaultMessage = "Welcome";
    String locale = "en";

    Message msg = new Message(toolName, messageKey, message, defaultMessage, locale);

    assertEquals(toolName, msg.getToolName());
    assertEquals(messageKey, msg.getMessageKey());
    assertEquals(message, msg.getMessage());
    assertEquals(defaultMessage, msg.getDefaultMessage());
    assertEquals(locale, msg.getLocale());
  }

  @Test
  void noArgsConstructor_createsInstance() {
    Message msg = new Message();

    assertNotNull(msg);
  }

  @Test
  void setters_workCorrectly() {
    Message msg = new Message();

    msg.setToolName("my-tool");
    msg.setMessageKey("error.message");
    msg.setMessage("An error occurred");
    msg.setDefaultMessage("Error");
    msg.setLocale("fr");

    assertEquals("my-tool", msg.getToolName());
    assertEquals("error.message", msg.getMessageKey());
    assertEquals("An error occurred", msg.getMessage());
    assertEquals("Error", msg.getDefaultMessage());
    assertEquals("fr", msg.getLocale());
  }

  @Test
  void constructor_handlesEmptyStrings() {
    Message msg = new Message("", "", "", "", "");

    assertEquals("", msg.getToolName());
    assertEquals("", msg.getMessageKey());
    assertEquals("", msg.getMessage());
    assertEquals("", msg.getDefaultMessage());
    assertEquals("", msg.getLocale());
  }

  @Test
  void toString_returnsNonNullString() {
    Message msg = new Message("tool", "key", "msg", "default", "en");

    String result = msg.toString();

    assertNotNull(result);
    assertTrue(result.contains("Message"));
  }
}
