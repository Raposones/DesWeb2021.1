package br.ufc.raphael.languagenizer.curso;

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

import br.ufc.raphael.languagenizer.lingua.Lingua;
import br.ufc.raphael.languagenizer.lingua.LinguaRepository;
import br.ufc.raphael.languagenizer.nivel.Nivel;
import br.ufc.raphael.languagenizer.nivel.NivelRepository;
import br.ufc.raphael.languagenizer.professor.Professor;
import br.ufc.raphael.languagenizer.professor.ProfessorRepository;

@RestController
public class CursoController {
    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    LinguaRepository linguaRepository;

    @Autowired
    NivelRepository nivelRepository;

    @PostMapping(value = "/curso")
    @Transactional
    public ResponseEntity<Object> addCurso(@RequestBody @Valid CadastraCursoRequest cadastraCursoRequest){
        boolean existsCurso = cursoRepository.existsByNome(cadastraCursoRequest.getNome());

        if(existsCurso){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este curso já está cadastrado neste sistema.");
        }

        Optional<Professor> profOpt = professorRepository.findByEmail(cadastraCursoRequest.getEmailprof());
        Optional<Lingua> linguaOpt = linguaRepository.findByNome(cadastraCursoRequest.getLinguagem());
        Optional<Nivel> nivelOpt = nivelRepository.findByClasse(cadastraCursoRequest.getClasse());

        if(profOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Professor inválido!");
        }

        if(linguaOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Lingua inválida!");
        }

        if(nivelOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Nivel inválido!");
        }

        Curso curso = cadastraCursoRequest.toModel(profOpt.get(), linguaOpt.get(), nivelOpt.get());
        cursoRepository.save(curso);
        
        return ResponseEntity.created(null).body("Curso cadastrado com successo!");
    }

    @GetMapping(value = "/curso/{nome}")
    @Transactional
    public ResponseEntity<Object> showCurso(@Valid @NotNull @PathVariable String nome){
        Optional<Curso> cursoOpt = cursoRepository.findByNome(nome);

        if(cursoOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nome inexistente.");
        }

        Curso curso = cursoOpt.get();

        return ResponseEntity.ok().body(curso);
    }

    @GetMapping(value = "/curso/{remNome}/remover")
    @Transactional
    public ResponseEntity<Object> removeCurso(@Valid @NotNull @PathVariable String remNome){
        Optional<Curso> cursoOpt = cursoRepository.findByNome(remNome);

        if(cursoOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Curso inexistente.");
        }

        cursoRepository.delete(cursoOpt.get());
        
        return ResponseEntity.ok().body("Curso removido com successo!");
    }

    @GetMapping(value="/cursos") 
    public List<Curso> listaCurso(){
        List<Curso> cursosList = cursoRepository.findAll();

        if(cursosList.isEmpty()){
            return null;
        }
        return cursosList;
    }
}
