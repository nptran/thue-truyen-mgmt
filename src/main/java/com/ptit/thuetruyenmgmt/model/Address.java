package com.ptit.thuetruyenmgmt.model;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

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
    private Integer id;

    @Column
    private String street;

    @Column
    private String district;

    @Column
    private String city;

    @Override
    public String toString() {
        String streetStr = street == null || street.trim().isEmpty() ? "" : street.trim() + ", ";
        String districtStr = district == null || district.trim().isEmpty() ? "" : district.trim() + ", ";
        String cityStr = city == null || city.trim().isEmpty() ? "" : city.trim();

        return StringUtils.strip((streetStr + districtStr + cityStr).trim(), ",");
    }
}
