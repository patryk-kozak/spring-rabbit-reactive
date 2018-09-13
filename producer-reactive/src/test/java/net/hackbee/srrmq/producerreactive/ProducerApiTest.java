package net.hackbee.srrmq.producerreactive;

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
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    doCallRealMethod().when(producerService).send(any());
  }

  @Test
  @DisplayName(
      "There should be a 'produce' HttpMethod.GET endpoint"
  )
  void shouldHavePostEndpointProduce() {
    webTestClient.get().uri("/produce").exchange().expectStatus().is2xxSuccessful();
  }

  @Test
  @DisplayName(
      "Should call ProducerService on 'produce' endpoint"
  )
  void shouldCallProducerService() {
    webTestClient.get().uri("/produce").exchange().expectStatus().is2xxSuccessful();
    verify(producerService).send(any());
  }

  @Test
  @DisplayName(
      "Should generate number and pass it to ProducerService"
  )
  void generateRandomNumAndPassToService() {
    webTestClient.get().uri("/produce").exchange().expectStatus().is2xxSuccessful();
    verify(producerService).send(producerServiceArgumentCaptor.capture());
    assertThat(producerServiceArgumentCaptor.getValue()).isInstanceOf(Integer.class);
  }

  @Test
  @DisplayName(
      "Should return Flux of LoadPacket class"
  )
  void returnFluxWithLoadPacket() {
    Flux<LoadPacket> result = webTestClient.get().uri("/produce").exchange()
        .expectStatus().is2xxSuccessful()
        .returnResult(LoadPacket.class)
        .getResponseBody();

    verify(producerService).send(producerServiceArgumentCaptor.capture());
    assertThat(producerServiceArgumentCaptor.getValue()).isInstanceOf(Integer.class);

    assertThat(result).isNotNull();
    assertThat(result.collectList().block().size()).isEqualTo(producerServiceArgumentCaptor.getValue());
  }

  @Test
  @DisplayName(
      "Number of packets should be positive"
  )
  void dontReturnNegativeNumbers() {
    Flux<LoadPacket> result = webTestClient.get().uri("/produce").exchange()
        .expectStatus().is2xxSuccessful()
        .returnResult(LoadPacket.class)
        .getResponseBody();

    assertThat(result).isNotNull();
    assertThat(result.collectList().block().size()).isPositive();
  }

}
