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

import br.ufc.raphael.languagenizer.professor.CadastraProfessorRequest;
import br.ufc.raphael.languagenizer.professor.Professor;
import br.ufc.raphael.languagenizer.professor.ProfessorRepository;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Transactional
@ExtendWith(MockitoExtension.class)
public class ProfessorTestes {
    private final String urlHost = "http://localhost:8080";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void cleanDB(){
        professorRepository.deleteAll();
    }

    @Test
    @DisplayName("Adicionar um novo professor")
    public void addProfessorTest() throws JsonProcessingException, Exception{
        CadastraProfessorRequest cadastraProfessorRequest = new CadastraProfessorRequest("Roberto Carlos", "carlrobert@gmail.com", "99192939495", "Professor de espanhol e ingles");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/professor") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraProfessorRequest))) 
            .andExpect(MockMvcResultMatchers.status().isCreated()); 

        Optional<Professor> professor = professorRepository.findByEmail(cadastraProfessorRequest.getEmail());

        assertTrue(professor.isPresent()); 

        assertEquals(cadastraProfessorRequest.getEmail(), professor.get().getEmail()); 
    }

    @Test
    @DisplayName("Identificar um professor com email repetido") 
    public void addRepeatedProfessorTest() throws JsonProcessingException, Exception{
        CadastraProfessorRequest cadastraProfessorRequest = new CadastraProfessorRequest("Roberto Carlos", "carlrobert@gmail.com", "99192939495", "Professor de espanhol e ingles");
        Professor professorRepeat = new Professor("Roberto Carlos", "carlrobert@gmail.com", "99192939495", "Professor de espanhol e ingles");
        professorRepository.save(professorRepeat);

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/professor") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraProfessorRequest)))
            .andExpect(MockMvcResultMatchers.status().isConflict()); 
    }

    @Test
    @DisplayName("Cadastro de professor com nome invalido") 
    public void addInvalidNameProfessorTest() throws JsonProcessingException, Exception{
        CadastraProfessorRequest cadastraProfessorRequest = new CadastraProfessorRequest("", "carlrobert@gmail.com", "99192939495", "Professor de espanhol e ingles");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/professor") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraProfessorRequest)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()); 
    }

    @Test
    @DisplayName("Cadastro de professor com email invalido") 
    public void addInvalidEmailProfessorTest() throws JsonProcessingException, Exception{
        CadastraProfessorRequest cadastraProfessorRequest = new CadastraProfessorRequest("Roberto Carlos", "carlrobertgmail.com", "99192939495", "Professor de espanhol e ingles");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/professor") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraProfessorRequest)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()); 
    }


    @Test
    @DisplayName("Cadastro de professor com telefone invalido") 
    public void addInvalidPhoneProfessorTest() throws JsonProcessingException, Exception{
        CadastraProfessorRequest cadastraProfessorRequest = new CadastraProfessorRequest("Roberto Carlos", "carlrobert@gmail.com", "", "Professor de espanhol e ingles");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/professor") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraProfessorRequest)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()); 
    }

    @Test
    @DisplayName("Cadastro de professor com descricao invalida") 
    public void addInvalidPhoneAlunoTest() throws JsonProcessingException, Exception{
        CadastraProfessorRequest cadastraProfessorRequest = new CadastraProfessorRequest("Roberto Carlos", "carlrobert@gmail.com", "996948234", "");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/professor") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraProfessorRequest)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()); 
    }
}
