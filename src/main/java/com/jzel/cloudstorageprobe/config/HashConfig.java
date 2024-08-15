package com.jzel.cloudstorageprobe.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HashConfig {

  private static final String SHA_256 = "SHA-256";

  @Bean
  MessageDigest getMessageDigest() {
    try {
      return MessageDigest.getInstance(SHA_256);
    } catch (final NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }
}
