package br.ufc.raphael.languagenizer.aluno;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlunoController {
    
    @Autowired
    AlunoRepository alunoRepository;


    @PostMapping(value = "/aluno")
    @Transactional
    public ResponseEntity<Object> addAluno(@RequestBody @Valid CadastraAlunoRequest cadastraAlunoRequest){
        boolean existsEmail = alunoRepository.existsByEmail(cadastraAlunoRequest.getEmail());

        if(existsEmail){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("O email deste aluno já está cadastrado neste sistema.");
        }

        Aluno aluno = cadastraAlunoRequest.toModel();
        alunoRepository.save(aluno);
        
        return ResponseEntity.created(null).body("Aluno cadastrado com successo!");
    }

    @GetMapping(value = "/aluno/{email}")
    @Transactional
    public ResponseEntity<Object> showAluno(@Valid @NotNull @PathVariable String email){
        Optional<Aluno> alunoOpt = alunoRepository.findByEmail(email);

        if(alunoOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email inexistente.");
        }

        Aluno aluno = alunoOpt.get();

        return ResponseEntity.ok().body(aluno);
    }

    @GetMapping(value = "/aluno/{remEmail}/remover")
    @Transactional
    public ResponseEntity<Object> removeAluno(@Valid @NotNull @PathVariable String remEmail){
        Optional<Aluno> alunoOpt = alunoRepository.findByEmail(remEmail);

        if(alunoOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email inexistente.");
        }

        alunoRepository.delete(alunoOpt.get());
        
        return ResponseEntity.ok().body("Aluno removido com successo!");
    }

    @PostMapping(value = "/aluno/alterar", consumes = "application/json")
    @Transactional
    public ResponseEntity<Object> updateAluno(@Valid @RequestBody ObjectNode objectNode){
        String email = objectNode.get("email").asText();
        String tel = objectNode.get("telefone").asText();
        String nome = objectNode.get("nome").asText();
        
        Optional<Aluno> alunoOpt = alunoRepository.findByEmail(email);

        if(alunoOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email inexistente.");
        }

        Aluno alunoOld = alunoOpt.get();
        String nomeOld = alunoOld.getNome();
        String telefoneOld = alunoOld.getTelefone();

        alunoRepository.updateAlunoByEmail(nome, tel, email);

        
        return ResponseEntity.ok().body("ALTERAÇÃO DE ALUNO\n"
                                      + "Nome: " + nomeOld + " --> " + nome
                                      + "\nTelefone: " + telefoneOld + " --> " + tel
                                      + "\nEmail: " + email
                                      + "\n\nAlteração concluída.");
    }

    @GetMapping(value="/alunos") 
    public List<Aluno> listaAlunos(){
        List<Aluno> alunosList = alunoRepository.findAll();

        if(alunosList.isEmpty()){
            return null;
        }
        return alunosList;
    }
}
