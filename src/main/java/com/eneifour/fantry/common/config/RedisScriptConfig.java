package com.eneifour.fantry.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class RedisScriptConfig {

    @Bean
    public RedisScript<String> placeBidScript(){

        // Lua 스크립트 파일을 로드.
        ClassPathResource scriptResource = new ClassPathResource("scripts/place-bid.lua");

        // DefaultRedisScript를 생성.
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(scriptResource));

        // 스크립트의 반환 타입을 설정. (Lua 스크립트의 return 타입과 일치)
        redisScript.setResultType(String.class);

        return redisScript;

    }


}
