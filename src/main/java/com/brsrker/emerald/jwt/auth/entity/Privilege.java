package com.brsrker.emerald.jwt.auth.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.brsrker.emerald.jwt.auth.util.entity.BaseAuditedLogEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "privileges")
public class Privilege extends BaseAuditedLogEntity<String> implements Serializable {

    @Column
    private String name;
    @JsonBackReference
    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles = new HashSet<>();

}
