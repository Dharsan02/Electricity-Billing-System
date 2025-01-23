package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Signup extends JFrame implements ActionListener {
    Choice loginASCho;
    TextField meterText, EmployerText, userNameText, nameText;
    JPasswordField passwordText;
    JButton create, back;

    Signup() {
        super("Signup Page");
        getContentPane().setBackground(new Color(168, 203, 255));

        JLabel createAs = new JLabel("Create Account As");
        createAs.setBounds(30, 50, 125, 20);
        add(createAs);

        loginASCho = new Choice();
        loginASCho.add("Admin");
        loginASCho.add("Customer");
        loginASCho.setBounds(170, 50, 120, 20);
        add(loginASCho);

        JLabel meterNo = new JLabel("Meter Number");
        meterNo.setBounds(30, 100, 125, 20);
        meterNo.setVisible(false);
        add(meterNo);

        meterText = new TextField();
        meterText.setBounds(170, 100, 125, 20);
        meterText.setVisible(false);
        add(meterText);

        JLabel Employer = new JLabel("Employer ID");
        Employer.setBounds(30, 100, 125, 20);
        Employer.setVisible(true);
        add(Employer);

        EmployerText = new TextField();
        EmployerText.setBounds(170, 100, 125, 20);
        EmployerText.setVisible(true);
        add(EmployerText);

        JLabel userName = new JLabel("UserName");
        userName.setBounds(30, 140, 125, 20);
        add(userName);

        userNameText = new TextField();
        userNameText.setBounds(170, 140, 125, 20);
        add(userNameText);

        JLabel name = new JLabel("Name");
        name.setBounds(30, 180, 125, 20);
        add(name);

        nameText = new TextField();
        nameText.setBounds(170, 180, 125, 20);
        add(nameText);

        meterText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    database c = new database();
                    String query = "SELECT * FROM Signup WHERE meter_no = ?";
                    PreparedStatement pstmt = c.connection.prepareStatement(query);
                    pstmt.setString(1, meterText.getText());
                    ResultSet resultSet = pstmt.executeQuery();

                    if (resultSet.next()) {
                        nameText.setText(resultSet.getString("name"));
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Meter Number");
                        nameText.setText("");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JLabel password = new JLabel("Password");
        password.setBounds(30, 220, 125, 20);
        add(password);

        passwordText = new JPasswordField();
        passwordText.setBounds(170, 220, 125, 20);
        add(passwordText);

        loginASCho.addItemListener(e -> {
            String user = loginASCho.getSelectedItem();
            if (user.equals("Customer")) {
                Employer.setVisible(false);
                nameText.setEditable(false);
                EmployerText.setVisible(false);
                meterNo.setVisible(true);
                meterText.setVisible(true);
            } else {
                Employer.setVisible(true);
                EmployerText.setVisible(true);
                meterNo.setVisible(false);
                meterText.setVisible(false);
            }
        });

        create = new JButton("Create");
        create.setBackground(new Color(66, 127, 219));
        create.setForeground(Color.black);
        create.setBounds(50, 285, 100, 25);
        create.addActionListener(this);
        add(create);

        back = new JButton("Back");
        back.setBackground(new Color(66, 127, 219));
        back.setForeground(Color.black);
        back.setBounds(180, 285, 100, 25);
        back.addActionListener(this);
        add(back);

        ImageIcon boyIcon = new ImageIcon(ClassLoader.getSystemResource("icon/boy.png"));
        Image boyImg = boyIcon.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
        ImageIcon boyIcon2 = new ImageIcon(boyImg);
        JLabel boyLabel = new JLabel(boyIcon2);
        boyLabel.setBounds(320, 30, 250, 250);
        add(boyLabel);

        setSize(600, 380);
        setLocation(500, 200);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == create) {
            String sloginAs = loginASCho.getSelectedItem();
            String susername = userNameText.getText();
            String sname = nameText.getText();
            String spassword = new String(passwordText.getPassword());
            String smeter = meterText.getText();

            if (susername.isEmpty() || spassword.isEmpty() || (sloginAs.equals("Customer") && smeter.isEmpty())) {
                JOptionPane.showMessageDialog(null, "Please fill all required fields");
                return;
            }

            try {
                database c = new database();
                String query;

                if (sloginAs.equals("Admin")) {
                    query = "INSERT INTO Signup (meter_no, username, name, password, usertype) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = c.connection.prepareStatement(query);
                    pstmt.setString(1, null); // Admin does not have a meter number
                    pstmt.setString(2, susername);
                    pstmt.setString(3, sname);
                    pstmt.setString(4, spassword);
                    pstmt.setString(5, sloginAs);
                    pstmt.executeUpdate();
                } else {
                    query = "UPDATE Signup SET username = ?, password = ?, usertype = ? WHERE meter_no = ?";
                    PreparedStatement pstmt = c.connection.prepareStatement(query);
                    pstmt.setString(1, susername);
                    pstmt.setString(2, spassword);
                    pstmt.setString(3, sloginAs);
                    pstmt.setString(4, smeter);
                    pstmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Account Created");
                setVisible(false);
                new Login();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == back) {
            setVisible(false);
            new Login();
        }
    }

    public static void main(String[] args) {
        new Signup();
    }
}
