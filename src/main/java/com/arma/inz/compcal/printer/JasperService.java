package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/printer")
public interface JasperService {
    @CrossOrigin
    @PostMapping("/kpirs")
    ResponseEntity<Resource> getKpirPdfAsResponse(@RequestHeader(value="Authorization") String authorization, @RequestBody KpirFilterDTO kpirFilterDTO);

    @CrossOrigin
    @PostMapping(value = "/kpir", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    byte[] getKpirPdfAsBytes(@RequestHeader(value="Authorization") String authorization, @RequestBody KpirFilterDTO kpirFilterDTO);

    @CrossOrigin
    @PostMapping("/mail/kpir")
    void sendKpirPdfViaMail(@RequestHeader(value="Authorization") String authorization, @RequestBody KpirFilterDTO kpirFilterDTO);
}
