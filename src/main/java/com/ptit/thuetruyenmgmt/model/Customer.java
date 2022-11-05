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
@Table(name = "Customer")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private long cccd;

    @Column
    private String phone;

    @Column
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "name_id")
    private FullName fullName;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

}
