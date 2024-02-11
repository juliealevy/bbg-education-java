package com.play.java.bbgeducation.application.common.logging.scrubbers;

import com.play.java.bbgeducation.application.auth.AuthenticationResult;
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
       if (body.getContent() instanceof AuthenticationResult){
           return scrubAuthResultToken(body);
       }
       return body;
    }

    private EntityModel<AuthenticationResult> scrubAuthResultToken(EntityModel<AuthenticationResult> body){
        AuthenticationResult clonedContent = AuthenticationResult.builder()
                .accessToken("")
                .refreshToken("")
                .build();
        EntityModel<AuthenticationResult> scrubbed = EntityModel.of(clonedContent);
        scrubbed.add(body.getLinks());

        return scrubbed;
    }

}
