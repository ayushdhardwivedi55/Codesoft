import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystem {
    private static StudentManagement sms;
    private static JFrame frame;
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    private static JTextField nameField;
    private static JTextField rollField;
    private static JTextField gradeField;
    private static JTextField parentNameField;
    private static JTextField mobileField;
    private static JTextField emailField;
    private static JTextField[] subjectFields;
    private static JLabel imageLabel;
    private static String imagePath = "";
    private static Color mainBackgroundColor = Color.LIGHT_GRAY;
    private static Color inputPanelBackgroundColor = Color.WHITE;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            sms = new StudentManagement();
            createAndShowLoginGUI();
        });
    }

    private static void createAndShowLoginGUI() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);
        loginFrame.setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        // Login action listener
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.equals("Ayush") && password.equals("Ayush123")) {
                loginFrame.dispose();
                createAndShowMainGUI();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel()); // Empty cell
        loginPanel.add(loginButton);

        loginFrame.add(loginPanel, BorderLayout.CENTER);
        loginFrame.setVisible(true);
    }

    private static void createAndShowMainGUI() {
        frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(mainBackgroundColor);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(inputPanelBackgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Name:");
        inputPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        JLabel rollLabel = new JLabel("Registration Number/Roll Number: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(rollLabel, gbc);

        rollField = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(rollField, gbc);

        JLabel parentNameLabel = new JLabel("Father's Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(parentNameLabel, gbc);

        parentNameField = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(parentNameField, gbc);

        JLabel mobileLabel = new JLabel("Mobile Number:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(mobileLabel, gbc);

        mobileField = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(mobileField, gbc);

        JLabel emailLabel = new JLabel("Email ID:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(emailField, gbc);

        JLabel gradeLabel = new JLabel("Grade:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(gradeLabel, gbc);

        gradeField = new JTextField(20);
        gbc.gridx = 1;
        inputPanel.add(gradeField, gbc);

        subjectFields = new JTextField[5];
        for (int i = 0; i < 5; i++) {
            JLabel subjectLabel = new JLabel("Subject " + (i + 1) + " Marks:");
            gbc.gridx = 0;
            gbc.gridy = 6 + i;
            inputPanel.add(subjectLabel, gbc);

            subjectFields[i] = new JTextField(20);
            gbc.gridx = 1;
            inputPanel.add(subjectFields[i], gbc);
        }

        JButton browseButton = new JButton("Browse Image");
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                imagePath = fileChooser.getSelectedFile().getAbsolutePath();
                imageLabel.setIcon(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 11;
        inputPanel.add(browseButton, gbc);

        imageLabel = new JLabel();
        gbc.gridx = 1;
        inputPanel.add(imageLabel, gbc);

        JButton addButton = new JButton("Add Student");
        addButton.setBackground(Color.GREEN);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String rollNumber = rollField.getText();
            String parentName = parentNameField.getText();
            String mobile = mobileField.getText();
            String email = emailField.getText();
            int[] subjectMarks = new int[5];
            try {
                for (int i = 0; i < 5; i++) {
                    subjectMarks[i] = Integer.parseInt(subjectFields[i].getText());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid marks entered.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String grade = calculateGrade(subjectMarks);
            if (!name.isEmpty() && !rollNumber.isEmpty() && !parentName.isEmpty() && !mobile.isEmpty() && !email.isEmpty() && !imagePath.isEmpty()) {
                sms.addStudent(new Student(name, rollNumber, parentName, mobile, email, subjectMarks, grade, imagePath));
                clearInputFields();
            } else {
                JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 13;
        inputPanel.add(addButton, gbc);

        JButton displayButton = new JButton("Display All Students");
        displayButton.setBackground(Color.BLUE);
        displayButton.setForeground(Color.WHITE);
        displayButton.addActionListener(e -> displayStudents());
        gbc.gridx = 1;
        inputPanel.add(displayButton, gbc);

        JButton viewImageButton = new JButton("View Student Image");
        viewImageButton.setBackground(Color.ORANGE);
        viewImageButton.setForeground(Color.WHITE);
        viewImageButton.addActionListener(e -> {
            String rollNumber = JOptionPane.showInputDialog(frame, "Enter Roll Number:");
            if (rollNumber != null && !rollNumber.trim().isEmpty()) {
                Student student = sms.getStudentByRollNumber(rollNumber.trim());
                if (student != null) {
                    showStudentImage(student);
                } else {
                    JOptionPane.showMessageDialog(frame, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 12;
        inputPanel.add(viewImageButton, gbc);

        // Add delete student button
        JButton deleteButton = new JButton("Delete Student");
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> {
            String rollNumber = JOptionPane.showInputDialog(frame, "Enter Roll Number to Delete:");
            if (rollNumber != null && !rollNumber.trim().isEmpty()) {
                boolean success = sms.deleteStudentByRollNumber(rollNumber.trim());
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Student deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 14;
        inputPanel.add(deleteButton, gbc);

        // Add color picker buttons
        JButton mainColorButton = new JButton("Set Main Background Color");
        mainColorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(frame, "Choose Background Color", mainBackgroundColor);
            if (color!= null) {
                mainBackgroundColor = color;
                frame.getContentPane().setBackground(mainBackgroundColor);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 15;
        inputPanel.add(mainColorButton, gbc);

        JButton panelColorButton = new JButton("Set Input Panel Background Color");
        panelColorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(frame, "Choose Panel Color", inputPanelBackgroundColor);
            if (color!= null) {
                inputPanelBackgroundColor = color;
                inputPanel.setBackground(inputPanelBackgroundColor);
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 15;
        inputPanel.add(panelColorButton, gbc);

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void clearInputFields() {
        nameField.setText("");
        rollField.setText("");
        parentNameField.setText("");
        mobileField.setText("");
        emailField.setText("");
        gradeField.setText("");
        for (JTextField subjectField : subjectFields) {
            subjectField.setText("");
        }
        imageLabel.setIcon(null);
        imagePath = "";
    }

    private static String calculateGrade(int[] marks) {
        int total = 0;
        for (int mark : marks) {
            total += mark;
        }
        double average = total / (double) marks.length;
        if (average >= 90) return "A";
        else if (average >= 80) return "B";
        else if (average >= 70) return "C";
        else if (average >= 60) return "D";
        else return "F";
    }

    private static void displayStudents() {
        List<Student> students = sms.getAllStudents();

        JFrame displayFrame = new JFrame("All Students");
        displayFrame.setSize(800, 600);
        displayFrame.setLayout(new BorderLayout());

        String[] columns = {"Name", "Roll Number", "Father's Name", "Mobile Number", "Email ID", "Marks (Subjects 1-5)", "Grade", "Image"};
        Object[][] data = new Object[students.size()][columns.length];
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            data[i][0] = student.getName();
            data[i][1] = student.getRollNumber();
            data[i][2] = student.getParentName();
            data[i][3] = student.getMobile();
            data[i][4] = student.getEmail();
            data[i][5] = student.getMarksAsString();
            data[i][6] = student.getGrade();
            data[i][7] = new ImageIcon(new ImageIcon(student.getImagePath()).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 7) {
                    return ImageIcon.class;
                }
                return String.class;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(50);

        JScrollPane scrollPane = new JScrollPane(table);
        displayFrame.add(scrollPane, BorderLayout.CENTER);
        displayFrame.setVisible(true);
    }

    private static void showStudentImage(Student student) {
        JFrame imageFrame = new JFrame("Student Image");
        imageFrame.setSize(300, 300);

        JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon(student.getImagePath()).getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
        imageFrame.add(imageLabel);

        imageFrame.setVisible(true);
    }

    // Assuming the Student class looks something like this:
    public static class Student {
        private String name;
        private String rollNumber;
        private String parentName;
        private String mobile;
        private String email;
        private int[] subjectMarks;
        private String grade;
        private String imagePath;

        public Student(String name, String rollNumber, String parentName, String mobile, String email, int[] subjectMarks, String grade, String imagePath) {
            this.name = name;
            this.rollNumber = rollNumber;
            this.parentName = parentName;
            this.mobile = mobile;
            this.email = email;
            this.subjectMarks = subjectMarks;
            this.grade = grade;
            this.imagePath = imagePath;
        }

        public String getName() { return name; }
        public String getRollNumber() { return rollNumber; }
        public String getParentName() { return parentName; }
        public String getMobile() { return mobile; }
        public String getEmail() { return email; }
        public int[] getSubjectMarks() { return subjectMarks; }
        public String getGrade() { return grade; }
        public String getImagePath() { return imagePath; }

        public String getMarksAsString() {
            StringBuilder sb = new StringBuilder();
            for (int mark : subjectMarks) {
                sb.append(mark).append(", ");
            }
            return sb.substring(0, sb.length() - 2);
        }
    }

    public static class StudentManagement {
        private List<Student> students;

        public StudentManagement() {
            students = new ArrayList<>();
        }

        public void addStudent(Student student) {
            students.add(student);
        }

        public List<Student> getAllStudents() {
            return new ArrayList<>(students);
        }

        public Student getStudentByRollNumber(String rollNumber) {
            for (Student student : students) {
                if (student.getRollNumber().equals(rollNumber)) {
                    return student;
                }
            }
            return null;
        }

        public boolean deleteStudentByRollNumber(String rollNumber) {
            for (Student student : students) {
                if (student.getRollNumber().equals(rollNumber)) {
                    return students.remove(student);
                }
            }
            return false;
        }
    }
}
