package br.ufc.raphael.languagenizer.nivel;

import javax.validation.constraints.NotBlank;

public class CadastraNivelRequest {
    
    @NotBlank
    String classe;

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    @Deprecated
    public CadastraNivelRequest() {
    }

    public CadastraNivelRequest(@NotBlank String classe) {
        this.classe = classe;
    }

    public Nivel toModel(){
        return new Nivel(this.classe);
    }
}
