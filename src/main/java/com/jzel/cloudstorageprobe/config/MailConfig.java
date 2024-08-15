package com.jzel.cloudstorageprobe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail")
public record MailConfig(String username, String to) {

}
