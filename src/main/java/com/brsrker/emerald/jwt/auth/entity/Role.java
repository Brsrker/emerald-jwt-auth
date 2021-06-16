package com.brsrker.emerald.jwt.auth.entity;

import com.brsrker.emerald.jwt.auth.util.entity.BaseAuditedLogEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseAuditedLogEntity<String> implements Serializable {

    @Column
    private String name;
    @JsonBackReference
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
    @JsonBackReference
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "roles_privileges",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges = new HashSet<>();

    public Role addPrivilege(Privilege privileges) {
        if (this.privileges == null) {
            this.privileges = new HashSet<>();
        }
        this.privileges.add(privileges);
        return this;
    }

    public Role removePrivilege(Privilege privileges) {
        if (this.privileges == null) {
            this.privileges = new HashSet<>();
        }
        this.privileges.remove(privileges);
        return this;
    }

}
