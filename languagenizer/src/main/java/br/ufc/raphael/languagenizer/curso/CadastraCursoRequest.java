package br.ufc.raphael.languagenizer.curso;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.ufc.raphael.languagenizer.lingua.Lingua;
import br.ufc.raphael.languagenizer.nivel.Nivel;
import br.ufc.raphael.languagenizer.professor.Professor;

public class CadastraCursoRequest {  
    
    @NotBlank
    String nome;

    @NotNull
    private int duração; //minutos

    @NotBlank
    @Size(max = 300, message = "Por favor, use uma descrição de até 300 caracteres.")
    private String descricao;

    @NotBlank
    private String emailprof;

    @NotBlank
    private String classe;

    @NotBlank
    private String linguagem;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDuração() {
        return duração;
    }

    public void setDuração(int duração) {
        this.duração = duração;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }



    public String getEmailprof() {
        return emailprof;
    }

    public void setEmailprof(String emailprof) {
        this.emailprof = emailprof;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getLinguagem() {
        return linguagem;
    }

    public void setLinguagem(String linguagem) {
        this.linguagem = linguagem;
    }

    @Deprecated
    public CadastraCursoRequest() {
    }

    public CadastraCursoRequest(@NotBlank String nome, @NotNull int duração,
            @NotBlank @Size(max = 300, message = "Por favor, use uma descrição de até 300 caracteres.") String descricao,
            @NotBlank String emailprof,@NotBlank String linguagem, @NotBlank String classe) {
        this.nome = nome;
        this.duração = duração;
        this.descricao = descricao;
        this.emailprof = emailprof;
        this.classe = classe;
        this.linguagem = linguagem;
    }

    public Curso toModel(Professor professor, Lingua lingua, Nivel nivel){
        return new Curso(this.nome, this.duração, this.descricao, professor, lingua, nivel);
    }

}