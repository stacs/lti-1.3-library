package edu.virginia.its.canvas.lti.config;

import static org.junit.jupiter.api.Assertions.*;

import edu.virginia.its.canvas.lti.model.Message;
import edu.virginia.its.canvas.lti.repos.MessageRepo;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MessagePropertiesInsertTest {

  @Autowired private MessageRepo messageRepo;

  @Test
  public void insertMessagesIntoDatabase() {
    List<Message> messages = messageRepo.findAll();
    assertEquals(12, messages.size());
    Message message =
        messages.stream()
            .filter(m -> "en".equals(m.getLocale()) && "test1".equals(m.getMessageKey()))
            .findFirst()
            .orElse(null);
    assertNotNull(message);
    assertEquals("test1", message.getMessage());
    assertEquals("test1", message.getDefaultMessage());
    assertEquals("lti-1.3-library", message.getToolName());
    assertEquals("SYSTEM", message.getCreatedBy());
    assertEquals("SYSTEM", message.getLastUpdatedBy());
    message =
        messages.stream()
            .filter(m -> "en".equals(m.getLocale()) && "test2".equals(m.getMessageKey()))
            .findFirst()
            .orElse(null);
    assertNotNull(message);
    assertEquals("preExistingMessage", message.getMessage());
    assertEquals("test2", message.getDefaultMessage());
    assertEquals("lti-1.3-library", message.getToolName());
    assertEquals("SYSTEM", message.getCreatedBy());
    assertEquals("SYSTEM", message.getLastUpdatedBy());
    message =
        messages.stream()
            .filter(m -> "en".equals(m.getLocale()) && "test3".equals(m.getMessageKey()))
            .findFirst()
            .orElse(null);
    assertNotNull(message);
    assertEquals("test3", message.getMessage());
    assertEquals("test3", message.getDefaultMessage());
    assertEquals("lti-1.3-library", message.getToolName());
    assertEquals("SYSTEM", message.getCreatedBy());
    assertEquals("SYSTEM", message.getLastUpdatedBy());
    message =
        messages.stream()
            .filter(m -> "es_ES".equals(m.getLocale()) && "test1".equals(m.getMessageKey()))
            .findFirst()
            .orElse(null);
    assertNotNull(message);
    assertEquals("test1-es", message.getMessage());
    assertEquals("test1-es", message.getDefaultMessage());
    assertEquals("lti-1.3-library", message.getToolName());
    assertEquals("SYSTEM", message.getCreatedBy());
    assertEquals("SYSTEM", message.getLastUpdatedBy());
    message =
        messages.stream()
            .filter(m -> "es_ES".equals(m.getLocale()) && "test2".equals(m.getMessageKey()))
            .findFirst()
            .orElse(null);
    assertNotNull(message);
    assertEquals("test2-es", message.getMessage());
    assertEquals("test2-es", message.getDefaultMessage());
    assertEquals("lti-1.3-library", message.getToolName());
    assertEquals("SYSTEM", message.getCreatedBy());
    assertEquals("SYSTEM", message.getLastUpdatedBy());
    message =
        messages.stream()
            .filter(m -> "es_ES".equals(m.getLocale()) && "test3".equals(m.getMessageKey()))
            .findFirst()
            .orElse(null);
    assertNotNull(message);
    assertEquals("test3-es", message.getMessage());
    assertEquals("test3-es", message.getDefaultMessage());
    assertEquals("lti-1.3-library", message.getToolName());
    assertEquals("SYSTEM", message.getCreatedBy());
    assertEquals("SYSTEM", message.getLastUpdatedBy());

    message =
        messages.stream()
            .filter(m -> "en".equals(m.getLocale()) && "other.test1".equals(m.getMessageKey()))
            .findFirst()
            .orElse(null);
    assertNotNull(message);
    assertEquals("otherTest1", message.getMessage());
    assertEquals("otherTest1", message.getDefaultMessage());
    assertEquals("lti-1.3-library", message.getToolName());
    assertEquals("SYSTEM", message.getCreatedBy());
    assertEquals("SYSTEM", message.getLastUpdatedBy());
    message =
        messages.stream()
            .filter(m -> "en".equals(m.getLocale()) && "other.test2".equals(m.getMessageKey()))
            .findFirst()
            .orElse(null);
    assertNotNull(message);
    assertEquals("otherTest2", message.getMessage());
    assertEquals("otherTest2", message.getDefaultMessage());
    assertEquals("lti-1.3-library", message.getToolName());
    assertEquals("SYSTEM", message.getCreatedBy());
    assertEquals("SYSTEM", message.getLastUpdatedBy());
    message =
        messages.stream()
            .filter(m -> "en".equals(m.getLocale()) && "other.test3".equals(m.getMessageKey()))
            .findFirst()
            .orElse(null);
    assertNotNull(message);
    assertEquals("otherTest3", message.getMessage());
    assertEquals("otherTest3", message.getDefaultMessage());
    assertEquals("lti-1.3-library", message.getToolName());
    assertEquals("SYSTEM", message.getCreatedBy());
    assertEquals("SYSTEM", message.getLastUpdatedBy());
    message =
        messages.stream()
            .filter(m -> "es_ES".equals(m.getLocale()) && "other.test1".equals(m.getMessageKey()))
            .findFirst()
            .orElse(null);
    assertNotNull(message);
    assertEquals("anotherPreExistingMessage", message.getMessage());
    assertEquals("otherTest1-es", message.getDefaultMessage());
    assertEquals("lti-1.3-library", message.getToolName());
    assertEquals("SYSTEM", message.getCreatedBy());
    assertEquals("SYSTEM", message.getLastUpdatedBy());
    message =
        messages.stream()
            .filter(m -> "es_ES".equals(m.getLocale()) && "other.test2".equals(m.getMessageKey()))
            .findFirst()
            .orElse(null);
    assertNotNull(message);
    assertEquals("otherTest2-es", message.getMessage());
    assertEquals("otherTest2-es", message.getDefaultMessage());
    assertEquals("lti-1.3-library", message.getToolName());
    assertEquals("SYSTEM", message.getCreatedBy());
    assertEquals("SYSTEM", message.getLastUpdatedBy());
    message =
        messages.stream()
            .filter(m -> "es_ES".equals(m.getLocale()) && "other.test3".equals(m.getMessageKey()))
            .findFirst()
            .orElse(null);
    assertNotNull(message);
    assertEquals("otherTest3-es", message.getMessage());
    assertEquals("otherTest3-es", message.getDefaultMessage());
    assertEquals("lti-1.3-library", message.getToolName());
    assertEquals("SYSTEM", message.getCreatedBy());
    assertEquals("SYSTEM", message.getLastUpdatedBy());
  }
}
