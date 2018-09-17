package net.hackbee.srrmq.producerreactive;

import java.io.Serializable;
import org.springframework.boot.jackson.JsonComponent;


import lombok.Builder;
import lombok.Data;

@JsonComponent
@Data
@Builder
class LoadPacket implements Serializable {

  private Long id;
  private String data;

}
