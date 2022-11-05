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
@Table(name = "full_name")
public class FullName implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String fName;

    @Column(name = "last_name")
    private String lName;

    @Override
    public String toString() {
        String fName_str = fName.trim().isEmpty() ? "" : fName;
        String lName_str = lName.trim().isEmpty() ? "" : lName + " ";

        return lName_str + fName_str;
    }
}
