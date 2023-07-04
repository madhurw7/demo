package com.testing.demo.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@Document
public class Input {
    @Id
    private String cacheKey;
    private Long whId;
    private Long pinCode;

    public Input(Long whId, Long pinCode) {
        this.whId = whId;
        this.pinCode = pinCode;
        this.cacheKey = "" + whId + "_" + pinCode;
    }
}
