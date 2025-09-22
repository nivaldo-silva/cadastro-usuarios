package io.github.nivaldosilva.cadastro_usuarios.mappers;

import java.util.List;
import io.github.nivaldosilva.cadastro_usuarios.controllers.request.EnderecoRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.EnderecoResponse;
import io.github.nivaldosilva.cadastro_usuarios.entities.Endereco;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnderecoMapper {

    public static Endereco toEntity(EnderecoRequest request) {
        return Endereco.builder()
                .rua(request.rua())
                .numero(request.numero())
                .cidade(request.cidade())
                .complemento(request.complemento())
                .cep(request.cep())
                .estado(request.estado())
                .build();
    }

    public static EnderecoResponse toResponse(Endereco endereco) {
        return new EnderecoResponse(
                endereco.getId(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep(),
                endereco.getDataCriacao()
        );
    }

    public static List<EnderecoResponse> toResponseList(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(EnderecoMapper::toResponse)
                .toList();
    }
}