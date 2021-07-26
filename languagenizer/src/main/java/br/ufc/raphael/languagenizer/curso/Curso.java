package br.ufc.raphael.languagenizer.curso;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.ufc.raphael.languagenizer.lingua.Lingua;
import br.ufc.raphael.languagenizer.nivel.Nivel;
import br.ufc.raphael.languagenizer.professor.Professor;
import lombok.Data;

@Data
@Entity
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank
    private String nome;

    @NotNull
    private int duração; //minutos

    @NotBlank
    @Size(max = 300, message = "Por favor, use uma descrição de até 300 caracteres.")
    private String descricao;

    @ManyToOne
    @NotNull
    Professor professor;

   // @OneToMany
    @ManyToOne
    @NotNull
    Lingua lingua;

    @ManyToOne
    @NotNull
    Nivel nivel;

    @Deprecated
    public Curso() {
    }

    public Curso(@NotBlank String nome, @NotNull int duração,
            @NotBlank @Size(max = 300, message = "Por favor, use uma descrição de até 300 caracteres.") String descricao,
            @NotNull Professor professor, @NotNull Lingua lingua, @NotNull Nivel nivel) {
        this.nome = nome;
        this.duração = duração;
        this.descricao = descricao;
        this.professor = professor;
        this.lingua = lingua;
        this.nivel = nivel;
    }
    
    
}
