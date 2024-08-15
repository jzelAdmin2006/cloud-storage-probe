package com.jzel.cloudstorageprobe.business;

import com.jzel.cloudstorageprobe.config.MailConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

  private final JavaMailSender mailSender;
  private final MailConfig mailConfig;

  public void sendWrongDataAlert() {
    send("\uD83D\uDE40 Nicht gut!",
        """
            Hash ist nicht korrekt
            """);
  }

  private void send(String subject, String text) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom(mailConfig.username());
    msg.setTo(mailConfig.to());
    msg.setText(text);
    msg.setSubject(subject);

    mailSender.send(msg);
  }

  public void sendWrongUrlAlert(String downloadUrl) {
  }

  public void sendFailedDownloadAlert(String s) {
  }
}
