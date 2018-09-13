package net.hackbee.srrmq.producerreactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import reactor.core.publisher.Flux;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class ProducerServiceTest {

  private ProducerService producerService;

  @BeforeEach
  void each() {
    producerService = new ProducerService();
  }

  @Test
  @DisplayName(
      "Should return created packets as Flux"
  )
  void returnFluxOfPackets() {
    assertThat(producerService.send(3)).isInstanceOf(Flux.class);
  }

  @Test
  @DisplayName(
      "Should create a new number of packets equal to method argument"
  )
  void shouldCreatePacketsEqualToParam() {
    assertThat(producerService.send(5).collectList().block().size()).isEqualTo(5);
  }

}
