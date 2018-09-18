package net.hackbee.srrmq.producerreactive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import reactor.core.publisher.Flux;

@RestController
public class ProducerApi {

  @Autowired
  ProducerService producerService;

  @GetMapping("/produce/{amount}")
  public Flux<LoadPacketStatus> produce(@PathVariable("amount") Integer amount) {
    return producerService.send(amount);
  }

}
