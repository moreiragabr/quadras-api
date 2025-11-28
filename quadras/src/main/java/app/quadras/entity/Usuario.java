package app.quadras.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Usuario implements UserDetails {

    public Usuario() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String senha;

    private String cidade;
    private String bairro;
    private String estado;
    private String rua;
    private String numeroCasa;
    private String cep;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"proprietario", "presidente", "jogadores"})
    private List<Quadra> quadras;

//    @ManyToMany(mappedBy = "usuariosCadastrados")
//    @JsonIgnoreProperties("usuariosCadastrados")
//    private List<HorarioDia> horarios;

//    @OneToMany(mappedBy = "presidente", cascade = CascadeType.REMOVE)
//    @JsonIgnoreProperties({"presidente", "jogadores"})
//    private List<Time> timesProprietarios;
//
//    @ManyToMany(mappedBy = "jogadores")
//    @JsonIgnoreProperties({"presidente", "jogadores"})
//    private List<Time> times;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonManagedReference("usuario-reserva") // Usa um valor para ligar com o 'BackReference'
    private List<Reserva> reservas;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Lógica: Se for ADMIN, tem permissão de ADMIN e de USER. Se não, só de USER.
        if (this.tipoUsuario == TipoUsuario.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getPassword() {
        return this.senha; // ⚠️ Retorne a senha do objeto
    }

    @Override
    public String getUsername() {
        return this.email; // ⚠️ Retorne o email (identificador único)
    }

    // Métodos de controle da conta (pode deixar true se não for gerenciar validade)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
