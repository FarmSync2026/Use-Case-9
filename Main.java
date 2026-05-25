public class FarmSyncUseCase9 {
    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println("FARMSYNC SYSTEM - RUNNING USE CASE 9");
        System.out.println("=========================================================================\n");

        DBManager dbManager = new DBManager();
        ProducerBank producerBank = new ProducerBank();

        ManageSearchClass manageSearch = new ManageSearchClass(dbManager, producerBank);
        manageSearch.init();

        ManageClearanceScreen mainScreen = new ManageClearanceScreen(manageSearch);
        ClearanceDisplayScreen displayScreen = new ClearanceDisplayScreen(manageSearch);

        System.out.println(">>> ΣΕΝΑΡΙΟ Α: ΒΑΣΙΚΗ ΡΟΗ (Επιτυχής Πληρωμή)");
        mainScreen.display();
        mainScreen.selectOrderClearance(5001);
        displayScreen.triggerClearanceExecution(1001);

        System.out.println("\n>>> ΣΕΝΑΡΙΟ Β: ΕΝΑΛΛΑΚΤΙΚΗ ΡΟΗ Α1 (Αποτυχία Πληρωμής)");
        producerBank.setSimulateFailure(true);
        mainScreen.display();
        mainScreen.selectOrderClearance(5001);
        displayScreen.triggerClearanceExecution(1001);
        producerBank.setSimulateFailure(false);

        System.out.println("\n>>> ΣΕΝΑΡΙΟ Γ: ΕΝΑΛΛΑΚΤΙΚΗ ΡΟΗ Α2 (Ελλιπή Στοιχεία)");
        producerBank.setSimulateMissingData(true);
        mainScreen.display();
        mainScreen.selectOrderClearance(5001);
        displayScreen.triggerClearanceExecution(1001);
    }
}