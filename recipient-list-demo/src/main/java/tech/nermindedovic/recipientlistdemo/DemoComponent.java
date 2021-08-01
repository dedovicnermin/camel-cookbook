package tech.nermindedovic.recipientlistdemo;

import org.apache.camel.Header;

public class DemoComponent {
    public String process(@Header("paymentType") String paymentType) {
        switch (paymentType) {
            case "debit":
                return "mock:debit,mock:logger";
            case "credit":
                return "mock:credit,mock:logger";
            default:
                return "mock:error,mock:errorLogger";
        }
    }
}
