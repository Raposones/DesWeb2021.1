package br.ufc.raphael.languagenizer.lingua;

import javax.validation.constraints.NotBlank;

public class CadastraLinguaRequest {
    
    @NotBlank
    String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Deprecated
    public CadastraLinguaRequest() {
    }

    public CadastraLinguaRequest(@NotBlank String nome) {
        this.nome = nome;
    }

    public Lingua toModel(){
        return new Lingua(this.nome);
    }
}
