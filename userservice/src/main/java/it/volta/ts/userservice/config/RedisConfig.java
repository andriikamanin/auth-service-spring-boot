// RedisConfig.java
package it.volta.ts.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import it.volta.ts.userservice.dto.UserProfileDto;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, UserProfileDto> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, UserProfileDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
