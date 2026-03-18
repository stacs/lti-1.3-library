package edu.virginia.its.canvas.lti.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CanvasTokenExceptionTest {

  @Test
  void constructor_setsMessage() {
    String errorMessage = "Could not find OidcAuthenticationToken";

    CanvasTokenException exception = new CanvasTokenException(errorMessage);

    assertEquals(errorMessage, exception.getMessage());
  }

  @Test
  void exception_canBeThrown() {
    assertThrows(
        CanvasTokenException.class,
        () -> {
          throw new CanvasTokenException("Test exception");
        });
  }

  @Test
  void exception_canBeCaught() {
    try {
      throw new CanvasTokenException("Test exception");
    } catch (CanvasTokenException e) {
      assertEquals("Test exception", e.getMessage());
    }
  }

  @Test
  void exception_isRuntimeException() {
    CanvasTokenException exception = new CanvasTokenException("Test");

    assertTrue(exception instanceof RuntimeException);
  }

  @Test
  void constructor_handlesNullMessage() {
    CanvasTokenException exception = new CanvasTokenException(null);

    assertNull(exception.getMessage());
  }

  @Test
  void constructor_handlesEmptyMessage() {
    CanvasTokenException exception = new CanvasTokenException("");

    assertEquals("", exception.getMessage());
  }
}
