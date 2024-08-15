package com.jzel.cloudstorageprobe.business;

import static java.time.Duration.ofSeconds;
import static java.util.Objects.requireNonNull;
import static org.openqa.selenium.By.className;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProbeService {

  public static final int DL_URL_DELAY_S = 20;
  private final EmailSenderService emailSenderService;
  private final WebDriver driver;
  private final OkHttpClient client = new OkHttpClient();
  private final MessageDigest digest;

  @Scheduled(cron = "0 0 1 * * *")
  public void probe() {
    driver.get("https://dropxcloud.com/3wGCmudizbs3Aah/file");
    System.out.println("Waiting for " + DL_URL_DELAY_S + "s on page " + driver.getTitle() + " for the download URL...");
    driver.manage().timeouts().implicitlyWait(ofSeconds(DL_URL_DELAY_S));
    WebElement downloadButton = driver.findElement(className("download-link"));

    String downloadUrl = downloadButton.getAttribute("href");
    System.out.println("Download URL: " + downloadUrl);
    try {
      URI uri = new URI(downloadUrl);
      Request request = new Request.Builder().url(uri.toURL()).build();
      try (Response response = client.newCall(request).execute()) {
        if (response.isSuccessful()) {
          ResponseBody body = response.body();
          long contentLength = requireNonNull(body).contentLength();

          try (InputStream in = body.byteStream();
              OutputStream out = new FileOutputStream("C:\\tmp\\tmp")) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;

            while ((bytesRead = in.read(buffer)) != -1) {
              digest.update(buffer, 0, bytesRead);
              out.write(buffer, 0, bytesRead);
              totalBytesRead += bytesRead;
              if (contentLength > 0) {
                double progress = (totalBytesRead * 100.0) / contentLength;
                System.out.printf("\rDownload progress: %.2f%%", progress);
              }
            }
          }

          byte[] hashBytes = digest.digest();
          StringBuilder hashString = new StringBuilder();
          for (byte b : hashBytes) {
            hashString.append(String.format("%02x", b));
          }

          System.out.println("\nSHA-256 Hashwert: " + hashString);

          if (!hashString.toString().equals("2ee1bf80584cc589e6dee93fd5ef52eb7520aca17b5c78d83cd2f8d1186af219")) {
            emailSenderService.sendWrongDataAlert();
          }
        } else {
          emailSenderService.sendFailedDownloadAlert(
              "Unexpected response: " + response.code() + " " + response.message()
          );
        }
      } catch (final IOException e) {
        emailSenderService.sendFailedDownloadAlert(e.getMessage());
      }
    } catch (final URISyntaxException | MalformedURLException e) {
      emailSenderService.sendWrongUrlAlert(downloadUrl);
    }
  }
}