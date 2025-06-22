import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class JobManagementSystem extends JFrame {

    // UI组件
    private JTabbedPane tabbedPane;
    private JTable jobTable, seekerTable, matchingTable, feeTable, categoryTable;
    private JComboBox<String> categoryComboBox, jobComboBox, seekerComboBox;
    private JTextField jobIdField, employerField, requiredField, notesField;
    private JTextField seekerIdField, seekerNameField, seekerFeeField;
    private JTextField employerNameField, employerFeeField, feeIdField;
    private JTextField categoryIdField, categoryNameField;
    private JRadioButton maleRadio, femaleRadio;
    private JComboBox<String> employedComboBox;

    public JobManagementSystem() {
        super("职业介绍信息管理系统");
        setSize(1200, 800);
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
        tabbedPane.addTab("职业分类管理", createCategoryManagementPanel());
        tabbedPane.addTab("职业管理", createJobManagementPanel());
        tabbedPane.addTab("求职者管理", createJobSeekerPanel());
        tabbedPane.addTab("职业匹配", createMatchingPanel());
        tabbedPane.addTab("费用管理", createFeePanel());
        tabbedPane.addTab("统计报表", createReportPanel());

        add(tabbedPane);
    }

    private JPanel createCategoryManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 表单面板
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("职业分类管理"));
        formPanel.setBackground(new Color(240, 248, 255));

        formPanel.add(new JLabel("分类ID:"));
        categoryIdField = new JTextField();
        categoryIdField.setEditable(false);
        categoryIdField.setForeground(Color.BLACK);
        formPanel.add(categoryIdField);

        formPanel.add(new JLabel("分类名称:"));
        categoryNameField = new JTextField();
        categoryNameField.setForeground(Color.BLACK);
        formPanel.add(categoryNameField);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton addButton = createStyledButton("添加分类", new Color(70, 130, 180));
        JButton updateButton = createStyledButton("更新分类", new Color(60, 179, 113));
        JButton deleteButton = createStyledButton("删除分类", new Color(205, 92, 92));
        JButton clearButton = createStyledButton("清空", new Color(169, 169, 169));
        JButton refreshButton = createStyledButton("刷新", new Color(143, 188, 143));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        // 表格面板
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        categoryTable = new JTable();
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.setRowHeight(25);
        categoryTable.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("职业分类列表"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // 添加组件到主面板
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.SOUTH);

        // 事件处理
        addButton.addActionListener(e -> addCategory());
        updateButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());
        clearButton.addActionListener(e -> clearCategoryFields());
        refreshButton.addActionListener(e -> loadCategoryData());

        categoryTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = categoryTable.getSelectedRow();
                if (row >= 0) {
                    categoryIdField.setText(categoryTable.getValueAt(row, 0).toString());
                    categoryNameField.setText(categoryTable.getValueAt(row, 1).toString());
                }
            }
        });

        return panel;
    }

    private JPanel createJobManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 表单面板
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("职业信息管理"));
        formPanel.setBackground(new Color(240, 248, 255));

        formPanel.add(new JLabel("职业号:"));
        jobIdField = new JTextField();
        jobIdField.setEditable(false);
        jobIdField.setForeground(Color.BLACK);
        formPanel.add(jobIdField);

        formPanel.add(new JLabel("职业类型:"));
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setForeground(Color.BLACK);
        loadCategories();
        formPanel.add(categoryComboBox);

        formPanel.add(new JLabel("用人单位:"));
        employerField = new JTextField();
        employerField.setForeground(Color.BLACK);
        formPanel.add(employerField);

        formPanel.add(new JLabel("需求人数:"));
        requiredField = new JTextField();
        requiredField.setForeground(Color.BLACK);
        formPanel.add(requiredField);

        formPanel.add(new JLabel("备注:"));
        notesField = new JTextField();
        notesField.setForeground(Color.BLACK);
        formPanel.add(notesField);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton addButton = createStyledButton("添加", new Color(70, 130, 180));
        JButton updateButton = createStyledButton("更新", new Color(60, 179, 113));
        JButton deleteButton = createStyledButton("删除", new Color(205, 92, 92));
        JButton clearButton = createStyledButton("清空", new Color(169, 169, 169));
        JButton refreshButton = createStyledButton("刷新", new Color(143, 188, 143));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        // 表格面板
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        jobTable = new JTable();
        jobTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobTable.setRowHeight(25);
        jobTable.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(jobTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("职业列表"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // 添加组件到主面板
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.SOUTH);

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
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 表单面板
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("求职者信息管理"));
        formPanel.setBackground(new Color(240, 248, 255));

        formPanel.add(new JLabel("求职者ID:"));
        seekerIdField = new JTextField();
        seekerIdField.setEditable(false);
        seekerIdField.setForeground(Color.BLACK);
        formPanel.add(seekerIdField);

        formPanel.add(new JLabel("姓名:"));
        seekerNameField = new JTextField();
        seekerNameField.setForeground(Color.BLACK);
        formPanel.add(seekerNameField);

        formPanel.add(new JLabel("性别:"));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(new Color(240, 248, 255));
        ButtonGroup genderGroup = new ButtonGroup();
        maleRadio = new JRadioButton("男");
        femaleRadio = new JRadioButton("女");
        maleRadio.setSelected(true);
        maleRadio.setBackground(new Color(240, 248, 255));
        maleRadio.setForeground(Color.BLACK);
        femaleRadio.setBackground(new Color(240, 248, 255));
        femaleRadio.setForeground(Color.BLACK);
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        formPanel.add(genderPanel);

        formPanel.add(new JLabel("聘用状态:"));
        employedComboBox = new JComboBox<>(new String[]{"未聘用", "聘用成功"});
        employedComboBox.setForeground(Color.BLACK);
        formPanel.add(employedComboBox);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton addButton = createStyledButton("添加", new Color(70, 130, 180));
        JButton updateButton = createStyledButton("更新", new Color(60, 179, 113));
        JButton deleteButton = createStyledButton("删除", new Color(205, 92, 92));
        JButton clearButton = createStyledButton("清空", new Color(169, 169, 169));
        JButton refreshButton = createStyledButton("刷新", new Color(143, 188, 143));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        // 表格面板
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        seekerTable = new JTable();
        seekerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        seekerTable.setRowHeight(25);
        seekerTable.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(seekerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("求职者列表"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // 添加组件到主面板
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.SOUTH);

        // 事件处理
        addButton.addActionListener(e -> addJobSeeker());
        updateButton.addActionListener(e -> updateJobSeeker());
        deleteButton.addActionListener(e -> deleteJobSeeker());
        clearButton.addActionListener(e -> clearSeekerFields());
        refreshButton.addActionListener(e -> loadSeekerData());

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
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 表单面板
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("职业匹配管理"));
        formPanel.setBackground(new Color(240, 248, 255));

        formPanel.add(new JLabel("选择职业:"));
        jobComboBox = new JComboBox<>();
        jobComboBox.setForeground(Color.BLACK);
        loadJobs();
        formPanel.add(jobComboBox);

        formPanel.add(new JLabel("选择求职者:"));
        seekerComboBox = new JComboBox<>();
        seekerComboBox.setForeground(Color.BLACK);
        loadJobSeekers();
        formPanel.add(seekerComboBox);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton matchButton = createStyledButton("匹配", new Color(70, 130, 180));
        JButton deleteButton = createStyledButton("删除匹配", new Color(205, 92, 92));
        JButton refreshButton = createStyledButton("刷新数据", new Color(143, 188, 143));

        buttonPanel.add(matchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // 表格面板
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        matchingTable = new JTable();
        matchingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        matchingTable.setRowHeight(25);
        matchingTable.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(matchingTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("匹配记录"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // 添加组件到主面板
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.SOUTH);

        // 事件处理
        matchButton.addActionListener(e -> matchJobSeeker());
        deleteButton.addActionListener(e -> deleteMatching());
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
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    
        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));
    
        // 表单面板
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("费用记录管理"));
        formPanel.setBackground(new Color(240, 248, 255));
    
        formPanel.add(new JLabel("费用ID:"));
        feeIdField = new JTextField();
        feeIdField.setEditable(false);
        feeIdField.setForeground(Color.BLACK);
        formPanel.add(feeIdField);
    
        formPanel.add(new JLabel("用人单位名称:"));
        employerNameField = new JTextField();
        employerNameField.setForeground(Color.BLACK);
        formPanel.add(employerNameField);
    
        formPanel.add(new JLabel("用人单位费用:"));
        employerFeeField = new JTextField();
        employerFeeField.setForeground(Color.BLACK);
        formPanel.add(employerFeeField);
    
        formPanel.add(new JLabel("求职者名称:"));
        seekerNameField = new JTextField();
        seekerNameField.setForeground(Color.BLACK);
        formPanel.add(seekerNameField);
    
        formPanel.add(new JLabel("求职者费用:"));
        seekerFeeField = new JTextField();
        seekerFeeField.setForeground(Color.BLACK);
        formPanel.add(seekerFeeField);
    
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.setBackground(new Color(240, 248, 255));
    
        JButton addButton = createStyledButton("添加", new Color(70, 130, 180));
        JButton updateButton = createStyledButton("更新", new Color(60, 179, 113));
        JButton deleteButton = createStyledButton("删除", new Color(205, 92, 92));
        JButton clearButton = createStyledButton("清空", new Color(169, 169, 169));
        JButton refreshButton = createStyledButton("刷新", new Color(143, 188, 143));
    
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);
    
        // 表格面板
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
    
        // 创建表格模型并添加删除按钮列
        DefaultTableModel feeTableModel = new DefaultTableModel(
            new Object[]{"费用ID", "用人单位名称", "用人单位费用", "求职者名称", "求职者费用", "操作"}, // 添加表头
            0 // 初始行数为0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1; // 只有删除列可编辑
            }
        };
    
        feeTable = new JTable(feeTableModel);
        feeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        feeTable.setRowHeight(30);
        feeTable.setForeground(Color.BLACK);
    
        // 添加删除按钮列
        feeTable.getColumnModel().getColumn(feeTable.getColumnCount() - 1).setCellRenderer(new ButtonRenderer());
        feeTable.getColumnModel().getColumn(feeTable.getColumnCount() - 1).setCellEditor(new ButtonEditor(new JCheckBox()));
    
        JScrollPane scrollPane = new JScrollPane(feeTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("费用记录"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
    
        // 添加组件到主面板
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
    
        panel.add(mainPanel, BorderLayout.CENTER);
    
        // 事件处理
        addButton.addActionListener(e -> addFeeRecord());
        updateButton.addActionListener(e -> updateFeeRecord());
        deleteButton.addActionListener(e -> deleteSelectedFeeRecord());
        clearButton.addActionListener(e -> clearFeeFields());
        refreshButton.addActionListener(e -> loadFeeData());
    
        feeTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = feeTable.getSelectedRow();
                if (row >= 0) {
                    feeIdField.setText(feeTable.getValueAt(row, 0).toString());
                    employerNameField.setText(feeTable.getValueAt(row, 1).toString());
                    employerFeeField.setText(feeTable.getValueAt(row, 2).toString());
                    seekerNameField.setText(feeTable.getValueAt(row, 3).toString());
                    seekerFeeField.setText(feeTable.getValueAt(row, 4).toString());
                }
            }
        });
    
        return panel;
    }

    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 报表选项卡
        JTabbedPane reportTabbedPane = new JTabbedPane();

        // 职业统计报表
        JPanel jobStatsPanel = new JPanel(new BorderLayout(10, 10));
        jobStatsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jobStatsPanel.setBackground(new Color(253, 245, 230));

        JTextArea jobReportArea = new JTextArea();
        jobReportArea.setEditable(false);
        jobReportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jobReportArea.setForeground(Color.BLACK);
        jobReportArea.setBackground(new Color(253, 245, 230));
        JScrollPane jobScrollPane = new JScrollPane(jobReportArea);
        jobScrollPane.setBorder(BorderFactory.createTitledBorder("职业需求统计"));

        JButton jobReportButton = createStyledButton("生成职业统计报表", new Color(70, 130, 180));
        jobReportButton.addActionListener(e -> {
            String report = generateJobReport();
            jobReportArea.setText(report);
        });

        JPanel jobButtonPanel = new JPanel();
        jobButtonPanel.setBackground(new Color(253, 245, 230));
        jobButtonPanel.add(jobReportButton);

        jobStatsPanel.add(jobScrollPane, BorderLayout.CENTER);
        jobStatsPanel.add(jobButtonPanel, BorderLayout.SOUTH);

        // 费用统计报表
        JPanel feeStatsPanel = new JPanel(new BorderLayout(10, 10));
        feeStatsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        feeStatsPanel.setBackground(new Color(253, 245, 230));

        JTextArea feeReportArea = new JTextArea();
        feeReportArea.setEditable(false);
        feeReportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        feeReportArea.setForeground(Color.BLACK);
        feeReportArea.setBackground(new Color(253, 245, 230));
        JScrollPane feeScrollPane = new JScrollPane(feeReportArea);
        feeScrollPane.setBorder(BorderFactory.createTitledBorder("费用收支统计"));

        JButton feeReportButton = createStyledButton("生成费用统计报表", new Color(70, 130, 180));
        feeReportButton.addActionListener(e -> {
            String report = generateFeeReport();
            feeReportArea.setText(report);
        });

        JPanel feeButtonPanel = new JPanel();
        feeButtonPanel.setBackground(new Color(253, 245, 230));
        feeButtonPanel.add(feeReportButton);

        feeStatsPanel.add(feeScrollPane, BorderLayout.CENTER);
        feeStatsPanel.add(feeButtonPanel, BorderLayout.SOUTH);

        // 添加选项卡
        reportTabbedPane.addTab("职业统计", jobStatsPanel);
        reportTabbedPane.addTab("费用统计", feeStatsPanel);

        panel.add(reportTabbedPane, BorderLayout.CENTER);
        return panel;
    }

    // 数据库操作方法
    private void loadInitialData() {
        loadCategoryData();
        loadJobData();
        loadSeekerData();
        loadMatchingData();
        loadFeeData();
    }

    private void loadCategoryData() {
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT category_id, category_name FROM JobCategory")) {

            categoryTable.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载职业分类数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCategory() {
        try (Connection conn = DBConnector.getConnection()) {
            String categoryName = categoryNameField.getText().trim();
            
            if (categoryName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入分类名称", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = "INSERT INTO JobCategory (category_name) VALUES (?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, categoryName);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "职业分类添加成功！");
                loadCategoryData();
                clearCategoryFields();
                loadCategories(); // 刷新职业管理页面的分类下拉框
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "添加职业分类失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCategory() {
        if (categoryIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择要更新的职业分类", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnector.getConnection()) {
            int categoryId = Integer.parseInt(categoryIdField.getText());
            String categoryName = categoryNameField.getText().trim();
            
            if (categoryName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入分类名称", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = "UPDATE JobCategory SET category_name = ? WHERE category_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, categoryName);
                pstmt.setInt(2, categoryId);
                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "职业分类更新成功！");
                    loadCategoryData();
                    clearCategoryFields();
                    loadCategories(); // 刷新职业管理页面的分类下拉框
                } else {
                    JOptionPane.showMessageDialog(this, "更新失败，记录可能已被删除", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "无效的分类ID", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "更新职业分类失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory() {
        if (categoryIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择要删除的职业分类", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除这个职业分类吗？\n注意：删除分类可能会影响相关的职业记录。", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnector.getConnection()) {
                int categoryId = Integer.parseInt(categoryIdField.getText());
                
                // 检查是否有职业使用此分类
                try (PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM Job WHERE category_id = ?")) {
                    checkStmt.setInt(1, categoryId);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, "无法删除：该分类下还有职业记录，请先删除相关职业", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                String sql = "DELETE FROM JobCategory WHERE category_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, categoryId);
                    int rows = pstmt.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "职业分类删除成功！");
                        loadCategoryData();
                        clearCategoryFields();
                        loadCategories(); // 刷新职业管理页面的分类下拉框
                    } else {
                        JOptionPane.showMessageDialog(this, "删除失败，记录可能已被删除", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "无效的分类ID", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "删除职业分类失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearCategoryFields() {
        categoryIdField.setText("");
        categoryNameField.setText("");
        categoryTable.clearSelection();
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
            // 修复这里的错误：将"极"改为"+"
            JOptionPane.showMessageDialog(this, "加载匹配数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFeeData() {
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Fee")) {

            // 构建基础表格模型
            DefaultTableModel model = buildTableModel(rs);

            // 添加删除按钮列
            model.addColumn("操作");
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt("删除", i, model.getColumnCount() - 1);
            }

            feeTable.setModel(model);
        } catch (SQLException e) {
            // 修复这里的错误：将"极"改为"+"
            JOptionPane.showMessageDialog(this, "加载费用数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addJob() {
        try (Connection conn = DBConnector.getConnection()) {
            String category = (String) categoryComboBox.getSelectedItem();
            String employer = employerField.getText();
            int required = Integer.parseInt(requiredField.getText());
            String notes = notesField.getText(); // 修复变量名

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
                    // 修复这里的错误：将"极"改为"setInt"
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

    private void updateFeeRecord() {
        if (feeIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择要更新的费用记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnector.getConnection()) {
            int feeId = Integer.parseInt(feeIdField.getText());
            String employerName = employerNameField.getText();
            double employerFee = Double.parseDouble(employerFeeField.getText());
            String seekerName = seekerNameField.getText();
            double seekerFee = Double.parseDouble(seekerFeeField.getText());

            String sql = "UPDATE Fee SET employer_name = ?, employer_fee = ?, seeker_name = ?, seeker_fee = ? WHERE fee_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, employerName);
                pstmt.setDouble(2, employerFee);
                pstmt.setString(3, seekerName);
                pstmt.setDouble(4, seekerFee);
                pstmt.setInt(5, feeId);
                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "费用记录更新成功！");
                    loadFeeData();
                    clearFeeFields();
                } else {
                    JOptionPane.showMessageDialog(this, "更新失败，记录可能已被删除", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "费用必须是数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "更新费用记录失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 删除选中费用记录的方法
    private void deleteSelectedFeeRecord() {
        int selectedRow = feeTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的费用记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int feeId = Integer.parseInt(feeTable.getValueAt(selectedRow, 0).toString());
        deleteFeeRecord(feeId);
    }

    // 实际的删除逻辑
    private void deleteFeeRecord(int feeId) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要删除这个费用记录吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION // 修复常量名
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnector.getConnection()) {
                String sql = "DELETE FROM Fee WHERE fee_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, feeId);
                    int rows = pstmt.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "费用记录删除成功！");
                        loadFeeData();
                        clearFeeFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "删除失败，记录可能已被删除", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "删除费用记录失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFeeFields() {
        feeIdField.setText("");
        employerNameField.setText("");
        employerFeeField.setText("");
        seekerNameField.setText("");
        seekerFeeField.setText("");
        feeTable.clearSelection();
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
            return "报表生成失败: " + e.getMessage();
        }

        return report.toString();
    }

    private String generateFeeReport() {
        StringBuilder report = new StringBuilder();
        report.append("========= 费用收支统计报表 =========\n");
        report.append(String.format("%-15s %-20s %-15s %-20s %-15s\n",
                "费用ID", "用人单位", "费用金额", "求职者", "费用金额"));

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Fee")) {

            double totalEmployerFee = 0;
            double totalSeekerFee = 0;
            int count = 0;

            while (rs.next()) {
                int feeId = rs.getInt("fee_id");
                String employer = rs.getString("employer_name");
                double employerFee = rs.getDouble("employer_fee");
                String seeker = rs.getString("seeker_name");
                double seekerFee = rs.getDouble("seeker_fee");

                report.append(String.format("%-15d %-20s %-15.2f %-20s %-15.2f\n",
                        feeId, employer, employerFee, seeker, seekerFee));

                totalEmployerFee += employerFee;
                totalSeekerFee += seekerFee;
                count++;
            }

            report.append("---------------------------------------------\n");
            report.append(String.format("总计: %d 条记录\n", count));
            report.append(String.format("用人单位总费用: %.2f\n", totalEmployerFee));
            report.append(String.format("求职者总费用: %.2f\n", totalSeekerFee));
            report.append(String.format("总收入: %.2f\n", totalEmployerFee + totalSeekerFee));
            report.append("=============================================");
        } catch (SQLException e) {
            return "费用报表生成失败: " + e.getMessage();
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

    // 创建样式化按钮
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    // 表格中的按钮渲染器
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "删除" : value.toString());
            setBackground(new Color(205, 92, 92)); // 红色背景
            setForeground(Color.BLACK);
            return this;
        }
    }

    // 表格中的按钮编辑器
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setForeground(Color.BLACK);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "删除" : value.toString();
            button.setText(label);
            button.setBackground(new Color(205, 92, 92)); // 红色背景
            button.setForeground(Color.BLACK);
            isPushed = true;
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                // 在这里处理删除操作
                int feeId = Integer.parseInt(feeTable.getValueAt(row, 0).toString());
                deleteFeeRecord(feeId);
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
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

