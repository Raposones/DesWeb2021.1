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

import br.ufc.raphael.languagenizer.aluno.Aluno;
import br.ufc.raphael.languagenizer.aluno.AlunoRepository;
import br.ufc.raphael.languagenizer.aluno.CadastraAlunoRequest;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Transactional
@ExtendWith(MockitoExtension.class)
public class AlunoTestes {
    private final String urlHost = "http://localhost:8080";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AlunoRepository alunoRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void cleanDB(){
        alunoRepository.deleteAll();
    }

    @Test
    @DisplayName("Adicionar um novo aluno")
    public void addAlunoTest() throws JsonProcessingException, Exception{
        CadastraAlunoRequest cadastraAlunoRequest = new CadastraAlunoRequest("Raphael Garcia", "raphaelgarcia@alu.ufc.br", "85991928794");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/aluno") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraAlunoRequest))) 
            .andExpect(MockMvcResultMatchers.status().isCreated()); 

        Optional<Aluno> aluno = alunoRepository.findByEmail(cadastraAlunoRequest.getEmail());

        assertTrue(aluno.isPresent()); 

        assertEquals(cadastraAlunoRequest.getEmail(), aluno.get().getEmail()); 
    }

    @Test
    @DisplayName("Identificar um aluno com email repetido") 
    public void addRepeatedAlunoTest() throws JsonProcessingException, Exception{
        CadastraAlunoRequest cadastraAlunoRequest = new CadastraAlunoRequest("Raphael Garcia", "raphaelgarcia@alu.ufc.br", "85991928794");
        String repEmail = cadastraAlunoRequest.getEmail();
        Aluno alunoRepeat = new Aluno("Rafael Garcia", repEmail, "123456789");
        alunoRepository.save(alunoRepeat);

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/aluno") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraAlunoRequest)))
            .andExpect(MockMvcResultMatchers.status().isConflict()); 
    }

    @Test
    @DisplayName("Cadastro de aluno com nome invalido") 
    public void addInvalidNameAlunoTest() throws JsonProcessingException, Exception{
        CadastraAlunoRequest cadastraAlunoRequest = new CadastraAlunoRequest("", "raphaelgarcia@alu.ufc.br", "85991928794");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/aluno") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraAlunoRequest)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()); 
    }

    @Test
    @DisplayName("Cadastro de aluno com email invalido") 
    public void addInvalidEmailAlunoTest() throws JsonProcessingException, Exception{
        CadastraAlunoRequest cadastraAlunoRequest = new CadastraAlunoRequest("Raphael Garcia", "raphaelgarciaalu.ufc.br", "85991928794");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/aluno") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraAlunoRequest)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()); 
    }

    @Test
    @DisplayName("Cadastro de aluno sem email") 
    public void addEmptyEmailAlunoTest() throws JsonProcessingException, Exception{
        CadastraAlunoRequest cadastraAlunoRequest = new CadastraAlunoRequest("Raphael Garcia", "", "85991928794");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/aluno") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraAlunoRequest)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()); 
    }

    @Test
    @DisplayName("Cadastro de aluno com telefone invalido") 
    public void addInvalidPhoneAlunoTest() throws JsonProcessingException, Exception{
        CadastraAlunoRequest cadastraAlunoRequest = new CadastraAlunoRequest("Raphael Garcia", "raphaelgarcia@alu.ufc.br", "");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/aluno") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cadastraAlunoRequest)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest()); 
    }

    @Test
    @DisplayName("Remoção mal-sucedida de aluno") 
    public void badRemoveAlunoTest() throws JsonProcessingException, Exception{
        Aluno aluno = new Aluno("Raphael Garcia", "raphaelgarcia@alu.ufc.br", "887767575");
        alunoRepository.save(aluno);
       
        String email = "outroemail@gmail.com";

        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/aluno/" + email + "/remover"))
            .andExpect(MockMvcResultMatchers.status().isConflict()); 

        Optional<Aluno> alunoAux = alunoRepository.findByEmail(aluno.getEmail());

        assertTrue(alunoAux.isPresent()); //aluno salvo tem que estar na db
    }

    @Test
    @DisplayName("Atualização bem-sucedida de aluno") 
    public void updateAlunoTest() throws JsonProcessingException, Exception{
        Aluno alunoAux = new Aluno("Raphael Garcia", "raphaelgarcia@alu.ufc.br", "887767575");
        alunoRepository.save(alunoAux);
        Aluno alunoUpd = new Aluno("Raphael Carvalho", "raphaelgarcia@alu.ufc.br", "123456778");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/aluno/alterar") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alunoUpd)))
            .andExpect(MockMvcResultMatchers.status().isOk()); 

        Optional<Aluno> aluno = alunoRepository.findByEmail(alunoUpd.getEmail());

        assertTrue(aluno.isPresent());
        assertEquals(alunoUpd.getEmail(), aluno.get().getEmail());
    }

    @Test
    @DisplayName("Atualização mal-sucedida de aluno") 
    public void badUpdateAlunoTest() throws JsonProcessingException, Exception{
        Aluno alunoAux = new Aluno("Raphael Garcia", "raphaelgarcia@alu.ufc.br", "887767575");
        alunoRepository.save(alunoAux);
        Aluno alunoUpd = new Aluno("Raphael Carvalho", "outroemail@alu.ufc.br", "123456778");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlHost + "/aluno/alterar") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alunoUpd)))
            .andExpect(MockMvcResultMatchers.status().isConflict()); 

        Optional<Aluno> aluno = alunoRepository.findByEmail(alunoUpd.getEmail());

        assertFalse(aluno.isPresent());
    }

       
    @Test
    @DisplayName("Mostrar aluno existente") 
    public void showAlunoTest() throws JsonProcessingException, Exception{
        Aluno alunoAux = new Aluno("Raphael Garcia", "raphaelgarcia@alu.ufc.br", "887767575");
        alunoRepository.save(alunoAux);
        String email = alunoAux.getEmail();

        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/aluno/" + email))
            .andExpect(MockMvcResultMatchers.status().isOk()); 

        Optional<Aluno> aluno = alunoRepository.findByEmail(email);

        assertTrue(aluno.isPresent());
    }
               

    @Test
    @DisplayName("Mostrar aluno inexistente") 
    public void badShowAlunoTest() throws JsonProcessingException, Exception{
        Aluno alunoAux = new Aluno("Raphael Garcia", "raphaelgarcia@alu.ufc.br", "887767575");
        alunoRepository.save(alunoAux);
        String email = "outroemail@gmail.com";

        mockMvc.perform(MockMvcRequestBuilders
                .get(urlHost + "/aluno/" + email))
            .andExpect(MockMvcResultMatchers.status().isConflict()); 

        Optional<Aluno> aluno = alunoRepository.findByEmail(email);
        assertFalse(aluno.isPresent());
    }
    
}
