package edu.buaa.service.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ToConsoleChannel {
    String CHANNEL = "ToConsoleChannel";

    @Output(CHANNEL)
    MessageChannel messageChannel();
}
