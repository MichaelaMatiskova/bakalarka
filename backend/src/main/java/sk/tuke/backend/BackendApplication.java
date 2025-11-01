package sk.tuke.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import sk.tuke.backend.service.*;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BackendApplication.class, args);
        System.out.println("Backend Application Started");

    }

    @Bean
    public CompetitorService competitorService() {
        return new CompetitorServiceJPA();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public QrCodesService  qrCodesService() {
        return new QrCodesServiceJPA();
    }
}
