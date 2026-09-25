package edu.virginia.its.canvas.lti.controller;

import com.nimbusds.jose.jwk.JWKSet;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwksController {

  private final JWKSet jwkSet;

  @GetMapping(value = "/.well-known/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> getJwks() {
    return jwkSet.toPublicJWKSet().toJSONObject();
  }
}
