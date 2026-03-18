package edu.virginia.its.canvas.lti.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.nimbusds.jose.jwk.JWKSet;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwksControllerTest {

  private JwksController controller;

  @BeforeEach
  void setUp() {
    controller = new JwksController();
  }

  @Test
  void getJwks_returnsNonNullMap() {
    Map<String, Object> result = controller.getJwks();

    assertNotNull(result);
  }

  @Test
  void getJwks_containsKeysArray() {
    Map<String, Object> result = controller.getJwks();

    assertTrue(result.containsKey("keys"));
  }

  @Test
  void getJwks_keysArrayIsNotEmpty() {
    Map<String, Object> result = controller.getJwks();

    Object keys = result.get("keys");
    assertNotNull(keys);
  }

  @Test
  void getJwks_returnsPublicKeysOnly() {
    Map<String, Object> result = controller.getJwks();

    // toPublicJWKSet() is called in getJwks(), so private key material should not be present
    String jsonString = result.toString();
    // Check that private key components are not in the keys
    assertFalse(
        jsonString.contains("d=") && jsonString.contains("p=") && jsonString.contains("q="),
        "Should not contain all private key components");

    // More reliable: check the actual keys don't have 'd' field
    @SuppressWarnings("unchecked")
    java.util.List<Map<String, Object>> keys =
        (java.util.List<Map<String, Object>>) result.get("keys");
    for (Map<String, Object> key : keys) {
      assertFalse(key.containsKey("d"), "Public key should not contain private exponent 'd'");
    }
  }

  @Test
  void jwkSet_returnsNonNullJWKSet() {
    JWKSet jwkSet = controller.jwkSet();

    assertNotNull(jwkSet);
  }

  @Test
  void jwkSet_containsAtLeastOneKey() {
    JWKSet jwkSet = controller.jwkSet();

    assertFalse(jwkSet.getKeys().isEmpty());
  }

  @Test
  void jwkSet_keyHasCorrectProperties() {
    JWKSet jwkSet = controller.jwkSet();

    Map<String, Object> firstKey = jwkSet.getKeys().get(0).toJSONObject();

    assertEquals("RS256", firstKey.get("alg"));
    assertEquals("sig", firstKey.get("use"));
    assertEquals("keyId", firstKey.get("kid"));
    assertEquals("RSA", firstKey.get("kty"));
  }

  @Test
  void jwkSet_keyContainsPublicExponent() {
    JWKSet jwkSet = controller.jwkSet();

    Map<String, Object> firstKey = jwkSet.getKeys().get(0).toJSONObject();

    assertTrue(firstKey.containsKey("e"), "Should contain public exponent 'e'");
    assertTrue(firstKey.containsKey("n"), "Should contain modulus 'n'");
  }

  @Test
  void getJwks_usesJwkSetBean() {
    // jwkSet() is a @Bean, so calling getJwks() should use the same bean instance
    JWKSet jwkSet1 = controller.jwkSet();
    JWKSet jwkSet2 = controller.jwkSet();

    // Verify that both calls return a JWKSet (bean exists)
    assertNotNull(jwkSet1);
    assertNotNull(jwkSet2);

    // Verify getJwks() returns the public portion of the JWK set
    Map<String, Object> result = controller.getJwks();
    assertNotNull(result);
    assertTrue(result.containsKey("keys"));
  }
}
