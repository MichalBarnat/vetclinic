package com.powtorka.vetclinic.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
// todo to moglby byc enum (user/admin)
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    public Role(String name) {
        this.name = name;
    }
    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }

    // uzytkownik moze miec dwie role: zwykly uzytkownik oraz admin
    // zwykly uzytkownik ma miec privilege : READ - bezdie mogl tylko uzywac endpointow czytajacych dane
    // uzytkownik z rola admin moze tak jak zwykly user czytac, czyli tez ma privilege READ,
    // ale oprocz tego moze tez zapisywac/modyfikowac - oprocz privilege READ ma miec tez privilege WRITE
}
