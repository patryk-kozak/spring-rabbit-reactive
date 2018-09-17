package net.hackbee.srrmq.producerreactive;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import reactor.core.publisher.Flux;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@DisplayName("Producer Service Test should")
class ProducerServiceTest {

  @InjectMocks
  private ProducerService producerService;

  @MockBean
  private RabbitReactiveTemplate rabbitReactiveTemplate;

  @BeforeEach
  void each() {
    MockitoAnnotations.initMocks(this);
    producerService = new ProducerService();
  }

  @Test
  @DisplayName(
      "return created packets as Flux"
  )
  void returnFluxOfPackets() {
    assertThat(producerService.send(3)).isInstanceOf(Flux.class);
  }

  @Test
  @DisplayName(
      "create a new number of packets equal to method argument"
  )
  void shouldCreatePacketsEqualToParam() {
    assertThat(producerService.send(5).collectList().block().size()).isEqualTo(5);
  }

  @Test
  @DisplayName(
      "create each LoadPacket with id and payload"
  )
  void shouldCreateLoadPacketWithData() {
    List<LoadPacket> result = producerService.send(3).collectList().block();
    result.forEach(packet -> {
      assertThat(packet.getId()).isNotNull();
      assertThat(packet.getData()).isNotNull();
    });
  }

  @Test
  @DisplayName(
      "send LoadPacket to RabbitMQ with RabbitReactiveTemplate process() method"
  )
  void shouldSendPacketToRabbit() {
    Flux<LoadPacket> res = producerService.send(3);
    verify(rabbitReactiveTemplate, times(3)).process(any(LoadPacket.class));
  }
}
