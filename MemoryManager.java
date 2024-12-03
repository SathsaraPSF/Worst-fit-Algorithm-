
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Memory Manager implementing Worst Fit Algorithm
class MemoryManager {
    private final List<MemoryBlock> memoryBlocks;

    public MemoryManager(int totalMemorySize) {
        this.memoryBlocks = new ArrayList<>();
        // Initial free block
        memoryBlocks.add(new MemoryBlock(0, totalMemorySize, false));
    }

    public synchronized AllocationResult allocateMemory(int requestSize) {
        // Find the worst (largest) free block
        int worstBlockIndex = findWorstFreeBlock();

        if (worstBlockIndex == -1) {
            return new AllocationResult(false, -1, "No suitable block found");
        }

        MemoryBlock worstBlock = memoryBlocks.get(worstBlockIndex);

        // Check if block is large enough
        if (worstBlock.getSize() < requestSize) {
            return new AllocationResult(false, -1, "Insufficient memory");
        }

        // Allocate memory
        int allocatedAddress = worstBlock.getStart();

        // If block is exactly the right size
        if (worstBlock.getSize() == requestSize) {
            worstBlock.setAllocated(true);
        }
        // If block is larger, split it
        else {
            // Create allocated block
            MemoryBlock allocatedBlock = new MemoryBlock(
                    worstBlock.getStart(),
                    requestSize,
                    true);

            // Update original block
            worstBlock.setStart(worstBlock.getStart() + requestSize);
            worstBlock.setSize(worstBlock.getSize() - requestSize);

            // Insert allocated block
            memoryBlocks.add(worstBlockIndex, allocatedBlock);
        }

        return new AllocationResult(true, allocatedAddress, "Allocation successful");
    }

    private int findWorstFreeBlock() {
        int worstBlockIndex = -1;
        int largestFreeBlockSize = -1;

        for (int i = 0; i < memoryBlocks.size(); i++) {
            MemoryBlock block = memoryBlocks.get(i);
            if (!block.isAllocated() && block.getSize() > largestFreeBlockSize) {
                worstBlockIndex = i;
                largestFreeBlockSize = block.getSize();
            }
        }

        return worstBlockIndex;
    }

    public synchronized boolean freeMemory(int address) {
        for (MemoryBlock block : memoryBlocks) {
            if (block.isAllocated() && block.getStart() == address) {
                block.setAllocated(false);
                mergeAdjacentFreeBlocks();
                return true;
            }
        }
        return false;
    }

    private void mergeAdjacentFreeBlocks() {
        // Sort blocks by start address
        memoryBlocks.sort(Comparator.comparingInt(MemoryBlock::getStart));

        // Merge adjacent free blocks
        for (int i = 0; i < memoryBlocks.size() - 1;) {
            MemoryBlock current = memoryBlocks.get(i);
            MemoryBlock next = memoryBlocks.get(i + 1);

            if (!current.isAllocated() && !next.isAllocated()) {
                // Merge blocks
                current.setSize(current.getSize() + next.getSize());
                memoryBlocks.remove(i + 1);
            } else {
                i++;
            }
        }
    }

    public List<MemoryBlock> getMemoryBlocks() {
        return memoryBlocks;
    }
}
