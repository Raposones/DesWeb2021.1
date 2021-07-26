package br.ufc.raphael.languagenizer.aluno;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long>{
    public boolean existsByEmail(String email);

    Optional<Aluno> findByEmail(String email);

    @Modifying
    @Query("update Aluno a set a.nome = ?1, a.telefone = ?2 where a.email = ?3")
    void updateAlunoByEmail(String nome, String telefone, String email);
}
