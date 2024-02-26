package com.play.java.bbgeducation.application.sessions.caching;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import lombok.SneakyThrows;

import java.util.Optional;

public interface SessionCacheManager {
    void cacheSession(SessionResult session);
}
