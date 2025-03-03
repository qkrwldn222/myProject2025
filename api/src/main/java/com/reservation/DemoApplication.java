package com.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
// @ComponentScan(basePackages = "com.reservation.infrastructure.*")
// @MapperScan(basePackages = "com.reservation.infrastructure.*.repository")
// @EnableMapRepositories(basePackages = "com.reservation.infrastructure.*")
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }
}
