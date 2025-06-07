package it.volta.ts.userservice.config;

import it.volta.ts.userservice.dto.UserEmailChangedEvent;
import it.volta.ts.userservice.dto.UserLoginEvent;
import it.volta.ts.userservice.dto.UserRegisteredEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private Map<String, Object> baseConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "user-service");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return config;
    }

    // ======== UserRegisteredEvent =========
    @Bean
    public ConsumerFactory<String, UserRegisteredEvent> userRegisteredConsumerFactory() {
        Map<String, Object> config = baseConfig();
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserRegisteredEvent.class.getName());

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(UserRegisteredEvent.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserRegisteredEvent> userRegisteredListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, UserRegisteredEvent>();
        factory.setConsumerFactory(userRegisteredConsumerFactory());
        return factory;
    }

    // ======== UserLoginEvent =========
    @Bean
    public ConsumerFactory<String, UserLoginEvent> userLoginConsumerFactory() {
        Map<String, Object> config = baseConfig();
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserLoginEvent.class.getName());

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(UserLoginEvent.class, false)
        );
    }

    // ======== UserEmailChangedEvent =========
    @Bean
    public ConsumerFactory<String, UserEmailChangedEvent> userEmailChangedConsumerFactory() {
        Map<String, Object> config = baseConfig();
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserEmailChangedEvent.class.getName());

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(UserEmailChangedEvent.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserEmailChangedEvent> userEmailChangedListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, UserEmailChangedEvent>();
        factory.setConsumerFactory(userEmailChangedConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserLoginEvent> userLoginListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, UserLoginEvent>();
        factory.setConsumerFactory(userLoginConsumerFactory());
        return factory;
    }
}