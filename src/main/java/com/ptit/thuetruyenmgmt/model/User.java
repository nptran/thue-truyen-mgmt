package com.ptit.thuetruyenmgmt.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String loginCode;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    @Column
    private String email;

    @Column
    private String phone;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "name_id")
    private FullName fullName;

}
