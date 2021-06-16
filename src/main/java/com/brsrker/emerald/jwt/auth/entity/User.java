package com.brsrker.emerald.jwt.auth.entity;

import com.brsrker.emerald.jwt.auth.util.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "users")
public class User extends BaseEntity<String> implements Serializable {

    @Column
    private String loginId;
    @Column
    @JsonIgnore
    private String password;
    @Column
    private LocalDateTime lastLogin;
    @Column
    private Boolean enabled;
    @Column
    private Boolean locked;
    @Column
    private Boolean expire;
    @Column
    private LocalDateTime expireAt;
    @JsonBackReference
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role){
        if(this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        if(this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.remove(role);
    }

}