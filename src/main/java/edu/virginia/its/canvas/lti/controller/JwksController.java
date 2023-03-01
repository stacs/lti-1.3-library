package edu.virginia.its.canvas.lti.controller;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class JwksController {

  @GetMapping(value = "/.well-known/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> getJwks() {
    return jwkSet().toPublicJWKSet().toJSONObject();
  }

  @Bean
  public JWKSet jwkSet() {
    KeyPair keyPair = null;
    try {
      log.warn("Generating keypair for JWKS endpoint, do not do this in production");
      keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    RSAKey.Builder builder =
        new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
            .keyUse(KeyUse.SIGNATURE)
            .algorithm(JWSAlgorithm.RS256)
            .keyID("keyId");
    return new JWKSet(builder.build());
  }
}
