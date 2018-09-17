package net.hackbee.srrmq.producerreactive;

import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;


import reactor.core.publisher.Mono;

@Component
public class RabbitReactiveTemplate {

  Mono<LoadPacketStatus> process(LoadPacket packet) {
    return Mono.just(LoadPacketStatus.builder()
        .loadPacket(packet)
        .isSuccess(true)
        .sendTime(ZonedDateTime.now())
        .build());
  }

}
