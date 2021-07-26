package br.ufc.raphael.languagenizer.professor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Professor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank
    private String nome;

    @NotBlank
    @Email(message = "Email invalido.")
    private String email;

    @Size(min = 8, max = 15, message = "Telefone invalido!")
    private String telefone;

    @NotBlank
    @Size(max = 400, message = "Você excedeu o máximo de 400 caracteres na descrição!")
    private String descricao;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Deprecated
    public Professor() {
    }

    public Professor(@NotBlank String nome, @NotBlank @Email(message = "Email invalido.") String email,
            @Size(min = 8, max = 15, message = "Telefone invalido!") String telefone,
            @NotBlank @Size(max = 400, message = "Você excedeu o máximo de 400 caracteres na descrição!") String descricao) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.descricao = descricao;
    }

    
}
