package net.hackbee.srrmq.producerreactive;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;


import reactor.core.publisher.Flux;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Producer API Test should")
class ProducerApiTest {

  @LocalServerPort
  int port;

  @MockBean
  ProducerService producerService;

  @Captor
  ArgumentCaptor<Integer> producerServiceArgumentCaptor;

  private WebTestClient webTestClient;

  @BeforeEach
  void before() {
    MockitoAnnotations.initMocks(this);
    this.webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    when(producerService.send(anyInt())).thenReturn(Flux.fromIterable(
        Collections.singletonList(LoadPacketStatus.builder()
            .isSuccess(true)
            .sendTime(ZonedDateTime.now())
            .build()))
    );
  }

  @Test
  @DisplayName(
      "have a 'produce' HttpMethod.GET endpoint"
  )
  void shouldHavePostEndpointProduce() {
    webTestClient.get().uri("/produce/1").exchange().expectStatus().is2xxSuccessful();
  }

  @Test
  @DisplayName(
      "call ProducerService on 'produce' endpoint"
  )
  void shouldCallProducerService() {
    webTestClient.get().uri("/produce/1").exchange().expectStatus().is2xxSuccessful();
    verify(producerService).send(any());
  }

  @Test
  @DisplayName(
      "pass number with request and pass it to ProducerService"
  )
  void generateRandomNumAndPassToService() {
    webTestClient.get().uri("/produce/12").exchange().expectStatus().is2xxSuccessful();
    verify(producerService).send(producerServiceArgumentCaptor.capture());
    assertThat(producerServiceArgumentCaptor.getValue()).isInstanceOf(Integer.class);
  }

  @Test
  @DisplayName(
      "return Flux of LoadPacketStatus class"
  )
  void returnFluxWithLoadPacket() {
    Flux<LoadPacketStatus> result = webTestClient.get().uri("/produce/1").exchange()
        .expectStatus().is2xxSuccessful()
        .returnResult(LoadPacketStatus.class)
        .getResponseBody();

    verify(producerService).send(producerServiceArgumentCaptor.capture());
    assertThat(producerServiceArgumentCaptor.getValue()).isInstanceOf(Integer.class);

    assertThat(result).isNotNull();
    assertThat(result.collectList().block().size()).isEqualTo(producerServiceArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "return positive number of packets"
  )
  void dontReturnNegativeNumbers() {
    Flux<LoadPacketStatus> result = webTestClient.get().uri("/produce/5").exchange()
        .expectStatus().is2xxSuccessful()
        .returnResult(LoadPacketStatus.class)
        .getResponseBody();

    assertThat(result).isNotNull();
    assertThat(result.collectList().block().size()).isPositive();
  }

  @Test
  @DisplayName(
      "return status of each packet"
  )
  void returnPacketStatus() {
    List<LoadPacketStatus> result = webTestClient.get().uri("/produce/3")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .returnResult(LoadPacketStatus.class)
        .getResponseBody()
        .collectList().block();

    assertThat(result).isNotEmpty();
    assert result != null;
    result.forEach(
        res -> {
          assertThat(res.getSendTime()).isNotNull();
          assertThat(res.isSuccess()).isTrue();
        }
    );
  }
}
