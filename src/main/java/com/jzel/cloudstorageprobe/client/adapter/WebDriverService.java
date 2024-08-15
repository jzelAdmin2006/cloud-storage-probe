package com.jzel.cloudstorageprobe.client.adapter;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PreDestroy;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
class WebDriverService {

  private final WebDriver driver = initDriver();

  @Bean
  WebDriver getDriver() {
    return driver;
  }

  private ChromeDriver initDriver() {
    WebDriverManager.chromedriver().setup();
    return new ChromeDriver(getChromeOptions());
  }

  private @NotNull ChromeOptions getChromeOptions() {
    final ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    options.addArguments("--disable-gpu");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    return options;
  }

  @PreDestroy
  void cleanup() {
    this.driver.quit();
  }
}
