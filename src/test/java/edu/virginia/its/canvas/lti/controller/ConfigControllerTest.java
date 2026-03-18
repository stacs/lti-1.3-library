package edu.virginia.its.canvas.lti.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.virginia.lts.canvas.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class ConfigControllerTest {

  private ConfigController controller;

  @BeforeEach
  void setUp() {
    Config config = mock(Config.class);
    when(config.getOidcInitiationUrl()).thenReturn("https://example.com/lti/oidc");
    when(config.getTargetLinkUri()).thenReturn("https://example.com/lti/launch");
    when(config.getPublicJwkUrl()).thenReturn("https://example.com/.well-known/jwks.json");
    when(config.getTitle()).thenReturn("Test LTI Tool");
    controller = new ConfigController(config);
  }

  @Test
  void getConfigJson_returnsValidFormattedJsonWithAllFields() {
    String result = controller.getConfigJson();

    // Verify it's valid, non-empty JSON
    assertNotNull(result);
    assertFalse(result.isEmpty());

    // Verify it's formatted with pretty printer (has newlines)
    assertTrue(result.contains("\n"));

    // Verify it includes all config fields
    assertTrue(result.contains("Test LTI Tool"));
    assertTrue(result.contains("https://example.com/lti/oidc"));
    assertTrue(result.contains("https://example.com/lti/launch"));
    assertTrue(result.contains("https://example.com/.well-known/jwks.json"));
  }

  @Test
  void getConfigJson_excludesNullFields() {
    Config configWithNulls = mock(Config.class);
    when(configWithNulls.getTitle()).thenReturn("Test Tool");
    ConfigController controllerWithNulls = new ConfigController(configWithNulls);

    String result = controllerWithNulls.getConfigJson();

    assertNotNull(result);
    // Should not contain fields that are null
    assertFalse(result.contains("null"));
  }

  @Test
  void getConfigJson_handlesJsonProcessingException() throws Exception {
    Config testConfig = mock(Config.class);
    ConfigController testController = new ConfigController(testConfig);

    ObjectMapper mockMapper = mock(ObjectMapper.class);
    ObjectWriter mockWriter = mock(ObjectWriter.class);
    when(mockMapper.writerWithDefaultPrettyPrinter()).thenReturn(mockWriter);
    when(mockWriter.writeValueAsString(any()))
        .thenThrow(new JsonProcessingException("Test error") {});

    ReflectionTestUtils.setField(testController, "mapper", mockMapper);

    String result = testController.getConfigJson();

    assertEquals("Error with configuration", result);
  }

  @Test
  void constructor_completesSuccessfully() {
    Config testConfig = mock(Config.class);
    ConfigController testController = new ConfigController(testConfig);

    assertNotNull(testController);
  }
}
