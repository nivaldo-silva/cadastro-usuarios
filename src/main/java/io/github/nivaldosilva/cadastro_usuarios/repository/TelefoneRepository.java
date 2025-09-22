package io.github.nivaldosilva.cadastro_usuarios.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.github.nivaldosilva.cadastro_usuarios.entities.Telefone;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, UUID> {

}
