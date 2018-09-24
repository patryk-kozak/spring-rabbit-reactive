package net.hackbee.srrmq.producerreactive;

import org.springframework.messaging.Message;


import reactor.core.publisher.Flux;

public interface RabbitReactiveTemplate<T, R> {

  Flux<R> convertAndSend(String exchange, String routingKey, T payload);

  Message<R> createMessage(T payload);

}
