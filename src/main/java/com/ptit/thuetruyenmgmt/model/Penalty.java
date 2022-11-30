package com.ptit.thuetruyenmgmt.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "penalty")
public class Penalty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    @Min(value = 0, message = "Mức Phí Phạt Tối Thiểu Là 500 Đồng")
    private double recommendedFee;

    /**
     * Map sang question trong {@link RentedBookPenalty#penalty}
     */
    @OneToMany(mappedBy = "penalty", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RentedBookPenalty> detectedPenalties;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Penalty penalty = (Penalty) o;
        return id != null && Objects.equals(id, penalty.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
