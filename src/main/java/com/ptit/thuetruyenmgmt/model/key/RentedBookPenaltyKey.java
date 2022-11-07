package com.ptit.thuetruyenmgmt.model.key;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RentedBookPenaltyKey implements Serializable {

    @Column(name = "rented_book_id")
	private Integer rentedBookId;

	@Column(name = "penalty_id")
	private Integer penaltyId;

}