package br.ufc.raphael.languagenizer.lingua;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinguaRepository extends JpaRepository<Lingua, Long>{
    public boolean existsByNome(String nome);

    Optional<Lingua> findByNome(String nome);
}
