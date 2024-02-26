package com.play.java.bbgeducation.unit.application.sessions.cache;

import com.play.java.bbgeducation.application.sessions.caching.SessionCacheManager;
import com.play.java.bbgeducation.application.sessions.caching.SessionGetCacheManager;
import com.play.java.bbgeducation.application.sessions.caching.SessionRemoveCacheManager;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SessionCacheManagerTests {
    private SessionCacheManager cacheManager;
    private SessionRemoveCacheManager removeCacheManager;

    private SessionGetCacheManager getCacheManager;

    @Autowired
    public SessionCacheManagerTests(SessionCacheManager sessionCacheManager, SessionRemoveCacheManager removeCacheManager, SessionGetCacheManager getCacheManager) {
        this.cacheManager = sessionCacheManager;
        this.removeCacheManager = removeCacheManager;
        this.getCacheManager = getCacheManager;
    }

    @Test
    void cacheAndGetSession_ShouldStoreAndFetchSession_WhenNotNullAndKeyNotExists(){
        SessionResult sessionResult = Instancio.create(SessionResult.class);
        cacheManager.cacheSession(sessionResult);
        Optional<SessionResult> cachedSession = getCacheManager.getSession(sessionResult.getProgram().getId(), sessionResult.getId());

        assertThat(cachedSession).isPresent();
        assertThat(cachedSession.get()).isEqualTo(sessionResult);
    }

    @Test
    void cacheSession_ShouldThrowException_WhenNull(){
        SessionResult sessionResult = null;

        assertThatThrownBy(() -> cacheManager.cacheSession(sessionResult))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cacheSession_ShouldUpdateSession_WhenKeyExists(){
        SessionResult sessionResult = Instancio.create(SessionResult.class);
        String originalName = sessionResult.getName();
        cacheManager.cacheSession(sessionResult);
        Optional<SessionResult> cachedSession = getCacheManager.getSession(sessionResult.getProgram().getId(), sessionResult.getId());

        sessionResult.setName("Updated");
        cacheManager.cacheSession(sessionResult);
        Optional<SessionResult> cachedUpdatedSession = getCacheManager.getSession(sessionResult.getProgram().getId(), sessionResult.getId());

        assertThat(cachedSession).isPresent();
        assertThat(cachedSession.get().getName()).isEqualTo(originalName);
        assertThat(cachedUpdatedSession).isPresent();
        assertThat(cachedUpdatedSession.get().getName()).isEqualTo(sessionResult.getName());
    }

    @Test
    void getSession_ShouldReturnEmpty_WhenKeyNotExists(){
        Optional<SessionResult> cachedSession = getCacheManager.getSession(100L, 200L);

        assertThat(cachedSession).isEmpty();
    }

    @Test
    void removeSession_ShouldDeleteSession_WhenKeyExists(){
        SessionResult sessionResult = Instancio.create(SessionResult.class);
        cacheManager.cacheSession(sessionResult);
        Optional<SessionResult> cachedSession = getCacheManager.getSession(sessionResult.getProgram().getId(), sessionResult.getId());

        removeCacheManager.removeSession(sessionResult.getProgram().getId(), sessionResult.getId());
        Optional<SessionResult> fetchAfterRemove = getCacheManager.getSession(sessionResult.getProgram().getId(), sessionResult.getId());

        assertThat(cachedSession).isPresent();
        assertThat(cachedSession.get()).isEqualTo(sessionResult);
        assertThat(fetchAfterRemove).isEmpty();
    }

    @Test
    void removeAll_ShouldDeleteAllSessions(){
        SessionResult sessionResult = Instancio.create(SessionResult.class);
        cacheManager.cacheSession(sessionResult);
        Optional<SessionResult> cachedSession = getCacheManager.getSession(sessionResult.getProgram().getId(), sessionResult.getId());

        SessionResult sessionResult2 = Instancio.create(SessionResult.class);
        cacheManager.cacheSession(sessionResult2);
        Optional<SessionResult> cachedSession2 = getCacheManager.getSession(sessionResult.getProgram().getId(), sessionResult.getId());
        int sizeBefore = getCacheManager.getSessionCount();

        removeCacheManager.removeAll();

        int sizeAfter = getCacheManager.getSessionCount();

        assertThat(sizeBefore).isEqualTo(2);
        assertThat(sizeAfter).isEqualTo(0);
    }

    @AfterEach
    void tearDown() {
        removeCacheManager.removeAll();
    }
}
