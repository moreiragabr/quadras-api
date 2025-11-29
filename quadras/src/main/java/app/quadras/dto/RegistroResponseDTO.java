package app.quadras.dto;

import app.quadras.entity.TipoUsuario;

public record RegistroResponseDTO(
        Long id,
        String nome,
        String email,
        TipoUsuario tipoUsuario,
        String cidade,
        String bairro,
        String estado,
        String rua,
        String numeroCasa,
        String cep
) {}
