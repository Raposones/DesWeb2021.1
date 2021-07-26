package br.ufc.raphael.languagenizer.professor;

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
public class ProfessorController {
    
    @Autowired
    ProfessorRepository professorRepository;

    @PostMapping(value = "/professor")
    @Transactional
    public ResponseEntity<Object> addProfessor(@RequestBody @Valid CadastraProfessorRequest cadastraProfessorRequest){
        boolean existsEmail = professorRepository.existsByEmail(cadastraProfessorRequest.getEmail());

        if(existsEmail){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("O email deste professor já está cadastrado neste sistema.");
        }

        Professor professor = cadastraProfessorRequest.toModel();
        professorRepository.save(professor);
        
        return ResponseEntity.created(null).body("Professor cadastrado com successo!");
    }

    @GetMapping(value = "/professor/{email}")
    @Transactional
    public ResponseEntity<Object> showProfessor(@Valid @NotNull @PathVariable String email){
        Optional<Professor> professorOpt = professorRepository.findByEmail(email);

        if(professorOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email inexistente.");
        }

        Professor professor = professorOpt.get();

        return ResponseEntity.ok().body(professor);
    }

    @GetMapping(value = "/professor/{remEmail}/remover")
    @Transactional
    public ResponseEntity<Object> removeProfessor(@Valid @NotNull @PathVariable String remEmail){
        Optional<Professor> professorOpt = professorRepository.findByEmail(remEmail);

        if(professorOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email inexistente.");
        }

        professorRepository.delete(professorOpt.get());
        
        return ResponseEntity.ok().body("Professor removido com successo!");
    }

    @PostMapping(value = "professor/alterar", consumes = "application/json")
    @Transactional
    public ResponseEntity<Object> updateProfessor(@Valid @RequestBody ObjectNode objectNode){
        String email = objectNode.get("email").asText();
        String tel = objectNode.get("telefone").asText();
        String nome = objectNode.get("nome").asText();
        String desc = objectNode.get("descricao").asText();

        Optional<Professor> professorOpt = professorRepository.findByEmail(email);

        if(professorOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email inexistente.");
        }

        Professor professorOld = professorOpt.get();
        String nomeOld = professorOld.getNome();
        String telefoneOld = professorOld.getTelefone();
        String descOld = professorOld.getDescricao();

        professorRepository.updateProfessorByEmail(nome, tel, email, desc);

        
        return ResponseEntity.ok().body("ALTERAÇÃO DE PROFESSOR\n"
                                      + "Nome: " + nomeOld + " --> " + nome
                                      + "\nTelefone: " + telefoneOld + " --> " + tel
                                      + "\nDescricao: " + descOld + " -----> " + desc
                                      + "\nEmail: " + email
                                      + "\n\nAlteração concluída.");
    }

    @GetMapping(value="/professores") 
    public List<Professor> listaProfessores(){
        List<Professor> professoresList = professorRepository.findAll();

        if(professoresList.isEmpty()){
            return null;
        }
        return professoresList;
    }
}
