package com.arma.inz.compcal.contractor;


import com.arma.inz.compcal.contractor.dto.ContractorDTO;
import com.arma.inz.compcal.contractor.dto.ContractorFilterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins ="http://localhost:4200")
@RequestMapping("/contractor")
public interface ContractorService {

    @CrossOrigin
    @PostMapping("")
    ResponseEntity get(@RequestHeader(value="Authorization") String authorization, @RequestBody ContractorFilterDTO contractorFilterDTO);

    @CrossOrigin
    @GetMapping("/{id}")
    ResponseEntity get(@PathVariable Long id);

    @CrossOrigin
    @PutMapping("/")
    ResponseEntity update(@RequestBody ContractorDTO dto);

    @PostMapping("/")
    ResponseEntity create(@RequestHeader(value="Authorization") String authorization, @RequestBody ContractorDTO dto);

    @CrossOrigin
    @DeleteMapping("/{id}")
    ResponseEntity delete(@PathVariable Long id);
}
