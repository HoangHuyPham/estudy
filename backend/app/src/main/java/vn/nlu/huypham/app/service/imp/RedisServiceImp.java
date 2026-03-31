package vn.nlu.huypham.app.service.imp;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.service.RedisService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class RedisServiceImp implements RedisService {

    final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean checkATBlackList(UUID jti) {
        return stringRedisTemplate.hasKey("at-blacklist:" + jti); 
    }

    @Override
    public void addATBlacklist(UUID jti, Instant expiredAt) throws RedisException {
        try {
            stringRedisTemplate.opsForValue().set(
                    "at-blacklist:" + jti,
                    "1",
                    Duration.between(Instant.now(), expiredAt).plusSeconds(1));
        } catch (Exception e) {
            throw Errors.REDIS_ERROR;
        }
    }

}
