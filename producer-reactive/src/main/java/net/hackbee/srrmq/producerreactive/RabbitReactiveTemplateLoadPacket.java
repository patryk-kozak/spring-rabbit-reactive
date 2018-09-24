package net.hackbee.srrmq.producerreactive;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RabbitReactiveTemplateLoadPacket implements RabbitReactiveTemplate<LoadPacket, LoadPacketStatus> {

  @Autowired
  private RabbitMessagingTemplate rabbitTemplate;

  @Override
  public Flux<LoadPacketStatus> convertAndSend(String exchange, String routingKey, LoadPacket payload) {
    return (Flux<LoadPacketStatus>) Mono.fromCallable(
        () -> rabbitTemplate.sendAndReceive(
            exchange,
            routingKey,
            createMessage(payload)
        ).getPayload()
    ).flux();
  }

  @Override
  public Message<LoadPacketStatus> createMessage(LoadPacket payload) {
    return new GenericMessage<>(
        LoadPacketStatus.builder().loadPacket(payload).build()
    );
  }

}
