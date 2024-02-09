package com.play.java.bbgeducation.application.common.logging.scrubbers;

import com.play.java.bbgeducation.application.auth.LoginResult;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

/*******************************
 *
 * Can't get EntityModel generic type of its content to match to the scrubber
 * so creating one for all EntityModels and checking content to implement
 * data specific scrubbing
 */
@Component
public class EntityModelResponseScrubber implements LoggingBodyScrubber<EntityModel>
{
    public EntityModel scrub(EntityModel body){
       if (body.getContent() instanceof LoginResult){
           return scrubLoginResultToken(body);
       }
       return body;
    }

    private EntityModel<LoginResult> scrubLoginResultToken(EntityModel<LoginResult> body){
        LoginResult clonedContent = LoginResult.builder()
                .accessToken("")
                .refreshToken("")
                .build();
        EntityModel<LoginResult> scrubbed = EntityModel.of(clonedContent);
        scrubbed.add(body.getLinks());

        return scrubbed;
    }

}
