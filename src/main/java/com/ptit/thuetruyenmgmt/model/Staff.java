package com.ptit.thuetruyenmgmt.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Staff extends User {

    @Column
    private String position;

    /**
     * Map sang staff trong {@link Bill#staff}
     */
    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Bill> bills;

    @Builder(builderMethodName = "staffBuilder")
    public Staff(int id, String loginCode, LocalDate dob, String email, String phone, FullName fullName, String position) {
        super(id, loginCode, dob, email, phone, fullName);
        this.position = position;
    }

}
