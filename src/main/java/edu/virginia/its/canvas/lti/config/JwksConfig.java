package edu.virginia.its.canvas.lti.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.pem.PemContent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwksConfig {

  /**
   * The tool's RSA key pair, built from the private key in ltitool.privateKey. The private half
   * signs messages we send to Canvas; the public half is published at /.well-known/jwks.json so
   * Canvas can verify them.
   */
  @Bean
  public RSAKey toolKeyPair(@Value("${ltitool.privateKey}") String privateKeyPem) {
    try {
      PemContent pemContent = Objects.requireNonNull(PemContent.of(privateKeyPem));
      RSAPrivateCrtKey privateKey =
          (RSAPrivateCrtKey) Objects.requireNonNull(pemContent.getPrivateKey());
      // A RSA private key contains the values needed to rebuild its public key
      RSAPublicKey publicKey =
          (RSAPublicKey)
              KeyFactory.getInstance("RSA")
                  .generatePublic(
                      new RSAPublicKeySpec(
                          privateKey.getModulus(), privateKey.getPublicExponent()));

      return new RSAKey.Builder(publicKey)
          .privateKey(privateKey)
          .keyUse(KeyUse.SIGNATURE)
          .algorithm(JWSAlgorithm.RS256)
          .keyIDFromThumbprint()
          .build();
    } catch (Exception e) {
      throw new IllegalStateException("Invalid ltitool.privateKey value", e);
    }
  }

  @Bean
  public JWKSet jwkSet(RSAKey toolKeyPair) {
    return new JWKSet(toolKeyPair);
  }
}
