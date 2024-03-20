package com.play.java.bbgeducation.api.auth;

import com.play.java.bbgeducation.application.common.CloneableData;
import com.play.java.bbgeducation.application.common.logging.scrubbing.HasScrubOnLog;
import com.play.java.bbgeducation.application.common.logging.scrubbing.ScrubOnLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@HasScrubOnLog
public class UpdatePasswordRequest extends CloneableData {
    @ScrubOnLog
    private String oldPassword;
    @ScrubOnLog
    private String newPassword;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return UpdatePasswordRequest.builder()
                .oldPassword(this.oldPassword)
                .newPassword(this.newPassword)
                .build();
    }
}
