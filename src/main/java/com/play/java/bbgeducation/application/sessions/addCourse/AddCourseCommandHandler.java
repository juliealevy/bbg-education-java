package com.play.java.bbgeducation.application.sessions.addCourse;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.sessions.caching.SessionGetCacheManager;
import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.domain.sessioncourse.SessionCourseEntity;
import com.play.java.bbgeducation.infrastructure.repositories.CourseRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionCourseRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddCourseCommandHandler implements Command.Handler<AddCourseCommand, OneOf2<Success, NotFound>> {

    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;
    private final SessionCourseRepository sessionCourseRepository;

    private final SessionGetCacheManager cacheManager;

    public AddCourseCommandHandler(SessionRepository sessionRepository, CourseRepository courseRepository, SessionCourseRepository sessionCourseRepository, SessionGetCacheManager cacheManager) {
        this.sessionRepository = sessionRepository;
        this.courseRepository = courseRepository;
        this.sessionCourseRepository = sessionCourseRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    public OneOf2<Success, NotFound> handle(AddCourseCommand command) {
        //fetch the session
        Optional<SessionEntity> session = sessionRepository.getByProgramIdAndId(command.getProgramId(), command.getSessionId());
        if (session.isEmpty()){
            return OneOf2.fromOption2(new NotFound());
        }

        Optional<CourseEntity> course = courseRepository.findById(command.getCourseId());
        if (course.isEmpty()){
            return OneOf2.fromOption2(new NotFound());
        }

        Optional<SessionCourseEntity> sessionCourse = sessionCourseRepository.getBySessionIdAndCourseId(command.getSessionId(), command.getCourseId());

        if (sessionCourse.isPresent()){
            return OneOf2.fromOption1(new Success());
        }

        SessionCourseEntity newSessionCourse = SessionCourseEntity.create(session.get(), course.get());
        SessionCourseEntity saved = sessionCourseRepository.save(newSessionCourse);
        //clear cache so gets fetched on next session get
        cacheManager.clearSession(command.getProgramId(), command.getSessionId());
        return OneOf2.fromOption1(new Success());

    }
}
