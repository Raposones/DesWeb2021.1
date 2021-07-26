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

import br.ufc.raphael.languagenizer.lingua.CadastraLinguaRequest;
import br.ufc.raphael.languagenizer.lingua.Lingua;
import br.ufc.raphael.languagenizer.lingua.LinguaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Transactional
@ExtendWith(MockitoExtension.class)
public class LinguaTestes {
    private final String urlHost = "http://localhost:8080";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LinguaRepository linguaRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void cleanDB(){
        linguaRepository.deleteAll();
    }

    @Test
    @DisplayName("Adicionar uma nova lingua")
    public void addLinguaTest() throws JsonProcessingException, Exception{
        CadastraLinguaRequest cadastraLinguaRequest = new CadastraLinguaRequest("ingles");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/lingua") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraLinguaRequest))) 
            .andExpect(MockMvcResultMatchers.status().isCreated()); 

        Optional<Lingua> lingua = linguaRepository.findByNome(cadastraLinguaRequest.getNome());

        assertTrue(lingua.isPresent()); 

        assertEquals(cadastraLinguaRequest.getNome(), lingua.get().getNome()); 
    }

    @Test
    @DisplayName("Adicionar uma lingua já existente")
    public void addRepLinguaTest() throws JsonProcessingException, Exception{
        CadastraLinguaRequest cadastraLinguaRequest = new CadastraLinguaRequest("ingles");
        Lingua linguaRep = new Lingua("ingles");
        linguaRepository.save(linguaRep);

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/lingua") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraLinguaRequest))) 
            .andExpect(MockMvcResultMatchers.status().isConflict()); 

    }

    @Test
    @DisplayName("Adicionar uma lingua invalida")
    public void addInvLinguaTest() throws JsonProcessingException, Exception{
        CadastraLinguaRequest cadastraLinguaRequest = new CadastraLinguaRequest("");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/lingua") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraLinguaRequest))) 
            .andExpect(MockMvcResultMatchers.status().isBadRequest()); 

        Optional<Lingua> lingua = linguaRepository.findByNome(cadastraLinguaRequest.getNome());

        assertFalse(lingua.isPresent()); 
    }

    @Test
    @DisplayName("Remoção bem-sucedida de uma lingua")
    public void removeLinguaTest() throws JsonProcessingException, Exception{
        Lingua lingua = new Lingua("ingles");
        linguaRepository.save(lingua);
        String remLingua = lingua.getNome();

        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/lingua/" + remLingua + "/remover"))
            .andExpect(MockMvcResultMatchers.status().isOk()); 

        Optional<Lingua> linguaCheck = linguaRepository.findByNome(lingua.getNome());

        assertFalse(linguaCheck.isPresent()); 
    }

    @Test
    @DisplayName("Remoção mal-sucedida de uma lingua")
    public void badRemoveLinguaTest() throws JsonProcessingException, Exception{
        Lingua lingua = new Lingua("ingles");
        linguaRepository.save(lingua);
        String remLingua = "frances";

        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/lingua/" + remLingua + "/remover"))
            .andExpect(MockMvcResultMatchers.status().isConflict()); 

    }

    @Test
    @DisplayName("Mostrar todas as linguas")
    public void showLinguasTest() throws JsonProcessingException, Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/linguas"))
            .andExpect(MockMvcResultMatchers.status().isOk()); 

    }
}
