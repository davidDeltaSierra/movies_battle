package com.ada.tech.movies_battle.user;

import com.ada.tech.movies_battle.common.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`user`")
public class User extends AbstractEntity {
    @Column
    private String username;

    @Column
    private String password;
}
