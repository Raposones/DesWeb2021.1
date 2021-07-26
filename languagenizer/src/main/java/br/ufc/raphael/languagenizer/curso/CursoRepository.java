package br.ufc.raphael.languagenizer.curso;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long>{
    public boolean existsByNome(String nome);

    Optional<Curso> findByNome(String nome);
}
