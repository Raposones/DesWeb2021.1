package br.ufc.raphael.languagenizer.nivel;

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
public class NivelController {
    @Autowired
    NivelRepository nivelRepository;

    @PostMapping(value = "/nivel")
    @Transactional
    public ResponseEntity<Object> addNivel(@RequestBody @Valid CadastraNivelRequest cadastraNivelRequest){
        boolean existsNivel = nivelRepository.existsByClasse(cadastraNivelRequest.getClasse());

        if(existsNivel){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este nivel já está cadastrado.");
        }

        Nivel nivel = cadastraNivelRequest.toModel();
        nivelRepository.save(nivel);
        
        return ResponseEntity.created(null).body("Nivel cadastrado com successo!");
    }

    @GetMapping(value = "/nivel/{remNivel}/remover")
    @Transactional
    public ResponseEntity<Object> removeNivel(@Valid @NotNull @PathVariable String remNivel){
        Optional<Nivel> nivelOpt = nivelRepository.findByClasse(remNivel);

        if(nivelOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nivel inexistente.");
        }

        nivelRepository.delete(nivelOpt.get());
        
        return ResponseEntity.ok().body("Nivel removido com successo!");
    }

    @GetMapping(value="/niveis") 
    public List<Nivel> listaNiveis(){
        List<Nivel> niveisList = nivelRepository.findAll();

        if(niveisList.isEmpty()){
            return null;
        }
        return niveisList;
    }
}
