package com.myxiaowang.logistics.pojo.QueryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月24日 09:47:00
 */
@Data
public class PayOrderQuery {
    private String userId;
    private String orderId;
}
