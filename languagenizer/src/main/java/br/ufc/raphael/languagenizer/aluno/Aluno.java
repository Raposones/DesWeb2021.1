package br.ufc.raphael.languagenizer.aluno;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.persistence.Entity;

@Entity
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank
    private String nome;

    @NotBlank
    @Email(message = "Email inválido!")
    private String email;

    @Size(min = 8, max = 15, message = "Telefone inválido!")
    private String telefone;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Deprecated
    public Aluno() {
    }

    public Aluno(@NotBlank String nome,
            @NotBlank @Email(message = "Email inválido!") String email,
            @Size(min = 8, max = 15, message = "Telefone inválido!") String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    
}
