package br.ufc.raphael.languagenizer.lingua;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinguaController {
    @Autowired
    LinguaRepository linguaRepository;

    @PostMapping(value = "/lingua")
    @Transactional
    public ResponseEntity<Object> addLingua(@RequestBody @Valid CadastraLinguaRequest cadastraLinguaRequest){
        boolean existsLingua = linguaRepository.existsByNome(cadastraLinguaRequest.getNome());

        if(existsLingua){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta lingua já está cadastrada.");
        }

        Lingua lingua = cadastraLinguaRequest.toModel();
        linguaRepository.save(lingua);
        
        return ResponseEntity.created(null).body("Lingua cadastrada com successo!");
    }


    @GetMapping(value = "/lingua/{remLingua}/remover")
    @Transactional
    public ResponseEntity<Object> removeLingua(@Valid @NotNull @PathVariable String remLingua){
        Optional<Lingua> linguaOpt = linguaRepository.findByNome(remLingua);

        if(linguaOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Lingua inexistente.");
        }

        linguaRepository.delete(linguaOpt.get());
        
        return ResponseEntity.ok().body("Lingua removida com successo!");
    }

    @GetMapping(value="/linguas") 
    public List<Lingua> listaLinguas(){
        List<Lingua> linguasList = linguaRepository.findAll();

        if(linguasList.isEmpty()){
            return null;
        }
        return linguasList;
    }
}
