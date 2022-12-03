package com.ptit.thuetruyenmgmt.model;

import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rented_book_penalty")
public class RentedBookPenalty implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private RentedBookPenaltyKey id;

    /**
     * Map sang {@link List} {@link RentedBook#penalties} trong {@link RentedBook}
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rentedBookId")
    private RentedBook rentedBook;

    /**
     * Map sang {@link List} {@link Penalty#detectedPenalties} trong {@link Penalty}
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("penaltyId")
    private Penalty penalty;

    @Column
    private double fee;

    // Được coi là trùng nhau khi trùng BookId và Penalty Id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RentedBookPenalty that = (RentedBookPenalty) o;
        return (id == null && ((Penalty) o).getId() == null) ||  id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
