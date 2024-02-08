package com.play.java.bbgeducation.application.programs.getById;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramGetByIdCommand implements Command<OneOf2<ProgramResult, NotFound>> {
    private Long id;
}
