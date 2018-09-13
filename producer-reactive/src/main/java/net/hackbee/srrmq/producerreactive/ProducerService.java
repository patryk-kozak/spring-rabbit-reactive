package net.hackbee.srrmq.producerreactive;

import org.springframework.stereotype.Service;


import reactor.core.publisher.Flux;

@Service
public class ProducerService {

  Flux<LoadPacket> send(Integer num) {
    return Flux.range(0, num)
        .map(id -> new LoadPacket());
  }

}
