package com.reservation.common.EmailService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  //    private final JavaMailSender mailSender;

  public void sendEmail(String to, String subject, String content) {
    //        try {
    //            MimeMessage message = mailSender.createMimeMessage();
    //            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    //            helper.setTo(to);
    //            helper.setSubject(subject);
    //            helper.setText(content, true);
    //
    //            mailSender.send(message);
    //        } catch (MessagingException e) {
    //            throw new RuntimeException("이메일 전송 실패", e);
    //        }
  }
}
