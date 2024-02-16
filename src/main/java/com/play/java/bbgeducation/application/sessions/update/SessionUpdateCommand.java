package com.play.java.bbgeducation.application.sessions.update;

import an.awesome.pipelinr.Command;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionUpdateCommand implements Command<OneOf3<Success, NotFound, ValidationFailed>> {
    //no updating program in this command... that will be a special call..at least for now
    private Long programId;
    private Long id;
    private String name;
    private String description;

    @JsonFormat(pattern="MM-dd-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern="MM-dd-yyyy")
    private LocalDate endDate;

    private Integer practicumHours;

}
