// Allocation Result Class
public class AllocationResult {
    private final boolean success;
    private final int address;
    private final String message;

    public AllocationResult(boolean success, int address, String message) {
        this.success = success;
        this.address = address;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getAddress() {
        return address;
    }

    public String getMessage() {
        return message;
    }
}
