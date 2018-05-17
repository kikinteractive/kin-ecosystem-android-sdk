package com.kin.ecosystem.data.model;

/**
 * Payment object, after sending a blockchain transaction as payment to an order.
 * Determine if the payment succeeded and for which orderID it connected.
 */
public class Payment {

    /**
     * Order id that the transaction is related to.
     */
    private String orderID;

    /**
     * The transaction id on the blockchain, could be null if the transaction failed.
     */
    private String transactionID;

    /**
     * Determine if the transaction succeeded or not.
     */
    private boolean isSucceed;

    /**
     * Result message:
     *      success - Payment with {@code transactionID}: succeeded
     *      failed - An error message from the network.
     */
    private String resultMessage;

    public Payment(String orderID, String transactionID) {
        this.orderID = orderID;
        this.transactionID = transactionID;
        this.isSucceed = true;
        this.resultMessage = String.format("Payment with transaction id: %s succeeded", transactionID);
    }

    public Payment(String orderID, boolean isSucceed, String resultMessage) {
        this.orderID = orderID;
        this.transactionID = null;
        this.isSucceed = isSucceed;
        this.resultMessage = resultMessage;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public boolean isSucceed() {
        return isSucceed;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}
