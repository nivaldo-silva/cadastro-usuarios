package io.github.nivaldosilva.cadastro_usuarios.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "telefones")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Telefone {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "numero", length = 15, nullable = false) 
    private String numero;

    @Column(name = "ddd", length = 3, nullable = false)
    private String ddd;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dataCriacao;

}
