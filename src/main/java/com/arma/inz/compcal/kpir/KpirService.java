package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins ="http://localhost:4200")
@RequestMapping("/kpir")
public interface KpirService {

    @GetMapping("/")
    ResponseEntity get(@RequestBody KpirFilterDTO kpirFilterDTO);

    @GetMapping("/{id}")
    ResponseEntity get(@RequestParam Long id);

    @PostMapping("/")
    ResponseEntity create(@RequestBody KpirCreateDTO kpirDTO);

    @PutMapping("/")
    ResponseEntity update(@RequestBody KpirCreateDTO kpirDTO);

}
