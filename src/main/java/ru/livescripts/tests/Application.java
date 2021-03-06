package ru.livescripts.tests;

import com.fasterxml.jackson.databind.Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.zalando.jackson.datatype.money.MoneyModule;
import ru.livescripts.tests.javamoney.formatter.MonetaryAmountFormatFactory;

@SpringBootApplication
public class Application {

    //https://spring.io/blog/2014/12/02/latest-jackson-integration-improvements-in-spring#with-spring-boot
    @Bean
    public Module jacksonDatatypeMoneyModule() {
        return new MoneyModule().withFormatting(new MonetaryAmountFormatFactory());
    }

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
