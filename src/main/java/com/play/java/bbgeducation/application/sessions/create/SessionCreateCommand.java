package com.play.java.bbgeducation.application.sessions.create;

import an.awesome.pipelinr.Command;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.play.java.bbgeducation.application.common.commands.EntityCommand;
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
@NoArgsConstructor
public class SessionCreateCommand extends EntityCommand implements Command<OneOf3<SessionResult, NotFound, ValidationFailed>> {
    @JsonFormat(pattern="MM-dd-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern="MM-dd-yyyy")
    private LocalDate endDate;
    private int practicumHours;
    private Long programId;

    @Builder
    public SessionCreateCommand(String name, String description, LocalDate startDate, LocalDate endDate,
                                int practicumHours, Long programId){

        super(name,description);
        this.startDate = startDate;
        this.endDate = endDate;
        this.practicumHours = practicumHours;
        this.programId = programId;


    }
}
