package com.dataart.secondmonth.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "audit_config")
public class AuditConfig {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "enabled")
    private Boolean enabled;

}
