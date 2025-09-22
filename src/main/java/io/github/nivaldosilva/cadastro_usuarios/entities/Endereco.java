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
@Table(name = "enderecos")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Endereco {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "rua", nullable = false)
    private String rua;

    @Column(name = "numero", length = 10) 
    private String numero;

    @Column(name = "complemento", length = 100) 
    private String complemento;

    @Column(name = "cidade", length = 150, nullable = false)
    private String cidade;

    @Column(name = "estado", length = 2, nullable = false)
    private String estado;

    @Column(name = "cep", length = 9, nullable = false)
    private String cep;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dataCriacao;
    

}
