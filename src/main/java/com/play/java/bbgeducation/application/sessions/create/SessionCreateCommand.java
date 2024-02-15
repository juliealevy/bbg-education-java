package com.play.java.bbgeducation.application.sessions.create;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionCreateCommand implements Command<OneOf3<SessionResult, NotFound, ValidationFailed>> {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int practicumHours;
    private Long programId;

}
