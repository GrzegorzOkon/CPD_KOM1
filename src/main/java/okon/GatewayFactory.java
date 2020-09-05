package okon;

public class GatewayFactory {
    public static Gateway make(Job job) {
        Gateway result = null;
        if (isSybaseVendor(job)) {
            result = new GatewaySybase(job);
        } else if (isMicrosoftVendor(job)) {
            result = new GatewayMicrosoft(job);
        }
        return result;
    }

    private static boolean isSybaseVendor(Job job) {
        if (job.getVendor().toLowerCase().contains("sybase"))
            return true;
        return false;
    }

    private static boolean isMicrosoftVendor(Job job) {
        if (job.getVendor().toLowerCase().contains("microsoft"))
            return true;
        return false;
    }
}
