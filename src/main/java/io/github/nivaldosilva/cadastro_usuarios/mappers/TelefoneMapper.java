package io.github.nivaldosilva.cadastro_usuarios.mappers;

import java.util.List;
import io.github.nivaldosilva.cadastro_usuarios.controllers.request.TelefoneRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.TelefoneResponse;
import io.github.nivaldosilva.cadastro_usuarios.entities.Telefone;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TelefoneMapper {

    public static Telefone toEntity(TelefoneRequest request) {
        return Telefone.builder()
                .numero(request.numero())
                .ddd(request.ddd())
                .build();
    }

    public static TelefoneResponse toResponse(Telefone telefone) {
        return new TelefoneResponse(
                telefone.getId(),
                telefone.getNumero(),
                telefone.getDdd(),
                telefone.getDataCriacao()
        );
    }

    public static List<TelefoneResponse> toResponseList(List<Telefone> telefones) {
        return telefones.stream()
                .map(TelefoneMapper::toResponse)
                .toList();
    }
}

