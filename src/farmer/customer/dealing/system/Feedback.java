package farmer.customer.dealing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Feedback extends JFrame implements ActionListener {

    JComboBox<String> ratingCombo;
    JTextArea commentTextArea;
    JButton submit, back;

    Feedback() {
        // Frame
        getContentPane().setBackground(Color.WHITE);
        setTitle("Feedback");
        setLayout(null);
        setSize(900,600);
        setLocation(300,0);

        // Title
        JLabel title = new JLabel("Customer Feedback");
        title.setFont(new Font("Raleway", Font.BOLD, 28));
        title.setBounds(300,20,400,40);
        add(title);

        // Rating label
        JLabel ratingLabel = new JLabel("Rating:");
        ratingLabel.setFont(new Font("Raleway", Font.BOLD, 20));
        ratingLabel.setBounds(100,100,200,30);
        add(ratingLabel);

        // Rating combo box
        String[] ratings = {"Select","1 - Poor","2 - Fair","3 - Good","4 - Very Good","5 - Excellent"};
        ratingCombo = new JComboBox<>(ratings);
        ratingCombo.setBounds(250,100,200,30);
        add(ratingCombo);

        // Comment label
        JLabel commentLabel = new JLabel("Comment:");
        commentLabel.setFont(new Font("Raleway", Font.BOLD, 20));
        commentLabel.setBounds(100,160,200,30);
        add(commentLabel);

        // Comment text area
        commentTextArea = new JTextArea();
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(commentTextArea);
        scroll.setBounds(250,160,400,200);
        add(scroll);

        // Submit button
        submit = new JButton("Submit");
        submit.setBounds(600,400,150,40);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Raleway", Font.BOLD, 16));
        submit.addActionListener(this);
        add(submit);

        // Back button
        back = new JButton("Back");
        back.setBounds(100,400,120,40);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Raleway", Font.BOLD, 16));
        back.addActionListener(e -> {
            setVisible(false);
            new CustomerDashboard("C001").setVisible(true); // example: redirect to dashboard
        });
        add(back);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == submit) {
            String rating = (String) ratingCombo.getSelectedItem();
            String comment = commentTextArea.getText().trim();

            if(rating.equals("Select") || comment.equals("")) {
                JOptionPane.showMessageDialog(this, "Please provide both rating and comment");
                return;
            }

            // ✅ Save feedback logic (DB) can be added here
            JOptionPane.showMessageDialog(this, "Thank you for your feedback!\nRating: " + rating + "\nComment: " + comment);

            // Clear fields
            ratingCombo.setSelectedIndex(0);
            commentTextArea.setText("");
        }
    }

    public static void main(String[] args) {
        new Feedback();
    }
}
