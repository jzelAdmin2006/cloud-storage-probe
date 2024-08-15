package com.jzel.cloudstorageprobe;

import com.jzel.cloudstorageprobe.business.ProbeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CloudStorageProbeApplication {

  public static void main(String[] args) {
    final ConfigurableApplicationContext context = SpringApplication.run(CloudStorageProbeApplication.class, args);
    context.getBean(ProbeService.class).probe();
  }

}
