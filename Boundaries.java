import java.util.List;

class ManageClearanceScreen {
    private ManageSearchClass manageSearch;

    public ManageClearanceScreen(ManageSearchClass manageSearch) { this.manageSearch = manageSearch; }

    public void display() {
        System.out.println("[ManageClearanceScreen] UI: Ο Admin επιλέγει 'Εκκαθαρίσεις Πληρωμών'.");
    }

    public void selectOrderClearance(int orderId) {
        System.out.println("[ManageClearanceScreen] UI: Αναζήτηση εκκαθαρίσεων για Order ID: " + orderId);
        List<Settlement> results = manageSearch.searchSettlements(orderId);
        ClearanceDisplayScreen displayScreen = new ClearanceDisplayScreen(manageSearch);
        displayScreen.displaySettlements(results);
    }
}

class ClearanceDisplayScreen {
    private ManageSearchClass manageSearch;

    public ClearanceDisplayScreen(ManageSearchClass manageSearch) { this.manageSearch = manageSearch; }

    public void displaySettlements(List<Settlement> settlements) {
        System.out.println("[ClearanceDisplayScreen] UI: Λίστα συναλλαγών προς εκκαθάριση:");
        for (Settlement s : settlements) {
            System.out.println("   -> [Settlement ID: " + s.getSettlementId() + "] Ποσό: €" + s.getTotalAmount() + " | Κατάσταση: " + s.getStatus());
        }
    }

    public void triggerClearanceExecution(int settlementId) {
        System.out.println("[ClearanceDisplayScreen] UI: Ο Admin επιλέγει 'Εκτέλεση Εκκαθάρισης'.");
        manageSearch.startSettlement(settlementId);
    }
}

class ClearanceReportScreen {
    public void display(SettlementReport report) {
        System.out.println("[ClearanceReportScreen] UI: === ΟΘΟΝΗ ΑΝΑΦΟΡΑΣ ΕΚΚΑΘΑΡΙΣΗΣ ===");
        System.out.println(report.generateReport());
        System.out.println("[ClearanceReportScreen] UI: Ο Admin επιβεβαιώνει την ολοκλήρωση (ΤΕΛΟΣ).\n");
    }
}

class PaymentErrorScreen {
    public void display(String errorMessage) {
        System.out.println("[PaymentErrorScreen] UI ΣΦΑΛΜΑ: " + errorMessage);
        System.out.println("[PaymentErrorScreen] UI: Ειδοποίηση Admin για αποτυχία πληρωμής.\n");
    }
}

class MissingDataScreen {
    public void display(String errorMessage) {
        System.out.println("[MissingDataScreen] UI ΣΦΑΛΜΑ: " + errorMessage);
        System.out.println("[MissingDataScreen] UI: Ειδοποίηση Admin για ελλιπή στοιχεία παραγωγού.\n");
    }
}
