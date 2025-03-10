package com.reservation.common.config;

import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

// @Configuration
@RequiredArgsConstructor
public class MailConfig {

  @Value("${mail.host}")
  private String mailHost;

  @Value("${mail.port}")
  private int mailPort;

  @Value("${mail.username}")
  private String mailUsername;

  @Value("${mail.password}")
  private String mailPassword;

  @Value("${mail.properties.mail.smtp.auth}")
  private String smtpAuth;

  @Value("${mail.properties.mail.smtp.starttls.enable}")
  private String starttlsEnable;

  @Value("${mail.transport.protocol}")
  private String mailProtocol;

  @Value("${mail.debug}")
  private String mailDebug;

  @Bean
  public JavaMailSender mailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(mailHost);
    mailSender.setPort(mailPort);
    mailSender.setUsername(mailUsername);
    mailSender.setPassword(mailPassword);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", mailProtocol);
    props.put("mail.smtp.auth", smtpAuth);
    props.put("mail.smtp.starttls.enable", starttlsEnable);
    props.put("mail.debug", mailDebug);

    return mailSender;
  }
}
