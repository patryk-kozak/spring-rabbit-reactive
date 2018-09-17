package net.hackbee.srrmq.producerreactive;

import java.time.ZonedDateTime;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoadPacketStatus {

  private LoadPacket loadPacket;
  private boolean isSuccess;
  private ZonedDateTime sendTime;

}
