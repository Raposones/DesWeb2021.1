package br.ufc.raphael.languagenizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import br.ufc.raphael.languagenizer.nivel.CadastraNivelRequest;
import br.ufc.raphael.languagenizer.nivel.Nivel;
import br.ufc.raphael.languagenizer.nivel.NivelRepository;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Transactional
@ExtendWith(MockitoExtension.class)
public class NivelTeste {
    private final String urlHost = "http://localhost:8080";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    NivelRepository nivelRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void cleanDB(){
        nivelRepository.deleteAll();
    }

    @Test
    @DisplayName("Adicionar um novo nivel")
    public void addNivelTest() throws JsonProcessingException, Exception{
        CadastraNivelRequest cadastraNivelRequest = new CadastraNivelRequest("a2");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/nivel") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraNivelRequest))) 
            .andExpect(MockMvcResultMatchers.status().isCreated()); 

        Optional<Nivel> nivel = nivelRepository.findByClasse(cadastraNivelRequest.getClasse());

        assertTrue(nivel.isPresent()); 

        assertEquals(cadastraNivelRequest.getClasse(), nivel.get().getClasse()); 
    }

    @Test
    @DisplayName("Adicionar um nivel ja existente")
    public void addRepNivelTest() throws JsonProcessingException, Exception{
        CadastraNivelRequest cadastraNivelRequest = new CadastraNivelRequest("a2");
        Nivel nivelRep = new Nivel("a2");
        nivelRepository.save(nivelRep);

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/nivel") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraNivelRequest))) 
            .andExpect(MockMvcResultMatchers.status().isConflict()); 
    }

    @Test
    @DisplayName("Adicionar um nivel invalido")
    public void addInvNivelTest() throws JsonProcessingException, Exception{
        CadastraNivelRequest cadastraNivelRequest = new CadastraNivelRequest("");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/nivel") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraNivelRequest))) 
            .andExpect(MockMvcResultMatchers.status().isBadRequest()); 

        Optional<Nivel> nivel = nivelRepository.findByClasse(cadastraNivelRequest.getClasse());

        assertFalse(nivel.isPresent()); 
    }

    @Test
    @DisplayName("Remoção bem-sucedida de um nivel")
    public void removeNivelTest() throws JsonProcessingException, Exception{
        Nivel nivel = new Nivel("a2");
        nivelRepository.save(nivel);
        String remNivel = nivel.getClasse();

        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/nivel/" + remNivel + "/remover"))
            .andExpect(MockMvcResultMatchers.status().isOk()); 

        Optional<Nivel> nivelCheck = nivelRepository.findByClasse(nivel.getClasse());

        assertFalse(nivelCheck.isPresent()); 
    }

    @Test
    @DisplayName("Remoção mal-sucedida de um nivel")
    public void badRemoveNivelTest() throws JsonProcessingException, Exception{
        Nivel nivel = new Nivel("a2");
        nivelRepository.save(nivel);
        String remNivel = "b2";

        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/lingua/" + remNivel + "/remover"))
            .andExpect(MockMvcResultMatchers.status().isConflict()); 

    }

    @Test
    @DisplayName("Mostrar todos os niveis")
    public void showNiveisTest() throws JsonProcessingException, Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/niveis"))
            .andExpect(MockMvcResultMatchers.status().isOk()); 

    }
}
