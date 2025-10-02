package com.eneifour.fantry.security.model;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

//토큰 상태 관리 서비스
@Service
public class RedisTokenService {
    private final StringRedisTemplate redis;

    public RedisTokenService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    //사용자 버전 조회
    public int currentUserVersion(String userId){
        String ver = redis.opsForValue().get("uv:"+userId);
        return ver==null?0:Integer.parseInt(ver);
    }

    //AccssToken 블랙리스트 등록
    public void saveBlackList(String tokenId, String token, long ttlSeconds){
        if(ttlSeconds<=0) return;
        redis.opsForValue().set("bl:access:"+tokenId, token, Duration.ofSeconds(ttlSeconds));
    }

    //AccessToken 블랙리스트 조회
    public boolean isBlackList(String tokenId){
        return redis.hasKey("bl:access:"+tokenId);
    }

    //refresh 토큰 저장하기
    public void saveRefreshToken(String userId, String refreshToken, long ttlSeconds){
        redis.opsForValue().set("rt:" + userId, refreshToken, Duration.ofSeconds(ttlSeconds));
    }

    //refreshToken 존재 여부 확인
    public boolean isExistRefreshToken(String userId){
        return redis.hasKey("rt:" + userId);
    }

    //refreshToken 일치 여부 확인
    public boolean matchesRefreshToken(String userId, String refreshToken){
        String userValue = redis.opsForValue().get("rt:" + userId);
        return userValue != null && refreshToken.equals(userValue);
    }

    //특정 refreshToken 지우기
    public void deleteRefreshToken(String userId){
        redis.delete("rt:"+userId);
    }
}
