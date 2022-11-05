package com.ptit.thuetruyenmgmt.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String street;

    @Column
    private String district;

    @Column
    private String city;

    @Override
    public String toString() {
        String street_str = street.trim().isEmpty() ? "" : street.trim() + ", ";
        String district_str = district.trim().isEmpty() ? "" : district.trim() + ", ";
        String city_str = city.trim().isEmpty() ? "" : city.trim();

        return street_str + district_str + city_str;
    }
}
