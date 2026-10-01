package com.ducanh.unievent.security.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ducanh.unievent.common.service.RedisService;
import com.ducanh.unievent.common.util.Sha256Util;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${jwt.refresh-duration}")
    private Long refreshDuration;

    private static String TOKEN_KEY_PREFIX = "auth:refresh:token:";
    private static String FAMILY_KEY_PREFIX = "auth:refresh:family:";

    private final String FIELD_USER_ID = "userId";
    private final String FIELD_FAMILY_ID = "familyId";
    private final String FIELD_REVOKED = "revoked";
    private final String FIELD_STATUS = "status";

    private final String STATUS_ACTIVE = "ACTIVE";
    private final String STATUS_REVOKED = "REVOKED";

    private final RedisService redisService;

    public String createRefreshToken(Long userId) {

        String rawToken = UUID.randomUUID().toString();
        String tokenHash = Sha256Util.sha256(rawToken);

        String familyId = UUID.randomUUID().toString();

        String tokenKey = buildTokenKey(tokenHash);
        String familyKey = buildFamilyKey(familyId);

        redisService.putHash(tokenKey, FIELD_USER_ID, userId.toString());
        redisService.putHash(tokenKey, FIELD_FAMILY_ID, familyId);
        redisService.putHash(tokenKey, FIELD_REVOKED, "false");
        redisService.expire(tokenKey, refreshDuration, TimeUnit.MILLISECONDS);

        redisService.putHash(familyKey, FIELD_STATUS, STATUS_ACTIVE);
        redisService.expire(familyKey, refreshDuration, TimeUnit.MILLISECONDS);

        return rawToken;
    }

    public Long verifyAndGetUserId(String rawToken) {

        String tokenHash = Sha256Util.sha256(rawToken);
        String tokenKey = buildTokenKey(tokenHash);

        if (Boolean.FALSE.equals(redisService.hasKey(tokenKey))) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        String revoked = redisService.getHash(tokenKey, FIELD_REVOKED);
        String familyId = redisService.getHash(tokenKey, FIELD_FAMILY_ID);
        String familyStatus = redisService.getHash(buildFamilyKey(familyId), FIELD_STATUS);
        String userId = redisService.getHash(tokenKey, FIELD_USER_ID);

        if ("true".equals(revoked)) {
            revokeFamily(familyId);
            throw new ApiException(ErrorCode.REFRESH_TOKEN_REUSE_DETECTED);
        }

        if (STATUS_REVOKED.equals(familyStatus)) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_REVOKED);
        }

        return Long.valueOf(userId);
    }

    public void revokeToken(String rawToken) {

        String tokenHash = Sha256Util.sha256(rawToken);
        String tokenKey = buildTokenKey(tokenHash);

        if (Boolean.FALSE.equals(redisService.hasKey(tokenKey))) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        redisService.putHash(tokenKey, FIELD_REVOKED, "true");
    }

    public String rotateRefreshToken(String oldRawToken, Long userId) {

        String oldTokenHash = Sha256Util.sha256(oldRawToken);
        String oldTokenKey = buildTokenKey(oldTokenHash);

        if (Boolean.FALSE.equals(redisService.hasKey(oldTokenKey))) {
            log.error("dong 1");
            throw new ApiException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        String revoked = redisService.getHash(oldTokenKey, FIELD_REVOKED);
        String familyId = redisService.getHash(oldTokenKey, FIELD_FAMILY_ID);
        String familyStatus = redisService.getHash(buildFamilyKey(familyId), FIELD_STATUS);

        if ("true".equals(revoked)) {
            revokeFamily(familyId);
            throw new ApiException(ErrorCode.REFRESH_TOKEN_REUSE_DETECTED);
        }

        if (STATUS_REVOKED.equals(familyStatus)) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_REVOKED);
        }

        redisService.putHash(oldTokenKey, FIELD_REVOKED, "true");

        String newRawToken = UUID.randomUUID().toString();
        String newTokenHash = Sha256Util.sha256(newRawToken);
        String newTokenKey = buildTokenKey(newTokenHash);

        redisService.putHash(newTokenKey, FIELD_USER_ID, userId.toString());
        redisService.putHash(newTokenKey, FIELD_FAMILY_ID, familyId);
        redisService.putHash(newTokenKey, FIELD_REVOKED, "false");
        redisService.expire(newTokenKey, refreshDuration, TimeUnit.MILLISECONDS);

        redisService.expire(buildFamilyKey(familyId), refreshDuration, TimeUnit.MILLISECONDS);

        return newRawToken;
    }

    public void revokeFamily(String familyId) {
        if (familyId == null) {
            return;
        }

        String familyKey = buildFamilyKey(familyId);

        if (Boolean.FALSE.equals(redisService.hasKey(familyKey))) {
            return;
        }

        redisService.putHash(familyKey, FIELD_STATUS, STATUS_REVOKED);
    }

    private String buildTokenKey(String tokenHash) {
        return TOKEN_KEY_PREFIX + tokenHash;
    }

    private String buildFamilyKey(String familyId) {
        return FAMILY_KEY_PREFIX + familyId;
    }
}
