package br.ufc.raphael.languagenizer.professor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CadastraProfessorRequest {
    
    @NotBlank
    String nome;

    @NotBlank
    @Email(message = "Email invalido.")
    String email;

    @Size(min = 8, max = 15, message = "Telefone invalido!")
    String telefone;

    @NotBlank
    @Size(max = 400, message = "Você excedeu o máximo de 400 caracteres na descrição!")
    String descricao;

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
    public CadastraProfessorRequest() {
    }

    public CadastraProfessorRequest(@NotBlank String nome, @NotBlank @Email(message = "Email invalido.") String email,
            @Size(min = 8, max = 15, message = "Telefone invalido!") String telefone,
            @NotBlank @Size(max = 400, message = "Você excedeu o máximo de 400 caracteres na descrição!") String descricao) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.descricao = descricao;
    }

    public Professor toModel(){
        return new Professor(this.nome, this.email, this.telefone, this.descricao);
    }
}
