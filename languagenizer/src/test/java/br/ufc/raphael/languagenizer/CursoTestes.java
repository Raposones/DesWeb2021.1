package br.ufc.raphael.languagenizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.ufc.raphael.languagenizer.curso.CadastraCursoRequest;
import br.ufc.raphael.languagenizer.curso.Curso;
import br.ufc.raphael.languagenizer.curso.CursoRepository;
import br.ufc.raphael.languagenizer.lingua.Lingua;
import br.ufc.raphael.languagenizer.lingua.LinguaRepository;
import br.ufc.raphael.languagenizer.nivel.Nivel;
import br.ufc.raphael.languagenizer.nivel.NivelRepository;
import br.ufc.raphael.languagenizer.professor.Professor;
import br.ufc.raphael.languagenizer.professor.ProfessorRepository;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Transactional
@ExtendWith(MockitoExtension.class)
public class CursoTestes {
    private final String urlHost = "http://localhost:8080";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    NivelRepository nivelRepository;

    @Autowired
    LinguaRepository linguaRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void cleanDB(){
        cursoRepository.deleteAll();
        nivelRepository.deleteAll();
        linguaRepository.deleteAll();
        professorRepository.deleteAll();
    }

    @Test
    @DisplayName("Adicionar um novo curso")
    public void addCursoTest() throws JsonProcessingException, Exception{
        Professor prof = new Professor("Roberto Carlos", "carlrobert@gmail.com", "99188893393", "Prof de teste");
        Lingua lingua = new Lingua("ingles");
        Nivel nivel = new Nivel("b2");
        professorRepository.save(prof);
        linguaRepository.save(lingua);
        nivelRepository.save(nivel);

        CadastraCursoRequest cadastraCursoRequest = new CadastraCursoRequest("intensivao ingles", 300, "intensivao ingles b2", "carlrobert@gmail.com", "ingles", "b2");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/curso") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraCursoRequest))) 
            .andExpect(MockMvcResultMatchers.status().isCreated()); 

        Optional<Curso> curso = cursoRepository.findByNome(cadastraCursoRequest.getNome());

        assertTrue(curso.isPresent()); 

        assertEquals(cadastraCursoRequest.getNome(), curso.get().getNome()); 
    }

