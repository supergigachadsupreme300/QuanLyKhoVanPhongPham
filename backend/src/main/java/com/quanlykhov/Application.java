package com.quanlykhov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Đánh dấu đây là lớp chính dùng để khởi động ứng dụng Spring Boot
@SpringBootApplication
public class Application {

    // Hàm main khởi chạy máy chủ backend
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
