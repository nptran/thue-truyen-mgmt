package com.ptit.thuetruyenmgmt.model;

import lombok.*;
import org.thymeleaf.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "full_name")
public class FullName implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String fName;

    @Column(name = "last_name")
    private String lName;

    @Override
    public String toString() {
        String lNameStr = lName == null || lName.trim().isEmpty() ? "" : lName + " ";
        String fNameStr = fName == null || fName.trim().isEmpty() ? "" : fName;

        return StringUtils.capitalizeWords((lNameStr + fNameStr).trim());
    }
}
