import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentForm extends JFrame implements ActionListener {
    private JTextField txtId, txtName, txtAge;
    private JButton btnSubmit;

    public StudentForm() {
        setTitle("Student Registration Form");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel(" Student ID:"));
        txtId = new JTextField();
        add(txtId);

        add(new JLabel(" Name:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel(" Age:"));
        txtAge = new JTextField();
        add(txtAge);

        btnSubmit = new JButton("Submit to Database");
        btnSubmit.addActionListener(this);
        add(new JLabel("")); // Empty spot for layout alignment
        add(btnSubmit);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String idStr = txtId.getText().trim();
        String name = txtName.getText().trim();
        String ageStr = txtAge.getText().trim();

        if (idStr.isEmpty() || name.isEmpty() || ageStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database URL (includes timezone setting to prevent connection errors)
        String url = "jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC";
        String user = "root";       // Change if your MySQL username is different
        String password = "root"; // Change to your actual MySQL root password

        String query = "INSERT INTO student (id, name, age) VALUES (?, ?, ?)";

        try {
            // Modern Connector Driver
            Class.forName("com.mysql.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, Integer.parseInt(idStr));
                stmt.setString(2, name);
                stmt.setInt(3, Integer.parseInt(ageStr));

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Record submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    txtId.setText("");
                    txtName.setText("");
                    txtAge.setText("");
                }
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Driver Error: Add mysql-connector-j-8.x.jar to project Libraries!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Age must be valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new StudentForm();
    }
}