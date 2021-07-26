package br.ufc.raphael.languagenizer.nivel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NivelRepository extends JpaRepository<Nivel, Long>{
    public boolean existsByClasse(String classe);

    Optional<Nivel> findByClasse(String classe);
}
