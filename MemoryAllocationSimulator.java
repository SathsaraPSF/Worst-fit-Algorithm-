
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

// Main UI Class
class MemoryAllocationSimulator extends JFrame {
    private final MemoryManager memoryManager;
    private JTextField allocSizeField;
    private JTextField freeAddressField;
    private JTextArea outputArea;
    private MemoryVisualizationPanel visualizationPanel;

    public MemoryAllocationSimulator() {
        // Initialize memory manager with 1000 bytes
        memoryManager = new MemoryManager(1000);

        // Set up the frame
        setTitle("Worst Fit Memory Allocation Simulator");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create components
        createComponents();

        // Add components to frame
        setLocationRelativeTo(null);
    }

    private void createComponents() {
        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 5, 10, 20));

        // Customize the TitledBorder with a larger font
        TitledBorder border = BorderFactory.createTitledBorder("Memory Operations");
        Font currentFont = border.getTitleFont();
        Font newFont = currentFont.deriveFont(20f);
        border.setTitleFont(newFont);
        inputPanel.setBorder(border);

        // Add an empty border for margins
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                border, // Outer border
                BorderFactory.createEmptyBorder(50, 40, 50, 40) // Top, Left, Bottom, Right margins
        ));

        // Allocation Section
        JLabel allocationSizeLabel = new JLabel("Allocation Size (bytes):");
        Font allocationSizeLabelFont = allocationSizeLabel.getFont();
        Font newAllocationSizeLabelFont = allocationSizeLabelFont.deriveFont(20f);
        allocationSizeLabel.setFont(newAllocationSizeLabelFont);

        inputPanel.add(allocationSizeLabel);
        allocSizeField = new JTextField(20);
        allocSizeField.setFont(new Font("Arial", Font.BOLD, 18));
        allocSizeField.setBorder(BorderFactory.createCompoundBorder(
                allocSizeField.getBorder(),
                BorderFactory.createEmptyBorder(0, 10, 0, 5)));
        inputPanel.add(allocSizeField);

        // Allocate Button
        JButton allocateButton = new JButton("Allocate");
        Font allocateButtonText = allocateButton.getFont();
        allocateButton.setFont(allocateButtonText.deriveFont(18f));
        allocateButton.addActionListener(e -> allocateMemory());
        inputPanel.add(allocateButton);

        // Free Address Section
        JLabel freeAddressLabel = new JLabel("Address to Free:");
        Font freeAddressLabelFont = freeAddressLabel.getFont();
        Font newFreeAddressLabelFont = freeAddressLabelFont.deriveFont(20f);
        freeAddressLabel.setFont(newFreeAddressLabelFont);

        inputPanel.add(freeAddressLabel);
        freeAddressField = new JTextField();
        freeAddressField.setFont(new Font("Arial", Font.BOLD, 18));
        freeAddressField.setBorder(BorderFactory.createCompoundBorder(
                freeAddressField.getBorder(),
                BorderFactory.createEmptyBorder(0, 10, 0, 5)));
        freeAddressField.setPreferredSize(new Dimension(100, 50));
        inputPanel.add(freeAddressField);

        // Free Button
        JButton freeButton = new JButton("Free");
        Font freeButtonText = freeButton.getFont();
        freeButton.setFont(freeButtonText.deriveFont(18f));
        freeButton.addActionListener(e -> freeMemory());
        inputPanel.add(freeButton);

        // Output Area
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Visualization Panel
        visualizationPanel = new MemoryVisualizationPanel(memoryManager);

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(visualizationPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void allocateMemory() {
        try {
            int size = Integer.parseInt(allocSizeField.getText());
            AllocationResult result = memoryManager.allocateMemory(size);

            outputArea.append(result.getMessage() +
                    (result.isSuccess() ? " at address " + result.getAddress() : "") + "\n");

            // Update visualization
            visualizationPanel.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number");
        }
    }

    private void freeMemory() {
        try {
            int address = Integer.parseInt(freeAddressField.getText());
            boolean freed = memoryManager.freeMemory(address);
            System.out.println(address);

            outputArea.append(freed
                    ? "Memory at " + address + " freed successfully\n"
                    : "Could not free memory at " + address + "\n");

            // Update visualization
            visualizationPanel.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid address");
        }
    }

    // Memory Visualization Panel
    private static class MemoryVisualizationPanel extends JPanel {
        private final MemoryManager memoryManager;

        public MemoryVisualizationPanel(MemoryManager memoryManager) {
            this.memoryManager = memoryManager;
            setPreferredSize(new Dimension(800, 800));

            // Customize the TitledBorder with a larger font
            TitledBorder border = BorderFactory.createTitledBorder("Memory Blocks Visualization");
            Font currentFont = border.getTitleFont();
            Font newFont = currentFont.deriveFont(20f);
            border.setTitleFont(newFont);
            setBorder(border);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            int panelWidth = getWidth();
            int totalMemorySize = 1000; // Total memory size

            // Draw each memory block
            int x = 0;
            for (MemoryBlock block : memoryManager.getMemoryBlocks()) {
                // Calculate block width proportional to its size
                int blockWidth = (int) ((double) block.getSize() / totalMemorySize * panelWidth);

                // Color based on allocation status
                g2d.setColor(block.isAllocated() ? Color.RED : Color.GREEN);
                g2d.fillRect(x, 30, blockWidth, 50);

                // Block details
                g2d.setColor(Color.BLACK);
                g2d.drawString(
                        (block.isAllocated() ? "Allocated" : "Free") +
                                " (Start: " + block.getStart() +
                                ", Size: " + block.getSize() + ")",
                        x, 100);

                x += blockWidth;
            }
        }
    }

    public static void main(String[] args) {
        // Run on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MemoryAllocationSimulator().setVisible(true);
        });
    }
}