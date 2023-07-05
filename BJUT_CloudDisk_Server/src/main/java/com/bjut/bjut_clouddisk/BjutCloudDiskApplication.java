package com.bjut.bjut_clouddisk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan
public class BjutCloudDiskApplication {

  public static void main(String[] args) {
    SpringApplication.run(BjutCloudDiskApplication.class, args);
  }

}
