package com.jzel.cloudstorageprobe.webservice.adapter.rest;

import com.jzel.cloudstorageprobe.business.ProbeService;
import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProbeController {

  private final ProbeService probeService;
  private final ExecutorService probeExecutor;

  @PutMapping("/probe")
  public void probe() {
    probeExecutor.submit(probeService::probe);
  }
}
