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
@DiscriminatorColumn(name="user_type",
        discriminatorType = DiscriminatorType.INTEGER)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(unique = true)
    protected String loginCode;

    @Column(name = "date_of_birth")
    protected LocalDate dob;

    @Column
    protected String email;

    @Column
    protected String phone;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "name_id")
    protected FullName fullName;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    protected Address address;

}
