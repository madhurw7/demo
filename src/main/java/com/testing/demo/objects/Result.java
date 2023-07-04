package com.testing.demo.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.aerospike.mapping.Document;

@Document
@Data
@AllArgsConstructor
public class Result {
    private String id;
    private Boolean isServicable;
    private Long whId;
    private String sla;
}
