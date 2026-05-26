import java.util.Date;

class Transaction {
    private int transactionId;
    private int orderId;
    private double amount;
    private String status;
    private Date date;

    public Transaction(int transactionId, int orderId, double amount, String status, Date date) {
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.date = date;
    }

    public int getTransactionId() { return transactionId; }
    public int getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getDate() { return date; }

    public String getTransactionDetails() {
        return "Transaction ID: " + transactionId + " | Order ID: " + orderId + " | Amount: €" + amount + " | Status: " + status;
    }
}

class Settlement {
    private int settlementId;
    private double totalAmount;
    private double commission;
    private double producerAmount;
    private String status;

    public Settlement(int settlementId, double totalAmount, double commission, double producerAmount, String status) {
        this.settlementId = settlementId;
        this.totalAmount = totalAmount;
        this.commission = commission;
        this.producerAmount = producerAmount;
        this.status = status;
    }

    public int getSettlementId() { return settlementId; }
    public double getTotalAmount() { return totalAmount; }
    public double getCommission() { return commission; }
    public double getProducerAmount() { return producerAmount; }
    public String getStatus() { return status; }
    public void updateStatus(String status) { this.status = status; }

    public double calculateNetAmount() {
        return this.totalAmount - this.commission;
    }
}

class SettlementReport {
    private int reportId;
    private Date createdAt;
    private String summary;

    public SettlementReport(int reportId, Date createdAt, String summary) {
        this.reportId = reportId;
        this.createdAt = createdAt;
        this.summary = summary;
    }

    public String generateReport() {
        return "[Report ID: " + reportId + " | Created At: " + createdAt + "]\n" + summary;
    }
}
