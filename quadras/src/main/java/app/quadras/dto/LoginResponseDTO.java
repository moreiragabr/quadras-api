package app.quadras.dto;

public record LoginResponseDTO(String token, Long id, String nome, String email, String role) {}
