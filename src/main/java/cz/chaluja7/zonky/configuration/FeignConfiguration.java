package cz.chaluja7.zonky.configuration;

import cz.chaluja7.zonky.configuration.properties.ZonkyApiProperties;
import feign.RequestInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import java.time.format.DateTimeFormatter;

@Configuration
@EnableFeignClients(basePackages = "cz.chaluja7.zonky.marketplace.remote.client")
@EnableConfigurationProperties(ZonkyApiProperties.class)
public class FeignConfiguration {

    @Bean
    public RequestInterceptor marketplaceRequestInterceptor(ZonkyApiProperties properties) {
        return new MarketplaceRequestInterceptor(properties.getUserAgent());
    }

    @Bean
    public FeignFormatterRegistrar localDateFeignFormatterRegistrar() {
        return formatterRegistry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setDateTimeFormatter(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            registrar.registerFormatters(formatterRegistry);
        };
    }

    // hystrix fallback should be implemented in the real environment, or at least simple retryer

}
