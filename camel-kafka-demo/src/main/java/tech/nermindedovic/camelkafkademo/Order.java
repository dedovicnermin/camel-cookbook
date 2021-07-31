package tech.nermindedovic.camelkafkademo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    private long orderId;
    private String productName;
    private double total;
    private boolean processed;
}
