package com.play.java.bbgeducation.application.sessions.caching;

import com.play.java.bbgeducation.application.common.caching.RedisUtil;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class SessionCacheManagerImpl
        implements SessionCacheManager, SessionGetCacheManager, SessionRemoveCacheManager{

    private static final String SESSION = "SESSION_";
    private RedisUtil<SessionResult> redisUtilSession;

    public SessionCacheManagerImpl(RedisUtil<SessionResult> redisUtilSession) {
        this.redisUtilSession = redisUtilSession;
    }


    public Optional<SessionResult> getSession(Long programId, Long sessionId)  {
        String key = buildCacheKey(programId, sessionId);
        return redisUtilSession.getValue(key);
    }

    @Override
    public int getSessionCount() {
        return redisUtilSession.getCount();
    }


    public void cacheSession(SessionResult session) {
        if (session == null){
            throw new IllegalArgumentException("Cannot cache a null session");
        }
        redisUtilSession.putValueWithExpireTime(
                buildCacheKey(session.getProgram().getId(), session.getId()),
                session,
                1, TimeUnit.HOURS);
    }

    @Override
    public void removeSession(Long programId, Long sessionId) {
        redisUtilSession.deleteValue(buildCacheKey(programId, sessionId));
    }

    @Override
    public void removeAll() {
        redisUtilSession.clearAll();
    }

    private String buildCacheKey(Long programId, Long sessionId){
        return SESSION + programId + "_" + sessionId;
    }
}
