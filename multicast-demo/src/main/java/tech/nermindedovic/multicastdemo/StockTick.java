package tech.nermindedovic.multicastdemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockTick {
    private String symbol;
    private double price;
    private boolean processed;
}
