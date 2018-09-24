package net.hackbee.srrmq.producerreactive;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import reactor.core.publisher.Flux;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@DisplayName("RabbitReactiveTemplate should")
class RabbitReactiveTemplateLoadPacketTest {

  @MockBean
  private RabbitMessagingTemplate rabbitTemplate;

  @InjectMocks
  private RabbitReactiveTemplateLoadPacket rabbitReactiveTemplate;

  @BeforeEach
  void each() {
    MockitoAnnotations.initMocks(this);
    doReturn(rabbitReactiveTemplate.createMessage(
        LoadPacket.builder().id(99L).data("data99").build()
    )).when(rabbitTemplate).sendAndReceive(anyString(), anyString(), any(Message.class));
  }

  @Test
  @DisplayName(
      "should return Flux created from Callable to rabbitTemplate"
  )
  void returnFluxFromRabbitConvertAndSend() {
    Flux<LoadPacketStatus> returnStream =
        rabbitReactiveTemplate.convertAndSend("exchange", "routeKey", LoadPacket.builder().id(1L).data("exp").build());

    List<LoadPacketStatus> blockedStream = returnStream.collectList().block();
    assertThat(blockedStream.get(0)).isNotNull();
  }

  @Test
  @DisplayName(
      "shoudl return a Message<?> from createMessage(payload)"
  )
  void returnMessageFromCreateMessage() {
    LoadPacket loadPacket = LoadPacket.builder().id(99L).data("ddd99").build();
    LoadPacketStatus expectedReturn = LoadPacketStatus.builder().loadPacket(loadPacket).build();
    Message<LoadPacketStatus> result = rabbitReactiveTemplate.createMessage(loadPacket);
    assertThat(result.getPayload()).isEqualTo(expectedReturn);
  }

  @Test
  @DisplayName(
      "should create a Message<?> type out of the LoadPacketStatus when convertAndSend called"
  )
  void shouldCreateMessageType() {
    String exchange = "exch1";
    String routingKey = "routeKey1";
    LoadPacket payload = LoadPacket.builder().id(99L).data("99 bottles").build();
    Flux<LoadPacketStatus> result = rabbitReactiveTemplate.convertAndSend(exchange, routingKey, payload);
    verify(rabbitTemplate).sendAndReceive(exchange, routingKey, any(Message.class));
  }

  @Test
  @DisplayName(
      "invoke rabbitTemplate same amount of time as reactive convertAndSend()"
  )
  void wrapperSameTimeInvokedAsImplementation() {
    rabbitReactiveTemplate.convertAndSend("exch", "key", new LoadPacket(1L, "data#1"));
    verify(rabbitTemplate).sendAndReceive(anyString(), anyString(), any(Message.class));
  }

}
