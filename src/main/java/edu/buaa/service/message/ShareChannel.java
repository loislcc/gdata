package edu.buaa.service.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ShareChannel {
    String CHANNELIN = "ShareInChannel";

    @Input(CHANNELIN)
    SubscribableChannel subscribableChannel();


}
