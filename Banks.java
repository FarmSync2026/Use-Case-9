class PlatformBank {
    public boolean credit(double commission) {
        System.out.println("[PlatformBank] Πίστωση προμήθειας πλατφόρμας: €" + commission);
        return true;
    }
}

class ProducerBank {
    private boolean simulateFailure = false;
    private boolean simulateMissingData = false;

    public void setSimulateFailure(boolean simulateFailure) { this.simulateFailure = simulateFailure; }
    public void setSimulateMissingData(boolean simulateMissingData) { this.simulateMissingData = simulateMissingData; }

    public boolean hasMissingData() {
        return simulateMissingData;
    }

    public boolean credit(double producerAmount) {
        if (simulateMissingData) {
            System.out.println("[ProducerBank] ΣΦΑΛΜΑ ΤΡΑΠΕΖΑΣ: Τα τραπεζικά στοιχεία του παραγωγού είναι ελλιπή/λανθασμένα.");
            return false;
        }
        if (simulateFailure) {
            System.out.println("[ProducerBank] ΣΦΑΛΜΑ ΤΡΑΠΕΖΑΣ: Αποτυχία σύνδεσης ή επεξεργασίας της πίστωσης.");
            return false;
        }
        System.out.println("[ProducerBank] Επιτυχής πίστωση καθαρού ποσού στον παραγωγό: €" + producerAmount);
        return true;
    }
}

class CustomerBank {
    public boolean refund(double totalAmount) {
        System.out.println("[CustomerBank] Επιτυχής επιστροφή χρημάτων (Refund) στον πελάτη: €" + totalAmount);
        return true;
    }
}