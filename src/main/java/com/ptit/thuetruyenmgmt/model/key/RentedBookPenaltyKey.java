package com.ptit.thuetruyenmgmt.model.key;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RentedBookPenaltyKey implements Serializable {

    @Column(name = "rented_book_id")
	@NotNull
	private Integer rentedBookId;

	@Column(name = "penalty_id")
	@NotNull
	private Integer penaltyId;

}