package com.play.java.bbgeducation.api.auth;

import com.play.java.bbgeducation.application.common.logging.scrubbing.HasScrubOnLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@HasScrubOnLog
public class UpdateUserNameRequest {
    private String email;
}
