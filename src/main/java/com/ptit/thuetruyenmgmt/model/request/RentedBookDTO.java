package com.ptit.thuetruyenmgmt.model.request;

import com.ptit.thuetruyenmgmt.model.*;
import lombok.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RentedBookDTO implements Serializable {

    private int customerId;

    private int rentedBookId;

    private String code;

    private String titleName;

    private LocalDateTime rentedTime;

    private double amount;

    @Valid
    private List<Penalty> penalties;

}
