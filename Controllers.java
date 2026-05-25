import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ManageSearchClass {
    private DBManager dbManager;
    private ProducerBank producerBank;

    public ManageSearchClass(DBManager dbManager, ProducerBank producerBank) {
        this.dbManager = dbManager;
        this.producerBank = producerBank;
    }

    public void init() {
        System.out.println("[ManageSearchClass] Controller: Αρχικοποίηση αναζήτησης εκκαθαρίσεων.");
    }

    public List<Settlement> searchSettlements(int orderId) {
        System.out.println("[ManageSearchClass] Controller: Αναζήτηση εκκαθαρίσεων για Order ID: " + orderId);
        List<Transaction> txList = dbManager.retrieveCompletedTransactions();
        List<Settlement> settlements = new ArrayList<>();
        
        for (Transaction tx : txList) {
            dbManager.setCurrentTransaction(tx);
            double amount = tx.getAmount();
            double comm = amount * 0.10;
            Settlement s = new Settlement(tx.getTransactionId() + 100, amount, comm, amount - comm, "Pending");
            settlements.add(s);
        }
        return settlements;
    }

    public void startSettlement(int settlementId) {
        System.out.println("[ManageSearchClass] Controller: Εκκίνηση εκκαθάρισης για Settlement ID: " + settlementId);

        List<Transaction> txList = dbManager.retrieveCompletedTransactions();
        if (!txList.isEmpty()) {
            dbManager.setCurrentTransaction(txList.get(0));
        }

        Settlement settlement = dbManager.retrieveSettlementData(settlementId);

        ManageSettlementClass settlementController = new ManageSettlementClass(dbManager, producerBank);

        settlementController.calculateTotalAmount(settlement);
        settlementController.calculateCommission(settlement);
        settlementController.calculateProducerAmount(settlement);
        
        settlementController.processSettlementFlow(settlement);
    }
}

class ManageSettlementClass {
    private DBManager dbManager;
    private PlatformBank platformBank = new PlatformBank();
    private ProducerBank producerBank;
    private CustomerBank customerBank = new CustomerBank();

    public ManageSettlementClass(DBManager dbManager, ProducerBank producerBank) {
        this.dbManager = dbManager;
        this.producerBank = producerBank;
    }

    public double calculateTotalAmount(Settlement s) { return s.getTotalAmount(); }
    public double calculateCommission(Settlement s) { return s.getCommission(); }
    public double calculateProducerAmount(Settlement s) { return s.getProducerAmount(); }

    public void processSettlementFlow(Settlement settlement) {
        platformBank.credit(calculateCommission(settlement));

        dbManager.setConnectedProducerBank(producerBank);
        boolean hasValidData = dbManager.verification(settlement);
        
        if (!hasValidData) {
            System.out.println("[ManageSettlementClass] Ροή: [Insufficient Data] -> Εκτέλεση εναλλακτικής ροής Α2.");
            dbManager.orderCancellation(5001);
            customerBank.refund(calculateTotalAmount(settlement));
            
            MissingDataScreen missingScreen = new MissingDataScreen();
            missingScreen.display("Σφάλμα: Ελλιπή τραπεζικά στοιχεία παραγωγού. Έγινε επιστροφή χρημάτων στον πελάτη.");
            return;
        }

        System.out.println("[ManageSettlementClass] Ροή: [Verification Success] -> Απόπειρα πληρωμής.");
        boolean creditSuccess = producerBank.credit(calculateProducerAmount(settlement));

        if (creditSuccess) {
            settlement.updateStatus("Settled");
            dbManager.updateSettlement(settlement);
            
            String summaryText = "Εκκαθάριση Επιτυχής.\nΣυνολικό: €" + calculateTotalAmount(settlement) + 
                                 "\nΠρομήθεια: €" + calculateCommission(settlement) + 
                                 "\nΚαθαρό Παραγωγού: €" + calculateProducerAmount(settlement);
            SettlementReport settlementReport = new SettlementReport(1001, new Date(), summaryText);
            
            ClearanceReportScreen reportScreen = new ClearanceReportScreen();
            reportScreen.display(settlementReport);
        } else {
            System.out.println("[ManageSettlementClass] Ροή: [Payment Failure] -> Εκτέλεση εναλλακτικής ροής Α1.");
            settlement.updateStatus("Failed");
            dbManager.updateSettlement(settlement);
            
            PaymentErrorScreen errorScreen = new PaymentErrorScreen();
            errorScreen.display("Η τραπεζική συναλλαγή απέτυχε. Η κατάσταση επανήλθε σε εκκρεμότητα.");
        }
    }
}