package net.hackbee.srrmq.producerreactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import reactor.core.publisher.Mono;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DisplayName("RabbitReactiveTemplate should")
class RabbitReactiveTemplateTest {

  @MockBean
  private RabbitTemplate rabbitTemplate;

  @InjectMocks
  private RabbitReactiveTemplate rabbitReactiveTemplate;

  @BeforeEach
  void each() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @DisplayName(
      "extend LoadPacket with information about time send packet"
  )
  void processMethodPresent() {
    Mono<LoadPacketStatus> result =
        rabbitReactiveTemplate.process(new LoadPacket(1L, "data#1"));
    assertThat(result.block().getSendTime()).isNotNull();
  }

  @Test
  @DisplayName(
      "mark as success when send succesfully"
  )
  void markSuccessWhenSendComplete() {

  }

  @Test
  @DisplayName(
      "mark as error when send is violated"
  )
  void markErrorIfAnythingElseThenSuccess() {

  }

}
