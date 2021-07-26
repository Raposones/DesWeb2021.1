package br.ufc.raphael.languagenizer.professor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long>{
    public boolean existsByEmail(String email);

    Optional<Professor> findByEmail(String email);

    @Modifying
    @Query("update Professor p set p.nome = ?1, p.telefone = ?2, p.descricao = ?4 where p.email = ?3")
    public void updateProfessorByEmail(String nome, String tel, String email, String descricao);
}
