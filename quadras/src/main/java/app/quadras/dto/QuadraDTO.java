package app.quadras.dto;

import app.quadras.entity.Quadra;

public record QuadraDTO(Long id, String nome, String endereco) {
    public static QuadraDTO fromQuadra(Quadra quadra) {
        String endereco = quadra.getRua() + ", " + quadra.getNumeroCasa();
        return new QuadraDTO(
                quadra.getId(),
                quadra.getNome(),
                endereco
        );
    }
}
