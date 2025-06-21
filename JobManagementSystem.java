import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class JobManagementSystem extends JFrame {

    // UI组件
    private JTabbedPane tabbedPane;
    private JTable jobTable, seekerTable, matchingTable, feeTable;
    private JComboBox<String> categoryComboBox, jobComboBox, seekerComboBox;
    private JTextField jobIdField, employerField, requiredField, notesField;
    private JTextField seekerIdField, seekerNameField, seekerFeeField;
    private JTextField employerNameField, employerFeeField;
    private JRadioButton maleRadio, femaleRadio;
    private JComboBox<String> employedComboBox;

    public JobManagementSystem() {
        super("职业介绍信息管理系统");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建UI
        createUI();

        // 加载初始数据
        loadInitialData();
    }

    private void createUI() {
        tabbedPane = new JTabbedPane();

        // 添加选项卡
        tabbedPane.addTab("职业管理", createJobManagementPanel());
        tabbedPane.addTab("求职者管理", createJobSeekerPanel());
        tabbedPane.addTab("职业匹配", createMatchingPanel());
        tabbedPane.addTab("费用管理", createFeePanel());
        tabbedPane.addTab("统计报表", createReportPanel());

        add(tabbedPane);
    }

    private JPanel createJobManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 表单面板
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("添加/编辑职业信息"));

        formPanel.add(new JLabel("职业号:"));
        jobIdField = new JTextField();
        jobIdField.setEditable(false);
        formPanel.add(jobIdField);

        formPanel.add(new JLabel("职业类型:"));
        categoryComboBox = new JComboBox<>();
        loadCategories();
        formPanel.add(categoryComboBox);

        formPanel.add(new JLabel("用人单位:"));
        employerField = new JTextField();
        formPanel.add(employerField);

        formPanel.add(new JLabel("需求人数:"));
        requiredField = new JTextField();
        formPanel.add(requiredField);

        formPanel.add(new JLabel("备注:"));
        notesField = new JTextField();
        formPanel.add(notesField);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("添加");
        JButton updateButton = new JButton("更新");
        JButton deleteButton = new JButton("删除");
        JButton clearButton = new JButton("清空");
        JButton refreshButton = new JButton("刷新");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        // 表格
        jobTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(jobTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("职业列表"));

        // 添加组件到主面板
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        // 事件处理
        addButton.addActionListener(e -> addJob());
        updateButton.addActionListener(e -> updateJob());
        deleteButton.addActionListener(e -> deleteJob());
        clearButton.addActionListener(e -> clearJobFields());
        refreshButton.addActionListener(e -> loadJobData());

        jobTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = jobTable.getSelectedRow();
                if (row >= 0) {
                    jobIdField.setText(jobTable.getValueAt(row, 0).toString());
                    categoryComboBox.setSelectedItem(jobTable.getValueAt(row, 1).toString());
                    employerField.setText(jobTable.getValueAt(row, 2).toString());
                    requiredField.setText(jobTable.getValueAt(row, 3).toString());
                    notesField.setText(jobTable.getValueAt(row, 5).toString());
                }
            }
        });

        return panel;
    }

    private JPanel createJobSeekerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 表单面板
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("添加/编辑求职者"));

        formPanel.add(new JLabel("求职者ID:"));
        seekerIdField = new JTextField();
        seekerIdField.setEditable(false);
        formPanel.add(seekerIdField);

        formPanel.add(new JLabel("姓名:"));
        seekerNameField = new JTextField();
        formPanel.add(seekerNameField);

        formPanel.add(new JLabel("性别:"));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup genderGroup = new ButtonGroup();
        maleRadio = new JRadioButton("男");
        femaleRadio = new JRadioButton("女");
        maleRadio.setSelected(true);
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        formPanel.add(genderPanel);

        formPanel.add(new JLabel("聘用状态:"));
        employedComboBox = new JComboBox<>(new String[]{"未聘用", "聘用成功"});
        formPanel.add(employedComboBox);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addSeekerButton = new JButton("添加");
        JButton updateSeekerButton = new JButton("更新");
        JButton deleteSeekerButton = new JButton("删除");
        JButton clearSeekerButton = new JButton("清空");
        JButton refreshSeekerButton = new JButton("刷新");

        buttonPanel.add(addSeekerButton);
        buttonPanel.add(updateSeekerButton);
        buttonPanel.add(deleteSeekerButton);
        buttonPanel.add(clearSeekerButton);
        buttonPanel.add(refreshSeekerButton);

        // 表格
        seekerTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(seekerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("求职者列表"));

        // 添加组件到主面板
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        // 事件处理
        addSeekerButton.addActionListener(e -> addJobSeeker());
        updateSeekerButton.addActionListener(e -> updateJobSeeker());
        deleteSeekerButton.addActionListener(e -> deleteJobSeeker());
        clearSeekerButton.addActionListener(e -> clearSeekerFields());
        refreshSeekerButton.addActionListener(e -> loadSeekerData());

        seekerTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = seekerTable.getSelectedRow();
                if (row >= 0) {
                    seekerIdField.setText(seekerTable.getValueAt(row, 0).toString());
                    seekerNameField.setText(seekerTable.getValueAt(row, 1).toString());
                    String gender = seekerTable.getValueAt(row, 2).toString();
                    if (gender.equals("男")) {
                        maleRadio.setSelected(true);
                    } else {
                        femaleRadio.setSelected(true);
                    }

                    String employed = seekerTable.getValueAt(row, 3).toString();
                    employedComboBox.setSelectedItem(employed.equals("true") ? "聘用成功" : "未聘用");
                }
            }
        });

        return panel;
    }

    private JPanel createMatchingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 表单面板
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("职业匹配"));

        formPanel.add(new JLabel("选择职业:"));
        jobComboBox = new JComboBox<>();
        loadJobs();
        formPanel.add(jobComboBox);

        formPanel.add(new JLabel("选择求职者:"));
        seekerComboBox = new JComboBox<>();
        loadJobSeekers();
        formPanel.add(seekerComboBox);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton matchButton = new JButton("匹配");
        JButton deleteMatchButton = new JButton("删除匹配");
        JButton refreshButton = new JButton("刷新数据");

        buttonPanel.add(matchButton);
        buttonPanel.add(deleteMatchButton);
        buttonPanel.add(refreshButton);

        // 表格
        matchingTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(matchingTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("匹配记录"));

        // 添加组件到主面板
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        // 事件处理
        matchButton.addActionListener(e -> matchJobSeeker());
        deleteMatchButton.addActionListener(e -> deleteMatching());
        refreshButton.addActionListener(e -> {
            loadMatchingData();
            loadJobSeekers();
        });

        matchingTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = matchingTable.getSelectedRow();
                if (row >= 0) {
                    String job = matchingTable.getValueAt(row, 1).toString();
                    String seeker = matchingTable.getValueAt(row, 3).toString();

                    jobComboBox.setSelectedItem(job);
                    seekerComboBox.setSelectedItem(seeker);
                }
            }
        });

        return panel;
    }

    private JPanel createFeePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 表单面板
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("费用管理"));

        formPanel.add(new JLabel("用人单位名称:"));
        employerNameField = new JTextField();
        formPanel.add(employerNameField);

        formPanel.add(new JLabel("用人单位费用:"));
        employerFeeField = new JTextField();
        formPanel.add(employerFeeField);

        formPanel.add(new JLabel("求职者名称:"));
        seekerNameField = new JTextField();
        formPanel.add(seekerNameField);

        formPanel.add(new JLabel("求职者费用:"));
        seekerFeeField = new JTextField();
        formPanel.add(seekerFeeField);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addFeeButton = new JButton("添加费用记录");
        JButton clearFeeButton = new JButton("清空");
        JButton refreshFeeButton = new JButton("刷新");

        buttonPanel.add(addFeeButton);
        buttonPanel.add(clearFeeButton);
        buttonPanel.add(refreshFeeButton);

        // 表格
        feeTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(feeTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("费用记录"));

        // 添加组件到主面板
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        // 事件处理
        addFeeButton.addActionListener(e -> addFeeRecord());
        clearFeeButton.addActionListener(e -> clearFeeFields());
        refreshFeeButton.addActionListener(e -> loadFeeData());

        return panel;
    }

    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 报表面板
        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBorder(BorderFactory.createTitledBorder("职业需求统计"));

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(reportArea);

        // 按钮
        JButton generateReportButton = new JButton("生成统计报表");
        generateReportButton.addActionListener(e -> {
            String report = generateJobReport();
            reportArea.setText(report);
        });

        reportPanel.add(scrollPane, BorderLayout.CENTER);
        reportPanel.add(generateReportButton, BorderLayout.SOUTH);

        panel.add(reportPanel, BorderLayout.CENTER);

        return panel;
    }

    // 数据库操作方法
    private void loadInitialData() {
        loadJobData();
        loadSeekerData();
        loadMatchingData();
        loadFeeData();
    }

    private void loadCategories() {
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM JobCategory")) {

            categoryComboBox.removeAllItems();
            while (rs.next()) {
                categoryComboBox.addItem(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载职业分类失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadJobs() {
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT job_id, employer FROM Job")) {

            jobComboBox.removeAllItems();
            while (rs.next()) {
                jobComboBox.addItem(rs.getString("employer"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载职业列表失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadJobSeekers() {
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT seeker_id, name FROM JobSeeker WHERE employed = false")) {

            seekerComboBox.removeAllItems();
            while (rs.next()) {
                seekerComboBox.addItem(rs.getString("name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载求职者列表失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadJobData() {
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT j.job_id, c.category_name, j.employer, " +
                     "j.required_count, j.hired_count, j.notes " +
                     "FROM Job j JOIN JobCategory c ON j.category_id = c.category_id")) {

            jobTable.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载职业数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSeekerData() {
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT seeker_id, name, gender, employed FROM JobSeeker")) {

            seekerTable.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载求职者数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMatchingData() {
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT m.match_id, j.employer AS job, s.name AS seeker " +
                     "FROM JobMatching m " +
                     "JOIN Job j ON m.job_id = j.job_id " +
                     "JOIN JobSeeker s ON m.seeker_id = s.seeker_id")) {

            matchingTable.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载匹配数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFeeData() {
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Fee")) {

            feeTable.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载费用数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addJob() {
        try (Connection conn = DBConnector.getConnection()) {
            String category = (String) categoryComboBox.getSelectedItem();
            String employer = employerField.getText();
            int required = Integer.parseInt(requiredField.getText());
            String notes = notesField.getText();

            // 获取category_id
            int categoryId = 0;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT category_id FROM JobCategory WHERE category_name = ?")) {
                pstmt.setString(1, category);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    categoryId = rs.getInt("category_id");
                }
            }

            if (categoryId == 0) {
                JOptionPane.showMessageDialog(this, "无效的职业分类", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO Job (category_id, employer, required_count, notes) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, categoryId);
                pstmt.setString(2, employer);
                pstmt.setInt(3, required);
                pstmt.setString(4, notes);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "职业添加成功！");
                loadJobData();
                clearJobFields();
                loadJobs();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "需求人数必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "添加职业失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateJob() {
        try (Connection conn = DBConnector.getConnection()) {
            int jobId = Integer.parseInt(jobIdField.getText());
            String category = (String) categoryComboBox.getSelectedItem();
            String employer = employerField.getText();
            int required = Integer.parseInt(requiredField.getText());
            String notes = notesField.getText();

            // 获取category_id
            int categoryId = 0;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT category_id FROM JobCategory WHERE category_name = ?")) {
                pstmt.setString(1, category);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    categoryId = rs.getInt("category_id");
                }
            }

            if (categoryId == 0) {
                JOptionPane.showMessageDialog(this, "无效的职业分类", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "UPDATE Job SET category_id = ?, employer = ?, required_count = ?, notes = ? WHERE job_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, categoryId);
                pstmt.setString(2, employer);
                pstmt.setInt(3, required);
                pstmt.setString(4, notes);
                pstmt.setInt(5, jobId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "职业更新成功！");
                loadJobData();
                clearJobFields();
                loadJobs();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "需求人数必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "更新职业失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteJob() {
        if (jobIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择要删除的职业", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除这个职业吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnector.getConnection()) {
                int jobId = Integer.parseInt(jobIdField.getText());
                String sql = "DELETE FROM Job WHERE job_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, jobId);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "职业删除成功！");
                    loadJobData();
                    clearJobFields();
                    loadJobs();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "无效的职业ID", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "删除职业失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearJobFields() {
        jobIdField.setText("");
        categoryComboBox.setSelectedIndex(0);
        employerField.setText("");
        requiredField.setText("");
        notesField.setText("");
        jobTable.clearSelection();
    }

    private void addJobSeeker() {
        try (Connection conn = DBConnector.getConnection()) {
            String name = seekerNameField.getText();
            String gender = maleRadio.isSelected() ? "男" : "女";
            boolean employed = employedComboBox.getSelectedItem().equals("聘用成功");

            String sql = "INSERT INTO JobSeeker (name, gender, employed) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, gender);
                pstmt.setBoolean(3, employed);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "求职者添加成功！");
                loadSeekerData();
                clearSeekerFields();
                loadJobSeekers();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "添加求职者失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateJobSeeker() {
        try (Connection conn = DBConnector.getConnection()) {
            int seekerId = Integer.parseInt(seekerIdField.getText());
            String name = seekerNameField.getText();
            String gender = maleRadio.isSelected() ? "男" : "女";
            boolean employed = employedComboBox.getSelectedItem().equals("聘用成功");

            String sql = "UPDATE JobSeeker SET name = ?, gender = ?, employed = ? WHERE seeker_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, gender);
                pstmt.setBoolean(3, employed);
                pstmt.setInt(4, seekerId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "求职者更新成功！");
                loadSeekerData();
                clearSeekerFields();
                loadJobSeekers();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "无效的求职者ID", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "更新求职者失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteJobSeeker() {
        if (seekerIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择要删除的求职者", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除这个求职者吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnector.getConnection()) {
                int seekerId = Integer.parseInt(seekerIdField.getText());
                String sql = "DELETE FROM JobSeeker WHERE seeker_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, seekerId);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "求职者删除成功！");
                    loadSeekerData();
                    clearSeekerFields();
                    loadJobSeekers();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "无效的求职者ID", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "删除求职者失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearSeekerFields() {
        seekerIdField.setText("");
        seekerNameField.setText("");
        maleRadio.setSelected(true);
        employedComboBox.setSelectedIndex(0);
        seekerTable.clearSelection();
    }

    private void matchJobSeeker() {
        try (Connection conn = DBConnector.getConnection()) {
            String job = (String) jobComboBox.getSelectedItem();
            String seeker = (String) seekerComboBox.getSelectedItem();

            if (job == null || seeker == null) {
                JOptionPane.showMessageDialog(this, "请选择职业和求职者", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 获取job_id
            int jobId = 0;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT job_id FROM Job WHERE employer = ?")) {
                pstmt.setString(1, job);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    jobId = rs.getInt("job_id");
                }
            }

            // 获取seeker_id
            int seekerId = 0;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT seeker_id FROM JobSeeker WHERE name = ?")) {
                pstmt.setString(1, seeker);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    seekerId = rs.getInt("seeker_id");
                }
            }

            if (jobId == 0 || seekerId == 0) {
                JOptionPane.showMessageDialog(this, "无效的职业或求职者", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 检查是否已满
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT required_count, hired_count FROM Job WHERE job_id = ?")) {
                pstmt.setInt(1, jobId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int required = rs.getInt("required_count");
                    int hired = rs.getInt("hired_count");
                    if (hired >= required) {
                        JOptionPane.showMessageDialog(this, "该职业需求已满，无法匹配", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            // 检查求职者是否已被聘用
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT employed FROM JobSeeker WHERE seeker_id = ?")) {
                pstmt.setInt(1, seekerId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next() && rs.getBoolean("employed")) {
                    JOptionPane.showMessageDialog(this, "该求职者已被聘用，无法再次匹配", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            String sql = "INSERT INTO JobMatching (job_id, seeker_id) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, jobId);
                pstmt.setInt(2, seekerId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "匹配成功！");

                // 更新已聘人数
                updateHiredCount(conn, jobId, 1);

                // 更新求职者状态
                updateSeekerEmployedStatus(conn, seekerId, true);

                loadMatchingData();
                loadJobData();
                loadSeekerData();
                loadJobSeekers(); // 刷新可用的求职者列表
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "匹配失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateHiredCount(Connection conn, int jobId, int increment) throws SQLException {
        String sql = "UPDATE Job SET hired_count = hired_count + ? WHERE job_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, increment);
            pstmt.setInt(2, jobId);
            pstmt.executeUpdate();
        }
    }

    private void updateSeekerEmployedStatus(Connection conn, int seekerId, boolean employed) throws SQLException {
        String sql = "UPDATE JobSeeker SET employed = ? WHERE seeker_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, employed);
            pstmt.setInt(2, seekerId);
            pstmt.executeUpdate();
        }
    }

    private void deleteMatching() {
        int row = matchingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的匹配记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除这个匹配记录吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnector.getConnection()) {
                int matchId = (int) matchingTable.getValueAt(row, 0);

                // 获取job_id和seeker_id
                int jobId = 0, seekerId = 0;
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT job_id, seeker_id FROM JobMatching WHERE match_id = ?")) {
                    pstmt.setInt(1, matchId);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        jobId = rs.getInt("job_id");
                        seekerId = rs.getInt("seeker_id");
                    }
                }

                String sql = "DELETE FROM JobMatching WHERE match_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, matchId);
                    pstmt.executeUpdate();

                    // 更新已聘人数
                    if (jobId > 0) {
                        updateHiredCount(conn, jobId, -1);
                    }

                    // 更新求职者状态
                    if (seekerId > 0) {
                        updateSeekerEmployedStatus(conn, seekerId, false);
                    }

                    JOptionPane.showMessageDialog(this, "匹配记录删除成功！");
                    loadMatchingData();
                    loadJobData();
                    loadSeekerData();
                    loadJobSeekers(); // 刷新可用的求职者列表
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "删除匹配记录失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addFeeRecord() {
        try (Connection conn = DBConnector.getConnection()) {
            String employerName = employerNameField.getText();
            double employerFee = Double.parseDouble(employerFeeField.getText());
            String seekerName = seekerNameField.getText();
            double seekerFee = Double.parseDouble(seekerFeeField.getText());

            String sql = "INSERT INTO Fee (employer_name, employer_fee, seeker_name, seeker_fee) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, employerName);
                pstmt.setDouble(2, employerFee);
                pstmt.setString(3, seekerName);
                pstmt.setDouble(4, seekerFee);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "费用记录添加成功！");
                loadFeeData();
                clearFeeFields();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "费用必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "添加费用记录失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFeeFields() {
        employerNameField.setText("");
        employerFeeField.setText("");
        seekerNameField.setText("");
        seekerFeeField.setText("");
    }

    private String generateJobReport() {
        StringBuilder report = new StringBuilder();
        report.append("========= 职业需求统计报表 =========\n");
        report.append(String.format("%-20s %-15s %-15s %-15s\n", "职业名称", "需求人数", "已聘人数", "空缺人数"));

        try (Connection conn = DBConnector.getConnection();
             CallableStatement cstmt = conn.prepareCall("{call GetJobStats()}")) {
            ResultSet rs = cstmt.executeQuery();

            while (rs.next()) {
                String jobName = rs.getString("employer");
                int required = rs.getInt("required_count");
                int hired = rs.getInt("hired_count");
                int vacancy = required - hired;

                report.append(String.format("%-20s %-15d %-15d %-15d\n",
                        jobName, required, hired, vacancy));
            }

            report.append("=====================================");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "生成报表失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return "报表生成失败: " + e.getMessage();
        }

        return report.toString();
    }

    // 辅助方法：从ResultSet构建TableModel
    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // 获取列名
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // 获取数据
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置系统UI风格
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new JobManagementSystem().setVisible(true);
        });
    }
}

// DBConnector.java


