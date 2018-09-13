package net.hackbee.srrmq.producerreactive;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import reactor.core.publisher.Flux;

@RestController
public class ProducerApi {

  @Autowired
  ProducerService producerService;

  @GetMapping("/produce")
  public Flux<LoadPacket> produce() {
    return producerService.send(ThreadLocalRandom.current().nextInt(1, 11));
  }

}
