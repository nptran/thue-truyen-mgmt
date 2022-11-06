package com.ptit.thuetruyenmgmt.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PenaltyDTO implements Serializable {

    private int id;

    private String name;

    private String description;

    @Min(value = 0, message = "Mức Phí Phạt Tối Thiểu Là 500 Đồng")
    private double recommendedFee;

}
