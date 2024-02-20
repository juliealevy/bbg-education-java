package com.play.java.bbgeducation.application.sessions.update;

import an.awesome.pipelinr.Command;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.play.java.bbgeducation.application.common.commands.EntityCommand;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SessionUpdateCommand extends EntityCommand
        implements Command<OneOf3<Success, NotFound, ValidationFailed>> {

    //no updating program in this command... that will be a special call..at least for now
    private Long programId;
    private Long id;
    @JsonFormat(pattern="MM-dd-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern="MM-dd-yyyy")
    private LocalDate endDate;
    private Integer practicumHours;

    @Builder
    public SessionUpdateCommand(Long id, String name, String description, LocalDate startDate, LocalDate endDate,
                                int practicumHours, Long programId){

        super(name,description);
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.practicumHours = practicumHours;
        this.programId = programId;


    }

}
