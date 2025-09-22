package io.github.nivaldosilva.cadastro_usuarios.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.nivaldosilva.cadastro_usuarios.entities.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {

}
