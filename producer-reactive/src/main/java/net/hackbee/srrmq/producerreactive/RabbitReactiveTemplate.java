package net.hackbee.srrmq.producerreactive;

import java.time.ZonedDateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import reactor.core.publisher.Flux;

@Component
public class RabbitReactiveTemplate {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  Flux<LoadPacketStatus> process(LoadPacket packet) {
    rabbitTemplate.convertAndSend("", "",
        LoadPacketStatus.builder()
            .loadPacket(packet)
            .isSuccess(true)
            .sendTime(ZonedDateTime.now())
            .build()
    );
    return Flux.just();
  }

}
