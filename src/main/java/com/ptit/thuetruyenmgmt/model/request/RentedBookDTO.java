package com.ptit.thuetruyenmgmt.model.request;

import com.ptit.thuetruyenmgmt.model.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentedBookDTO implements Serializable {

    private int customerId;

    private int rentedBookId;

    private String code;

    private String titleName;

    private LocalDateTime rentedTime;

    private double amount;

    private List<Penalty> penalties;

}