    @Test
    @DisplayName("Adicionar um curso já existente")
    public void addRepCursoTest() throws JsonProcessingException, Exception{
        Professor prof = new Professor("Roberto Carlos", "carlrobert@gmail.com", "99188893393", "Prof de teste");
        Lingua lingua = new Lingua("ingles");
        Nivel nivel = new Nivel("b2");
        professorRepository.save(prof);
        linguaRepository.save(lingua);
        nivelRepository.save(nivel);


        CadastraCursoRequest cadastraCursoRequest = new CadastraCursoRequest("intensingles", 300, "intensivao ingles b2", "carlrobert@gmail.com", "ingles", "b2");
        Curso curso1 = new Curso("intensingles", 300, "intensivao ingles b2", prof, lingua, nivel);
        cursoRepository.save(curso1);
        

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/curso") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraCursoRequest))) 
            .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("Adicionar um curso com nome invalido")
    public void addInvNameCursoTest() throws JsonProcessingException, Exception{
        Professor prof = new Professor("Roberto Carlos", "carlrobert@gmail.com", "99188893393", "Prof de teste");
        Lingua lingua = new Lingua("ingles");
        Nivel nivel = new Nivel("b2");
        professorRepository.save(prof);
        linguaRepository.save(lingua);
        nivelRepository.save(nivel);

        CadastraCursoRequest cadastraCursoRequest = new CadastraCursoRequest("", 300, "intensivao ingles b2", "carlrobert@gmail.com", "ingles", "b2");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/curso") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraCursoRequest))) 
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Adicionar um curso com professor invalido")
    public void addInvProfCursoTest() throws JsonProcessingException, Exception{
        Professor prof = new Professor("Roberto Carlos", "carlrobert@gmail.com", "99188893393", "Prof de teste"); //nao usarei este email
        Lingua lingua = new Lingua("ingles");
        Nivel nivel = new Nivel("b2");
        professorRepository.save(prof);
        linguaRepository.save(lingua);
        nivelRepository.save(nivel);

        CadastraCursoRequest cadastraCursoRequest = new CadastraCursoRequest("intensivao ingles", 300, "intensivao ingles b2", "notprof@gmail.com", "ingles", "b2");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/curso") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraCursoRequest))) 
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Adicionar um curso com lingua invalida")
    public void addInvLinguaCursoTest() throws JsonProcessingException, Exception{
        Professor prof = new Professor("Roberto Carlos", "carlrobert@gmail.com", "99188893393", "Prof de teste");
        Lingua lingua = new Lingua("ingles"); //nao usarei esta lingua
        Nivel nivel = new Nivel("b2");
        professorRepository.save(prof);
        linguaRepository.save(lingua);
        nivelRepository.save(nivel);

        CadastraCursoRequest cadastraCursoRequest = new CadastraCursoRequest("intensivao ingles", 300, "intensivao ingles b2", "carlrobert@gmail.com", "frances", "b2");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/curso") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraCursoRequest))) 
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Adicionar um curso com nivel invalido")
    public void addInvNivelCursoTest() throws JsonProcessingException, Exception{
        Professor prof = new Professor("Roberto Carlos", "carlrobert@gmail.com", "99188893393", "Prof de teste");
        Lingua lingua = new Lingua("ingles");
        Nivel nivel = new Nivel("b2"); //nao usarei este nivel
        professorRepository.save(prof);
        linguaRepository.save(lingua);
        nivelRepository.save(nivel);

        CadastraCursoRequest cadastraCursoRequest = new CadastraCursoRequest("intensivao ingles", 300, "intensivao ingles b2", "carlrobert@gmail.com", "ingles", "a2");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/curso") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraCursoRequest))) 
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Mostrar um curso pelo nome")
    public void showCursoTest() throws JsonProcessingException, Exception{
        Professor prof = new Professor("Roberto Carlos", "carlrobert@gmail.com", "99188893393", "Prof de teste"); //nao usarei este email
        Lingua lingua = new Lingua("ingles");
        Nivel nivel = new Nivel("b2");
        professorRepository.save(prof);
        linguaRepository.save(lingua);
        nivelRepository.save(nivel);

        Curso curso = new Curso("intensingles", 300, "intensivao ingles b2", prof, lingua, nivel);
        cursoRepository.save(curso);
        String nomeCurso = curso.getNome();

        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/curso/" + nomeCurso))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Procura por curso que não existe")
    public void badShowCursoTest() throws JsonProcessingException, Exception{
        String nomeCurso = "intenglishente";
        //nenhum curso cadastrado, então deve retornar 409 - CONFLICT
        //por nao achar o nome desejado

        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/curso/" + nomeCurso))
            .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("Listar cursos (mesmo se vazio)")
    public void showCursosTest() throws JsonProcessingException, Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/cursos"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Remover curso existente pelo nome")
    public void remCursoTest() throws JsonProcessingException, Exception{
        Professor prof = new Professor("Roberto Carlos", "carlrobert@gmail.com", "99188893393", "Prof de teste");
        Lingua lingua = new Lingua("ingles");
        Nivel nivel = new Nivel("b2");
        professorRepository.save(prof);
        linguaRepository.save(lingua);
        nivelRepository.save(nivel);

        Curso curso = new Curso("intensingles", 300, "intensivao ingles b2", prof, lingua, nivel);
        cursoRepository.save(curso);
        String nomeCurso = curso.getNome();

        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/curso/" + nomeCurso + "/remover"))
            .andExpect(MockMvcResultMatchers.status().isOk());

        //Optional<Curso> cursoCheck = cursoRepository.findByNome(nomeCurso);

        //assertFalse(cursoCheck.isPresent());
    }

    @Test
    @DisplayName("Remover curso que não existe")
    public void badRemCursoTest() throws JsonProcessingException, Exception{
         String nomeCurso = "Frenchify";

        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/curso/" + nomeCurso + "/remover"))
            .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}
