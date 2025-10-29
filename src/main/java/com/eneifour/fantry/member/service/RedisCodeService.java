package com.eneifour.fantry.member.service;

import com.eneifour.fantry.member.dto.AuthCodeDTO;
import com.eneifour.fantry.member.dto.MemberDTO;
import com.eneifour.fantry.security.exception.AuthErrorCode;
import com.eneifour.fantry.security.exception.AuthException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

//인증 코드 발급 및 검증하는 서비스
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCodeService {
    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;
    private final SecureRandom random = new SecureRandom();

    //TTL(Time To Live) 설정
    private static final Duration PENDING_TTL = Duration.ofMinutes(5);      //5분

    //키 설계
    private String pendingKey(String email) { return "pending:" + email; }
    private String codeKey(String code) { return "code:" + code; }

    //이메일 인증 코드 생성, 6자리
    public String generateCode6(){
        return String.format("%06d", random.nextInt(1_000_000));
    }

    //임시 키 Redis에 저장
    public String savePending(MemberDTO memberDTO){
        //json 파싱
        String existingJson = (String) redis.opsForValue().get("pending:" + memberDTO.getEmail());
        try{
            //기존 인증 정보 삭제(재전송 대비)
            if(existingJson != null){
                MemberDTO existingMemberDTO = objectMapper.readValue(existingJson, MemberDTO.class);
                String oldCode = existingMemberDTO.getMailCode();

                //기존 키 삭제
                if(oldCode != null) redis.delete(codeKey(oldCode));
                redis.delete(pendingKey(memberDTO.getEmail()));
            }

            String code = generateCode6();
            memberDTO.setMailCode(code);

            String json = objectMapper.writeValueAsString(memberDTO);

            //redis에 저장
            redis.opsForValue().set(pendingKey(memberDTO.getEmail()), json, PENDING_TTL);
            redis.opsForValue().set(codeKey(memberDTO.getMailCode()), memberDTO.getEmail(), PENDING_TTL);

            log.debug("Redis에 등록된 이메일 인증 코드: {} ", code);

            return code;

        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    //인증 코드 검증
    public void verifyCode6(AuthCodeDTO authCodeDTO) throws AuthException{
        String storedEmail = redis.opsForValue().get(codeKey(authCodeDTO.getCode()));
        if(storedEmail == null){
            throw new AuthException(AuthErrorCode.AUTH_CODE_NOT_FOUND);
        }
        if(!storedEmail.equals(authCodeDTO.getEmail())){
            throw new AuthException(AuthErrorCode.AUTH_EMAIL_MISMATCH);
        }
        redis.delete(pendingKey(authCodeDTO.getEmail()));
        redis.delete(codeKey(authCodeDTO.getCode()));
    }

    //TTL 전달
    public long getRemainingTTL(String email){
        Long ttl = redis.getExpire(pendingKey(email), TimeUnit.SECONDS);
        return ttl != null ? ttl : 0;
    }
}
