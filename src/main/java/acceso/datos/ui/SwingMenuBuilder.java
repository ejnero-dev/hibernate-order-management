package acceso.datos.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SwingMenuBuilder extends JFrame {
    private JPanel buttonPanel;
    private JLabel statusLabel;

    public static final String STATUS_READY = "Listo";
    public static final String STATUS_PROCESSING = "Procesando...";
    public static final String STATUS_CLOSING = "Cerrando...";
    public static final String STATUS_ERROR = "Error";

    public SwingMenuBuilder(String title) {
        super(title);
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel de título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Sistema de Gestión de Pedidos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel de botones
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de estado
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel(STATUS_READY);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(statusLabel);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public void setStatus(String status) {
        if (SwingUtilities.isEventDispatchThread()) {
            statusLabel.setText(status);
        } else {
            SwingUtilities.invokeLater(() -> statusLabel.setText(status));
        }
    }

    public void addMenuOption(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setMargin(new Insets(10, 20, 10, 20));

        button.addActionListener(e -> {
            if (action != null) {
                action.run();
            }
        });

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(button);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    public static void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static String showInputDialog(String message) {
        return JOptionPane.showInputDialog(null, message);
    }

    public static int showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Confirmar", 
                JOptionPane.YES_NO_OPTION);
    }

    public static void showLongTextDialog(String title, String message) {
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void displayGUI() {
        setVisible(true);
    }
}