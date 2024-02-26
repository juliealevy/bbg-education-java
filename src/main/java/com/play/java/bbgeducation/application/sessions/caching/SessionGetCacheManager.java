package com.play.java.bbgeducation.application.sessions.caching;

import com.play.java.bbgeducation.application.sessions.result.SessionResult;

import java.util.Optional;

public interface SessionGetCacheManager extends SessionCacheManager {
    Optional<SessionResult> getSession(Long programId, Long sessionId);
    int getSessionCount();
}
