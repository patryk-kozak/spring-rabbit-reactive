package net.hackbee.srrmq.producerreactive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import reactor.core.publisher.Flux;

@Service
public class ProducerService {

  @Autowired
  private RabbitReactiveTemplate rabbitReactiveTemplate;

  Flux<LoadPacketStatus> send(Integer num) {
    return Flux.range(0, num)
        .flatMap(id ->
            rabbitReactiveTemplate.process(LoadPacket.builder()
                .id(id.longValue())
                .data("data#" + id)
                .build())
        );
  }

}
