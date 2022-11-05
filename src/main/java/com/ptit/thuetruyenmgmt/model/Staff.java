package com.ptit.thuetruyenmgmt.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Staff extends User {

    @Column
    private String position;

    /**
     * Map sang staff trong {@link Bill#staff}
     */
    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Bill> bills;

}
