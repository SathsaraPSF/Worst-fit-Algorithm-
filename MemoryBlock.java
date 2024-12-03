// Memory Block Representation
public class MemoryBlock {
    private int start;
    private int size;
    private boolean isAllocated;

    public MemoryBlock(int start, int size, boolean isAllocated) {
        this.start = start;
        this.size = size;
        this.isAllocated = isAllocated;
    }

    // Getters and setters
    public int getStart() {
        return start;
    }

    public int getSize() {
        return size;
    }

    public boolean isAllocated() {
        return isAllocated;
    }

    public void setAllocated(boolean allocated) {
        isAllocated = allocated;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
