package com.arma.inz.compcal.printer;

import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/printer")
public interface JasperService {

    @PostMapping("/kpirs")
    ResponseEntity<byte[]> getKpir(@RequestHeader(value="Authorization") String authorization, @RequestBody KpirFilterDTO kpirFilterDTO) throws IOException;
}
