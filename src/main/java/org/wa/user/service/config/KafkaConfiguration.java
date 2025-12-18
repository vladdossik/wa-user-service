package org.wa.user.service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import org.wa.user.service.dto.user.UserRegisteredDto;
import org.wa.user.service.exception.AttributeDuplicateException;
import org.wa.user.service.exception.UserProfileAlreadyExistsException;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfiguration {

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, UserRegisteredDto> kafkaListenerContainerFactory(
            ConsumerFactory<String, UserRegisteredDto> consumerFactory,
            DefaultErrorHandler errorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, UserRegisteredDto> kafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        kafkaListenerContainerFactory.setCommonErrorHandler(errorHandler);
        return kafkaListenerContainerFactory;
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(new FixedBackOff(1000L, 3));

        errorHandler.addNotRetryableExceptions(
                AttributeDuplicateException.class,
                UserProfileAlreadyExistsException.class
        );
        return errorHandler;
    }
}
