import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class DBManager {
    private Transaction currentTransaction;
    private ProducerBank connectedProducerBank;

    public void setConnectedProducerBank(ProducerBank bank) {
        this.connectedProducerBank = bank;
    }

    public List<Transaction> retrieveCompletedTransactions() {
        System.out.println("[DBManager] DB: Ανάκτηση ολοκληρωμένων συναλλαγών προς εκκαθάριση.");
        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction(901, 5001, 250.0, "Completed", new Date()));
        return list;
    }

    public Settlement retrieveSettlementData(int settlementId) {
        System.out.println("[DBManager] DB: Ανάκτηση δεδομένων εκκαθάρισης για Settlement ID: " + settlementId);
        if (currentTransaction != null) {
            double amount = currentTransaction.getAmount();
            double comm = amount * 0.10;
            return new Settlement(settlementId, amount, comm, amount - comm, "Pending");
        }
        return new Settlement(settlementId, 250.0, 25.0, 225.0, "Pending");
    }

    public boolean verification(Settlement settlement) {
        System.out.println("[DBManager] DB: Έλεγχος πληρότητας/εγκυρότητας στοιχείων εκκαθάρισης.");
        if (connectedProducerBank != null && connectedProducerBank.hasMissingData()) {
            System.out.println("[DBManager] DB: Ανίχνευση ελλιπών τραπεζικών στοιχείων παραγωγού.");
            return false;
        }
        return settlement.getProducerAmount() > 0;
    }

    public void updateSettlement(Settlement settlement) {
        System.out.println("[DBManager] DB: Ενημέρωση κατάστασης εκκαθάρισης σε: " + settlement.getStatus());
        if (currentTransaction != null && "Settled".equals(settlement.getStatus())) {
            currentTransaction.setStatus("Εκκαθαρισμένη");
            System.out.println("[DBManager] DB: Η κατάσταση της Συναλλαγής " + currentTransaction.getTransactionId() + " άλλαξε σε: 'Εκκαθαρισμένη'");
        } else if (currentTransaction != null && "Failed".equals(settlement.getStatus())) {
            currentTransaction.setStatus("Σε εκκρεμότητα");
            System.out.println("[DBManager] DB: Η κατάσταση της Συναλλαγής " + currentTransaction.getTransactionId() + " παρέμεινε: 'Σε εκκρεμότητα'");
        }
    }

    public void orderCancellation(int orderId) {
        System.out.println("[DBManager] DB: Ακύρωση παραγγελίας με Order ID: " + orderId + " λόγω ελλιπών στοιχείων.");
    }

    public void setCurrentTransaction(Transaction tx) {
        this.currentTransaction = tx;
    }
}