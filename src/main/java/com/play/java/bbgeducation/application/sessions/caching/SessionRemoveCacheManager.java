package com.play.java.bbgeducation.application.sessions.caching;

public interface SessionRemoveCacheManager {
    void removeSession(Long programId, Long sessionId);

    void removeAll();
}
