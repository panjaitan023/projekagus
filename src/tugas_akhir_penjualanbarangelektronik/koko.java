/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugas_akhir_penjualanbarangelektronik;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class koko extends JFrame {
    private JTabbedPane tabbedPane;

    // Components for Sales Entry Tab
    private JTextField txtProductName;
    private JTextField txtQuantity;
    private JTextField txtPrice;
    private JButton btnAdd;
    private JTable salesTable;
    private DefaultTableModel salesTableModel;

    // Components for Invoice / Report Tab
    private JTextArea invoiceArea;
    private JButton btnGenerateInvoice;
    private JButton btnClearInvoice;

    public koko() {
        setTitle("Aplikasi Penjualan - Sales Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // Sales Entry Tab
        JPanel salesEntryPanel = new JPanel(new BorderLayout(16,16));
        salesEntryPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        salesEntryPanel.add(createSalesEntryForm(), BorderLayout.NORTH);
        salesEntryPanel.add(createSalesTablePanel(), BorderLayout.CENTER);

        // Invoice Tab
        JPanel invoicePanel = new JPanel(new BorderLayout(16,16));
        invoicePanel.setBorder(new EmptyBorder(16,16,16,16));
        invoiceArea = new JTextArea();
        invoiceArea.setEditable(false);
        invoiceArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JScrollPane invoiceScrollPane = new JScrollPane(invoiceArea);
        invoicePanel.add(invoiceScrollPane, BorderLayout.CENTER);
        invoicePanel.add(createInvoiceButtonsPanel(), BorderLayout.SOUTH);

        tabbedPane.addTab("Penjualan", salesEntryPanel);
        tabbedPane.addTab("Invoice / Laporan", invoicePanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createSalesEntryForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Penjualan"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        // Product Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nama Produk:"), gbc);
        txtProductName = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(txtProductName, gbc);

        // Quantity
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Jumlah:"), gbc);
        txtQuantity = new JTextField(8);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(txtQuantity, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Harga per unit (Rp):"), gbc);
        txtPrice = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(txtPrice, gbc);

        // Add Button
        btnAdd = new JButton("Tambah ke Penjualan");
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnAdd, gbc);

        btnAdd.addActionListener(e -> addSale());

        return formPanel;
    }

    private JPanel createSalesTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Penjualan"));

        // Table columns: Product, Quantity, Price, Total
        String[] columns = {"Nama Produk", "Jumlah", "Harga per unit (Rp)", "Total (Rp)"};
        salesTableModel = new DefaultTableModel(columns, 0) {
            // Make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        salesTable = new JTable(salesTableModel);
        salesTable.setFillsViewportHeight(true);
        salesTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(salesTable);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInvoiceButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 8));
        btnGenerateInvoice = new JButton("Generate Invoice");
        btnClearInvoice = new JButton("Clear Invoice");

        panel.add(btnClearInvoice);
        panel.add(btnGenerateInvoice);

        btnGenerateInvoice.addActionListener(e -> generateInvoice());
        btnClearInvoice.addActionListener(e -> invoiceArea.setText(""));

        return panel;
    }

    private void addSale() {
        String productName = txtProductName.getText().trim();
        String quantityStr = txtQuantity.getText().trim();
        String priceStr = txtPrice.getText().trim();

        if (productName.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Error Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity;
        double price;
        try {
            quantity = Integer.parseInt(quantityStr);
            price = Double.parseDouble(priceStr);
            if (quantity <= 0 || price <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah dan harga harus lebih dari nol.", "Error Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka bulat dan harga berupa angka valid.", "Error Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double total = quantity * price;

        salesTableModel.addRow(new Object[]{
                productName, quantity, String.format("%,.2f", price), String.format("%,.2f", total)
        });

        // Clear input fields
        txtProductName.setText("");
        txtQuantity.setText("");
        txtPrice.setText("");
        txtProductName.requestFocus();
    }

    private void generateInvoice() {
        if (salesTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Daftar penjualan kosong. Tambahkan penjualan terlebih dahulu.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder invoice = new StringBuilder();
        invoice.append("----- INVOICE PENJUALAN -----\n");
        invoice.append("Tanggal: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())).append("\n\n");

        invoice.append(String.format("%-20s %10s %15s %15s\n", "Produk", "Jumlah", "Harga/Unit (Rp)", "Total (Rp)"));
        invoice.append("-----------------------------------------------------------------\n");

        double grandTotal = 0;
        for (int i = 0; i < salesTableModel.getRowCount(); i++) {
            String product = salesTableModel.getValueAt(i, 0).toString();
            String qty = salesTableModel.getValueAt(i, 1).toString();
            String price = salesTableModel.getValueAt(i, 2).toString();
            String total = salesTableModel.getValueAt(i, 3).toString();

            invoice.append(String.format("%-20s %10s %15s %15s\n", product, qty, price, total));

            // Remove commas and parse to double to sum total
            grandTotal += Double.parseDouble(total.replaceAll(",", ""));
        }

        invoice.append("-----------------------------------------------------------------\n");
        invoice.append(String.format("%-20s %10s %15s %15s\n", "", "", "Grand Total:", String.format("%,.2f", grandTotal)));

        invoiceArea.setText(invoice.toString());
        tabbedPane.setSelectedIndex(1); // Switch to Invoice tab
    }

    public static void main(String[] args) {
        // Use system look and feel for modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        EventQueue.invokeLater(() -> {
            koko frame = new koko();
            frame.setVisible(true);
        });
    }
}

