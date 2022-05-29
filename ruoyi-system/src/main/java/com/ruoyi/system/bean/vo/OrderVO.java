package com.ruoyi.system.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author Jandmla
 * @version 1.0
 * @description TODO
 * @data 2022/2/23 16:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class OrderVO {

    private String time;

    private String year;

    private String month;

    private String day;

    private Integer moneyNum;

    private String timestamp;

    private BigDecimal money;
}
