package com.ptit.thuetruyenmgmt.model;

import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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
	private RentedBookPenaltyKey key;

    /**
     * Map sang {@link java.util.List} {@link RentedBook#penalties} trong {@link RentedBook}
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rentedBookId")
    private RentedBook rentedBook;

    /**
     * Map sang {@link java.util.List} {@link Penalty#detectedPenalties} trong {@link Penalty}
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("penaltyId")
    private Penalty penalty;

    @Column
    private double fee;

    // Được coi là trùng nhau khi trùng BookId và Penalty Id
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof RentedBookPenalty)
                && ((RentedBookPenalty) obj).getRentedBook().equals(this.rentedBook.getId())
                && ((RentedBookPenalty) obj).getPenalty().getId().equals(this.penalty.getId());
    }

    @Override
    public int hashCode() {
        return this.rentedBook.getId().hashCode() + this.penalty.getId().hashCode();
    }
}
