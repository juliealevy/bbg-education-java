package com.play.java.bbgeducation.application.config;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipelineProvider {
    @Bean
    public Pipeline getPipeline(
            ObjectProvider<Command.Handler> commandHandlers,
            ObjectProvider<Notification.Handler> notificationHandlers,
            ObjectProvider<Command.Middleware> middlewares) {
        return new Pipelinr()
                .with(commandHandlers::stream) // Registers Handlers
                .with(notificationHandlers::stream) // Registers Notifications (not covered here)
                .with(middlewares::orderedStream); // Registers Middlewares (not covered here)
    }
}
