package com.ptit.thuetruyenmgmt.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@RequiredArgsConstructor
@Entity
//@Table(name = "staff")
@DiscriminatorValue("1")
public class Staff extends User {

    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'THU_NGAN'", nullable = false)
    @Enumerated(EnumType.STRING)
    private StaffPosition position;

    /**
     * Map sang staff trong {@link Bill#staff}
     */
    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Bill> bills;

    @Builder(builderMethodName = "staffBuilder")
    public Staff(Integer id, String loginCode, LocalDate dob, String email, String phone, FullName fullName, Address address, StaffPosition position) {
        super(id, loginCode, dob, email, phone, fullName, address);
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Staff staff = (Staff) o;
        return (id == null && ((Penalty) o).getId() == null) || getId() != null && Objects.equals(getId(), staff.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
