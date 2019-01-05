package com.arma.inz.compcal.kpir;

import com.arma.inz.compcal.kpir.dto.KpirCreateDTO;
import com.arma.inz.compcal.kpir.dto.KpirFilterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins ="http://localhost:8080")
@RequestMapping("/kpir")
public interface KpirService {

    @CrossOrigin
    @PostMapping("")
    ResponseEntity get(@RequestHeader(value="Authorization") String authorization, @RequestBody KpirFilterDTO kpirFilterDTO);

    @CrossOrigin
    @GetMapping("/{id}")
    ResponseEntity get(@PathVariable Long id);

    @CrossOrigin
    @PutMapping("/")
    ResponseEntity update(@RequestBody KpirCreateDTO dto);

    @PostMapping("/")
    ResponseEntity create(@RequestHeader(value="Authorization") String authorization, @RequestBody KpirCreateDTO dto);

    @CrossOrigin
    @DeleteMapping("/{id}")
    ResponseEntity delete(@PathVariable Long id);

    @CrossOrigin
    @GetMapping("/idx")
    ResponseEntity getNextIdx(@RequestHeader(value="Authorization") String authorization);
}
