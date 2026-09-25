package edu.virginia.its.canvas.lti.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class JwksConfigTest {

  // TEST-ONLY 512-bit key
  private static final String TEST_KEY =
      """
    -----BEGIN PRIVATE KEY-----
    MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEA21FlBdP3M61vE8h0
    9r4+c/GFHsPWDERrZxrtDaM2/ZX0ko1qaEAZgL0x6gXTP0nNTQscf57Td50ZOnrb
    lsU9pQIDAQABAkAzft0VjTuR6rsWDg9IkErhtmfnXVw47Se6wdu/Q/95cvjVmUHd
    6Z4SgtxHb/8yHYKu6jeeEKKwDf8LkCseWKSBAiEA+keTPgI13WW6XHTokOA4F7NN
    UcoWfKiypuT9BQOzcgkCIQDgVKh28XYJ3JN0/rm66pphWNg8GQOAhU4lg8DwFgjl
    vQIgcwSNsEOHyZVEjdTURDAm1w15jQrYRrWoVHaLqNBImTECIGtBTsJ/s/YHFiFr
    4JUzsdg1SD2DoB8EnVZmZgrkHiDpAiBlHB+atwhAyhcHWT7aEZorLc//99XZKi6D
    FGnN18ETCQ==
    -----END PRIVATE KEY-----
  """;

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner().withUserConfiguration(JwksConfig.class);

  @Test
  void failsToStartWhenKeyIsMissing() {
    runner.run(
        context -> {
          assertThat(context).hasFailed();
          assertThat(context.getStartupFailure()).hasStackTraceContaining("ltitool.privateKey");
        });
  }

  @Test
  void failsToStartWhenKeyIsInvalid() {
    runner
        .withPropertyValues("ltitool.privateKey=not a key")
        .run(
            context -> {
              assertThat(context).hasFailed();
              assertThat(context.getStartupFailure())
                  .hasStackTraceContaining("Invalid ltitool.privateKey");
            });
  }

  @Test
  void startsAndPublishesOnlyThePublicKeyWhenKeyIsValid() {
    runner
        .withPropertyValues("ltitool.privateKey=" + TEST_KEY)
        .run(
            context -> {
              assertThat(context).hasNotFailed();
              assertThat(context.getBean(RSAKey.class).isPrivate()).isTrue();

              JWKSet publicSet = context.getBean(JWKSet.class).toPublicJWKSet();
              assertThat(publicSet.getKeys()).hasSize(1);
              assertThat(publicSet.getKeys().getFirst().isPrivate()).isFalse();
            });
  }
}
