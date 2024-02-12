package com.play.java.bbgeducation.application.common.logging.scrubbing;

import com.play.java.bbgeducation.application.common.CloneableData;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class LoggingDataScrubberService {
    public Object scrubBody(Object body) {
        boolean isEntityModel = body instanceof EntityModel;

        Pair<EntityModel, Object> objectToScrub = getObjectToScrub(body, isEntityModel);

        EntityModel entityModelBody = objectToScrub.getLeft();
        Object bodyToScrub = objectToScrub.getRight();

        if (!canScrub(bodyToScrub)) return body;

        try {
            final Object scrubbedBody = cloneAndScrub(bodyToScrub);
            if (isEntityModel) {
                return buildEntityModelReturn(entityModelBody, scrubbedBody);
            } else {
                return scrubbedBody;
            }
        } catch (CloneNotSupportedException ex) {
            return body;
        }
    }

    private boolean canScrub(Object bodyToScrub) {
        return (bodyToScrub instanceof CloneableData) &&
                (bodyToScrub.getClass().isAnnotationPresent(HasScrubOnLog.class));
    }

    private Pair<EntityModel, Object> getObjectToScrub(Object body, boolean isEntityModel){
        if (isEntityModel && ((EntityModel) body).getContent() instanceof CloneableData) {
            EntityModel entityModelBody = ((EntityModel) body);
            return Pair.of(entityModelBody, entityModelBody.getContent());
        } else {
            return Pair.of(null, body);
        }
    }

    private Object cloneAndScrub(Object bodyToScrub) throws CloneNotSupportedException {
        final Object bodyClone = ((CloneableData) bodyToScrub).clone();
        PropertyAccessor accessor = PropertyAccessorFactory.forBeanPropertyAccess(bodyClone);
        Arrays.stream(bodyClone.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ScrubOnLog.class))
                .forEach(f -> {
                    if (f.getType() == String.class) {
                        accessor.setPropertyValue(f.getName(), "");
                    }
                });
        return bodyClone;
    }

    private EntityModel buildEntityModelReturn(EntityModel originalBody, Object scrubbedBody){
        Class<?> contentClass = originalBody.getContent().getClass();
        return EntityModel.of(contentClass.cast(scrubbedBody))
                .add(originalBody.getLinks());
    }
}
