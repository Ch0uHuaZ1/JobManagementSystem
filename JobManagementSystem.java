/**
 * 职业介绍信息管理系统 - 主界面类
 * 
 * 这是一个基于Swing的桌面应用程序，用于管理职业介绍相关的信息。
 * 系统包含以下主要功能模块：
 * 1. 职业分类管理 - 管理职业的分类信息
 * 2. 职业管理 - 管理具体的职业信息
 * 3. 求职者管理 - 管理求职者信息
 * 4. 职业匹配 - 将求职者与职业进行匹配
 * 5. 费用管理 - 管理用人单位和求职者的费用信息
 * 6. 统计报表 - 生成各种统计报表
 * 
 * 技术特点：
 * - 使用Swing构建图形用户界面
 * - 采用MySQL数据库存储数据
 * - 支持完整的CRUD操作（增删改查）
 * - 提供数据统计和报表功能
 * 
 * @author 系统开发者
 * @version 1.0
 * @since 2024
 */

// ==================== 导入必要的Java包 ====================
import java.awt.*;                    // 导入AWT包，用于图形界面组件
import java.sql.*;                    // 导入SQL包，用于数据库操作
import java.util.Vector;              // 导入Vector类，用于存储表格数据
import javax.swing.*;                 // 导入Swing包，用于现代图形界面组件
import javax.swing.table.DefaultTableModel;  // 导入表格模型类
import javax.swing.table.TableCellRenderer;  // 导入表格单元格渲染器接口

/**
 * 职业介绍信息管理系统主类
 * 继承自JFrame，作为应用程序的主窗口
 */
public class JobManagementSystem extends JFrame {

    // ==================== UI组件声明区域 ====================
    
    /**
     * 主选项卡面板 - 用于组织不同的功能模块
     * 包含6个选项卡：职业分类管理、职业管理、求职者管理、职业匹配、费用管理、统计报表
     */
    private JTabbedPane tabbedPane;
    
    /**
     * 数据表格组件 - 用于显示各种数据的列表
     * jobTable: 职业信息表格
     * seekerTable: 求职者信息表格  
     * matchingTable: 职业匹配记录表格
     * feeTable: 费用记录表格
     * categoryTable: 职业分类表格
     */
    private JTable jobTable, seekerTable, matchingTable, feeTable, categoryTable;
    
    /**
     * 下拉选择框组件 - 用于选择不同的选项
     * categoryComboBox: 职业分类选择框
     * jobComboBox: 职业选择框
     * seekerComboBox: 求职者选择框
     */
    private JComboBox<String> categoryComboBox, jobComboBox, seekerComboBox;
    
    /**
     * 文本输入框组件 - 用于输入各种信息
     * 职业管理相关：
     * - jobIdField: 职业ID输入框（只读）
     * - employerField: 用人单位输入框
     * - requiredField: 需求人数输入框
     * - notesField: 备注信息输入框
     * 
     * 求职者管理相关：
     * - seekerIdField: 求职者ID输入框（只读）
     * - seekerNameField: 求职者姓名输入框
     * 
     * 费用管理相关：
     * - seekerFeeField: 求职者费用输入框
     * - employerNameField: 用人单位名称输入框
     * - employerFeeField: 用人单位费用输入框
     * - feeIdField: 费用ID输入框（只读）
     * 
     * 职业分类管理相关：
     * - categoryIdField: 分类ID输入框（只读）
     * - categoryNameField: 分类名称输入框
     */
    private JTextField jobIdField, employerField, requiredField, notesField;
    private JTextField seekerIdField, seekerNameField, seekerFeeField;
    private JTextField employerNameField, employerFeeField, feeIdField;
    private JTextField categoryIdField, categoryNameField;
    
    /**
     * 单选按钮组件 - 用于性别选择
     * maleRadio: 男性选择按钮
     * femaleRadio: 女性选择按钮
     */
    private JRadioButton maleRadio, femaleRadio;
    
    /**
     * 聘用状态下拉框 - 用于选择求职者的聘用状态
     * 选项包括："未聘用"、"聘用成功"
     */
    private JComboBox<String> employedComboBox;

    // ==================== 构造函数 ====================
    
    /**
     * 构造函数 - 初始化职业介绍信息管理系统
     * 
     * 主要功能：
     * 1. 设置窗口标题为"职业介绍信息管理系统"
     * 2. 设置窗口大小为1200x800像素
     * 3. 设置窗口关闭操作为退出程序
     * 4. 将窗口居中显示
     * 5. 创建用户界面
     * 6. 加载初始数据
     */
    public JobManagementSystem() {
        super("职业介绍信息管理系统");  // 调用父类构造函数，设置窗口标题
        setSize(1200, 800);              // 设置窗口大小为1200像素宽，800像素高
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 设置关闭窗口时退出整个程序
        setLocationRelativeTo(null);     // 将窗口居中显示在屏幕中央

        // 创建用户界面 - 调用createUI方法构建所有界面组件
        createUI();

        // 加载初始数据 - 从数据库加载各种数据到界面表格中
        loadInitialData();
    }

    // ==================== UI创建方法 ====================
    
    /**
     * 创建主用户界面
     * 
     * 主要功能：
     * 1. 创建主选项卡面板
     * 2. 添加6个功能模块的选项卡
     * 3. 将选项卡面板添加到主窗口
     * 
     * 选项卡说明：
     * - 职业分类管理：管理职业的分类信息
     * - 职业管理：管理具体的职业信息
     * - 求职者管理：管理求职者信息
     * - 职业匹配：将求职者与职业进行匹配
     * - 费用管理：管理费用信息
     * - 统计报表：生成各种统计报表
     */
    private void createUI() {
        // 创建主选项卡面板 - 用于组织不同的功能模块
        tabbedPane = new JTabbedPane();

        // 添加6个功能模块的选项卡
        // 每个选项卡对应一个功能模块，使用createXXXPanel()方法创建对应的面板
        tabbedPane.addTab("职业分类管理", createCategoryManagementPanel());  // 第1个选项卡：职业分类管理
        tabbedPane.addTab("职业管理", createJobManagementPanel());           // 第2个选项卡：职业管理
        tabbedPane.addTab("求职者管理", createJobSeekerPanel());             // 第3个选项卡：求职者管理
        tabbedPane.addTab("职业匹配", createMatchingPanel());                // 第4个选项卡：职业匹配
        tabbedPane.addTab("费用管理", createFeePanel());                     // 第5个选项卡：费用管理
        tabbedPane.addTab("统计报表", createReportPanel());                  // 第6个选项卡：统计报表

        // 将选项卡面板添加到主窗口 - 作为主窗口的唯一内容组件
        add(tabbedPane);
    }

    /**
     * 创建职业分类管理面板
     * 
     * 面板布局：
     * - 北部：表单区域（分类ID、分类名称输入框）
     * - 中部：按钮区域（添加、更新、删除、清空、刷新按钮）
     * - 南部：表格区域（职业分类列表）
     * 
     * 主要功能：
     * 1. 添加新的职业分类
     * 2. 更新现有职业分类信息
     * 3. 删除职业分类（需要检查是否有关联的职业记录）
     * 4. 清空表单内容
     * 5. 刷新分类列表数据
     * 
     * 界面特点：
     * - 使用BorderLayout布局管理器，组件间距为10像素
     * - 表单使用GridLayout布局，2列显示
     * - 按钮使用FlowLayout布局，居中对齐
     * - 表格支持单选模式，点击行自动填充表单
     * 
     * @return 配置完成的职业分类管理面板
     */
    private JPanel createCategoryManagementPanel() {
        // ==================== 主面板创建 ====================
        // 创建主面板，使用BorderLayout布局管理器，组件间距为10像素
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        // 设置面板边距：上15像素，左15像素，下15像素，右15像素
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ==================== 表单面板创建 ====================
        // 创建表单面板，使用GridLayout布局，自动计算行数，2列，组件间距8像素
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        // 设置表单面板的标题边框
        formPanel.setBorder(BorderFactory.createTitledBorder("职业分类管理"));
        // 设置表单面板的背景色为浅蓝色
        formPanel.setBackground(new Color(240, 248, 255));

        // 添加分类ID标签和输入框
        formPanel.add(new JLabel("分类ID:"));  // 添加"分类ID:"标签
        categoryIdField = new JTextField();  // 创建分类ID输入框
        categoryIdField.setEditable(false);  // 设置为只读（ID由数据库自动生成）
        categoryIdField.setForeground(Color.BLACK);  // 设置字体颜色为黑色
        formPanel.add(categoryIdField);  // 将输入框添加到表单面板

        // 添加分类名称标签和输入框
        formPanel.add(new JLabel("分类名称:"));  // 添加"分类名称:"标签
        categoryNameField = new JTextField();  // 创建分类名称输入框
        categoryNameField.setForeground(Color.BLACK);  // 设置字体颜色为黑色
        formPanel.add(categoryNameField);  // 将输入框添加到表单面板

        // ==================== 按钮面板创建 ====================
        // 创建按钮面板，使用FlowLayout布局，居中对齐，组件间距15像素，上下边距10像素
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        // 设置按钮面板的背景色为浅蓝色
        buttonPanel.setBackground(new Color(240, 248, 255));

        // 创建各种功能按钮，使用createStyledButton方法创建统一样式的按钮
        JButton addButton = createStyledButton("添加分类", new Color(70, 130, 180));      // 蓝色添加按钮
        JButton updateButton = createStyledButton("更新分类", new Color(60, 179, 113));   // 绿色更新按钮
        JButton deleteButton = createStyledButton("删除分类", new Color(205, 92, 92));    // 红色删除按钮
        JButton clearButton = createStyledButton("清空", new Color(169, 169, 169));       // 灰色清空按钮
        JButton refreshButton = createStyledButton("刷新", new Color(143, 188, 143));     // 浅绿色刷新按钮

        // 将按钮添加到按钮面板
        buttonPanel.add(addButton);      // 添加"添加分类"按钮
        buttonPanel.add(updateButton);   // 添加"更新分类"按钮
        buttonPanel.add(deleteButton);   // 添加"删除分类"按钮
        buttonPanel.add(clearButton);    // 添加"清空"按钮
        buttonPanel.add(refreshButton);  // 添加"刷新"按钮

        // ==================== 表格面板创建 ====================
        // 创建表格面板，使用BorderLayout布局
        JPanel tablePanel = new JPanel(new BorderLayout());
        // 设置表格面板的上边距为25像素
        tablePanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        // 创建职业分类表格
        categoryTable = new JTable();  // 创建表格组件
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // 设置单选模式（一次只能选择一行）
        categoryTable.setRowHeight(25);  // 设置行高为25像素
        categoryTable.setForeground(Color.BLACK);  // 设置字体颜色为黑色
        
        // 创建表格的滚动面板
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        // 设置滚动面板的标题边框
        scrollPane.setBorder(BorderFactory.createTitledBorder("职业分类列表"));
        // 将滚动面板添加到表格面板的中央
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // ==================== 组件组装 ====================
        // 将表单面板添加到主面板的北部
        panel.add(formPanel, BorderLayout.NORTH);
        // 将按钮面板添加到主面板的中部
        panel.add(buttonPanel, BorderLayout.CENTER);
        // 将表格面板添加到主面板的南部
        panel.add(tablePanel, BorderLayout.SOUTH);

        // ==================== 事件处理 ====================
        // 为各个按钮添加事件监听器，使用Lambda表达式简化代码
        addButton.addActionListener(e -> addCategory());        // 添加分类按钮事件：点击时调用addCategory()方法
        updateButton.addActionListener(e -> updateCategory());  // 更新分类按钮事件：点击时调用updateCategory()方法
        deleteButton.addActionListener(e -> deleteCategory());  // 删除分类按钮事件：点击时调用deleteCategory()方法
        clearButton.addActionListener(e -> clearCategoryFields());  // 清空按钮事件：点击时调用clearCategoryFields()方法
        refreshButton.addActionListener(e -> loadCategoryData());    // 刷新按钮事件：点击时调用loadCategoryData()方法

        // 为表格添加选择事件监听器
        categoryTable.getSelectionModel().addListSelectionListener(event -> {
            // 确保选择事件不是正在调整中（避免重复触发）
            if (!event.getValueIsAdjusting()) {
                int row = categoryTable.getSelectedRow();  // 获取选中的行索引
                if (row >= 0) {  // 确保有行被选中（行索引大于等于0）
                    // 将选中行的数据填充到表单中
                    categoryIdField.setText(categoryTable.getValueAt(row, 0).toString());    // 第0列：分类ID
                    categoryNameField.setText(categoryTable.getValueAt(row, 1).toString());  // 第1列：分类名称
                }
            }
        });

        // 返回配置完成的职业分类管理面板
        return panel;
    }

    /**
     * 创建职业管理面板
     * 
     * 面板布局：
     * - 北部：表单区域（职业号、职业类型、用人单位、需求人数、备注）
     * - 中部：按钮区域（添加、更新、删除、清空、刷新）
     * - 南部：表格区域（职业列表）
     * 
     * 主要功能：
     * 1. 添加、更新、删除职业信息
     * 2. 清空表单内容
     * 3. 刷新职业列表
     * 4. 点击表格行自动填充表单
     * 
     * 数据联动：
     * - 职业类型下拉框与职业分类管理联动
     * - 职业变更后自动刷新下拉框
     * 
     * @return 配置完成的职业管理面板
     */
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

    // ==================== 数据库操作方法 ====================
    
    /**
     * 加载初始数据
     * 
     * 在系统启动时调用，用于从数据库加载所有模块的初始数据到界面表格中。
     * 加载顺序：
     * 1. 职业分类数据 - 用于分类管理页面
     * 2. 职业数据 - 用于职业管理页面
     * 3. 求职者数据 - 用于求职者管理页面
     * 4. 匹配数据 - 用于职业匹配页面
     * 5. 费用数据 - 用于费用管理页面
     * 
     * 调用时机：构造函数中，在创建UI之后
     */
    private void loadInitialData() {
        loadCategoryData();    // 加载职业分类数据
        loadJobData();         // 加载职业数据
        loadSeekerData();      // 加载求职者数据
        loadMatchingData();    // 加载匹配数据
        loadFeeData();         // 加载费用数据
    }

    /**
     * 加载职业分类数据
     * 
     * 从数据库的JobCategory表中查询所有职业分类信息，
     * 并将结果显示在职业分类管理页面的表格中。
     * 
     * 查询字段：
     * - category_id: 分类ID
     * - category_name: 分类名称
     * 
     * 异常处理：
     * - 如果数据库连接失败，显示错误对话框
     * - 如果SQL执行失败，显示具体的错误信息
     */
    private void loadCategoryData() {
        try (Connection conn = DBConnector.getConnection();  // 获取数据库连接
             Statement stmt = conn.createStatement();        // 创建SQL语句对象
             ResultSet rs = stmt.executeQuery("SELECT category_id, category_name FROM JobCategory")) {  // 执行查询

            // 将查询结果设置为表格的数据模型
            categoryTable.setModel(buildTableModel(rs));
            
        } catch (SQLException e) {
            // 捕获SQL异常，显示用户友好的错误信息
            JOptionPane.showMessageDialog(this, 
                "加载职业分类数据失败: " + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 添加职业分类
     * 
     * 将用户输入的职业分类信息插入到数据库中。
     * 
     * 操作流程：
     * 1. 获取用户输入的分类名称
     * 2. 验证输入不为空
     * 3. 执行INSERT SQL语句
     * 4. 刷新分类列表
     * 5. 清空表单
     * 6. 更新职业管理页面的分类下拉框
     * 
     * 数据验证：
     * - 分类名称不能为空
     * - 分类名称会自动去除首尾空格
     * 
     * 异常处理：
     * - SQL异常：显示错误对话框
     * - 输入验证失败：显示警告对话框
     */
    private void addCategory() {
        try (Connection conn = DBConnector.getConnection()) {  // 获取数据库连接
            // 获取用户输入的分类名称，并去除首尾空格
            String categoryName = categoryNameField.getText().trim();
            
            // 验证输入不为空
            if (categoryName.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "请输入分类名称", 
                    "提示", 
                    JOptionPane.WARNING_MESSAGE);
                return;  // 输入为空，直接返回
            }

            // 准备INSERT SQL语句，使用参数化查询防止SQL注入
            String sql = "INSERT INTO JobCategory (category_name) VALUES (?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, categoryName);  // 设置第一个参数为分类名称
                pstmt.executeUpdate();              // 执行插入操作
                
                // 插入成功，显示成功消息
                JOptionPane.showMessageDialog(this, "职业分类添加成功！");
                
                // 刷新相关数据
                loadCategoryData();     // 重新加载分类列表
                clearCategoryFields();  // 清空表单
                loadCategories();       // 刷新职业管理页面的分类下拉框
            }
        } catch (SQLException e) {
            // 捕获SQL异常，显示错误信息
            JOptionPane.showMessageDialog(this, 
                "添加职业分类失败: " + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 更新职业分类
     * 
     * 根据用户选择的分类记录，更新数据库中的职业分类信息。
     * 
     * 操作流程：
     * 1. 验证是否选择了要更新的分类
     * 2. 获取分类ID和新的分类名称
     * 3. 验证输入不为空
     * 4. 执行UPDATE SQL语句
     * 5. 检查更新结果
     * 6. 刷新相关数据
     * 
     * 数据验证：
     * - 必须选择要更新的分类（分类ID不为空）
     * - 新的分类名称不能为空
     * 
     * 异常处理：
     * - NumberFormatException：分类ID格式错误
     * - SQLException：数据库操作失败
     */
    private void updateCategory() {
        // 验证是否选择了要更新的分类
        if (categoryIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "请选择要更新的职业分类", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnector.getConnection()) {  // 获取数据库连接
            // 解析分类ID（字符串转整数）
            int categoryId = Integer.parseInt(categoryIdField.getText());
            // 获取新的分类名称，并去除首尾空格
            String categoryName = categoryNameField.getText().trim();
            
            // 验证新的分类名称不为空
            if (categoryName.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "请输入分类名称", 
                    "提示", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 准备UPDATE SQL语句
            String sql = "UPDATE JobCategory SET category_name = ? WHERE category_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, categoryName);  // 设置新的分类名称
                pstmt.setInt(2, categoryId);       // 设置分类ID
                int rows = pstmt.executeUpdate();  // 执行更新操作，返回受影响的行数

                // 检查更新结果
                if (rows > 0) {
                    // 更新成功
                    JOptionPane.showMessageDialog(this, "职业分类更新成功！");
                    loadCategoryData();     // 重新加载分类列表
                    clearCategoryFields();  // 清空表单
                    loadCategories();       // 刷新职业管理页面的分类下拉框
                } else {
                    // 没有记录被更新（可能记录已被删除）
                    JOptionPane.showMessageDialog(this, 
                        "更新失败，记录可能已被删除", 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            // 分类ID格式错误
            JOptionPane.showMessageDialog(this, 
                "无效的分类ID", 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            // 数据库操作失败
            JOptionPane.showMessageDialog(this, 
                "更新职业分类失败: " + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 删除职业分类
     * 
     * 删除用户选择的职业分类记录，但会先检查是否有关联的职业记录。
     * 
     * 安全机制：
     * 1. 删除前会检查该分类下是否有职业记录
     * 2. 如果有职业记录，会阻止删除并提示用户
     * 3. 用户需要先删除相关职业记录才能删除分类
     * 
     * 操作流程：
     * 1. 验证是否选择了要删除的分类
     * 2. 显示确认对话框
     * 3. 检查是否有关联的职业记录
     * 4. 执行DELETE SQL语句
     * 5. 刷新相关数据
     * 
     * 数据完整性保护：
     * - 防止删除有外键引用的分类
     * - 维护数据库的引用完整性
     */
    private void deleteCategory() {
        // 验证是否选择了要删除的分类
        if (categoryIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "请选择要删除的职业分类", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 显示确认对话框，提醒用户删除操作的后果
        int confirm = JOptionPane.showConfirmDialog(this, 
            "确定要删除这个职业分类吗？\n注意：删除分类可能会影响相关的职业记录。", 
            "确认删除", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {  // 用户确认删除
            try (Connection conn = DBConnector.getConnection()) {  // 获取数据库连接
                int categoryId = Integer.parseInt(categoryIdField.getText());  // 解析分类ID
                
                // ==================== 安全检查：检查是否有职业使用此分类 ====================
                try (PreparedStatement checkStmt = conn.prepareStatement(
                        "SELECT COUNT(*) FROM Job WHERE category_id = ?")) {
                    checkStmt.setInt(1, categoryId);  // 设置分类ID参数
                    ResultSet rs = checkStmt.executeQuery();  // 执行查询
                    if (rs.next() && rs.getInt(1) > 0) {  // 如果有职业记录使用此分类
                        JOptionPane.showMessageDialog(this, 
                            "无法删除：该分类下还有职业记录，请先删除相关职业", 
                            "错误", 
                            JOptionPane.ERROR_MESSAGE);
                        return;  // 阻止删除操作
                    }
                }

                // ==================== 执行删除操作 ====================
                String sql = "DELETE FROM JobCategory WHERE category_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, categoryId);  // 设置分类ID参数
                    int rows = pstmt.executeUpdate();  // 执行删除操作

                    // 检查删除结果
                    if (rows > 0) {
                        // 删除成功
                        JOptionPane.showMessageDialog(this, "职业分类删除成功！");
                        loadCategoryData();     // 重新加载分类列表
                        clearCategoryFields();  // 清空表单
                        loadCategories();       // 刷新职业管理页面的分类下拉框
                    } else {
                        // 没有记录被删除
                        JOptionPane.showMessageDialog(this, 
                            "删除失败，记录可能已被删除", 
                            "错误", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                // 分类ID格式错误
                JOptionPane.showMessageDialog(this, 
                    "无效的分类ID", 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                // 数据库操作失败
                JOptionPane.showMessageDialog(this, 
                    "删除职业分类失败: " + e.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 清空职业分类表单
     * 
     * 清空职业分类管理页面中所有输入框的内容，
     * 并取消表格中的选择状态。
     * 
     * 清空内容：
     * - 分类ID输入框
     * - 分类名称输入框
     * - 表格选择状态
     */
    private void clearCategoryFields() {
        categoryIdField.setText("");      // 清空分类ID输入框
        categoryNameField.setText("");    // 清空分类名称输入框
        categoryTable.clearSelection();   // 取消表格选择状态
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

    /**
     * 添加职业信息
     *
     * 从表单获取用户输入，插入到Job表。自动根据职业分类名称查找category_id。
     *
     * 主要流程：
     * 1. 获取表单输入（分类、用人单位、需求人数、备注）
     * 2. 验证输入合法性（需求人数为数字，分类存在）
     * 3. 查询分类ID
     * 4. 执行INSERT语句
     * 5. 刷新表格和下拉框
     *
     * 异常处理：
     * - 需求人数非数字时提示
     * - SQL异常弹窗提示
     */
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

    /**
     * 更新职业信息
     *
     * 根据表单内容和选中行的职业ID，更新Job表对应记录。
     *
     * 主要流程：
     * 1. 获取表单输入和职业ID
     * 2. 验证输入合法性
     * 3. 查询分类ID
     * 4. 执行UPDATE语句
     * 5. 刷新表格和下拉框
     *
     * 异常处理：
     * - 需求人数非数字时提示
     * - SQL异常弹窗提示
     */
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

    /**
     * 删除职业信息
     *
     * 根据表单选中的职业ID，删除Job表对应记录。
     *
     * 主要流程：
     * 1. 验证是否选择了要删除的职业
     * 2. 弹窗确认
     * 3. 执行DELETE语句
     * 4. 刷新表格和下拉框
     *
     * 异常处理：
     * - 职业ID格式错误
     * - SQL异常弹窗提示
     */
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

    /**
     * 添加求职者信息
     *
     * 从表单获取用户输入，插入到JobSeeker表。
     *
     * 主要流程：
     * 1. 获取表单输入（姓名、性别、聘用状态）
     * 2. 执行INSERT语句
     * 3. 刷新表格和下拉框
     *
     * 异常处理：
     * - SQL异常弹窗提示
     */
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

    /**
     * 更新求职者信息
     *
     * 根据表单内容和选中行的求职者ID，更新JobSeeker表对应记录。
     *
     * 主要流程：
     * 1. 获取表单输入和求职者ID
     * 2. 执行UPDATE语句
     * 3. 刷新表格和下拉框
     *
     * 异常处理：
     * - 求职者ID格式错误
     * - SQL异常弹窗提示
     */
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

    /**
     * 删除求职者信息
     *
     * 根据表单选中的求职者ID，删除JobSeeker表对应记录。
     *
     * 主要流程：
     * 1. 验证是否选择了要删除的求职者
     * 2. 弹窗确认
     * 3. 执行DELETE语句
     * 4. 刷新表格和下拉框
     *
     * 异常处理：
     * - 求职者ID格式错误
     * - SQL异常弹窗提示
     */
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

    /**
     * 匹配求职者到职业
     *
     * 将选中的求职者与职业进行匹配，插入JobMatching表，并更新相关状态。
     *
     * 主要流程：
     * 1. 获取下拉框选中的职业和求职者
     * 2. 查询对应ID
     * 3. 检查职业需求人数和求职者聘用状态
     * 4. 插入匹配记录
     * 5. 更新职业已聘人数和求职者状态
     * 6. 刷新相关表格和下拉框
     *
     * 异常处理：
     * - SQL异常弹窗提示
     */
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
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT employed FROM JobSeeker WHERE seeker_id =?")) {
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

    /**
     * 更新职业已聘人数
     *
     * @param conn 数据库连接
     * @param jobId 职业ID
     * @param increment 增加或减少的数量（+1/-1）
     * @throws SQLException SQL异常
     */
    private void updateHiredCount(Connection conn, int jobId, int increment) throws SQLException {
        String sql = "UPDATE Job SET hired_count = hired_count + ? WHERE job_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, increment);
            pstmt.setInt(2, jobId);
            pstmt.executeUpdate();
        }
    }

    /**
     * 更新求职者聘用状态
     *
     * @param conn 数据库连接
     * @param seekerId 求职者ID
     * @param employed 聘用状态
     * @throws SQLException SQL异常
     */
    private void updateSeekerEmployedStatus(Connection conn, int seekerId, boolean employed) throws SQLException {
        String sql = "UPDATE JobSeeker SET employed = ? WHERE seeker_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, employed);
            pstmt.setInt(2, seekerId);
            pstmt.executeUpdate();
        }
    }

    /**
     * 删除匹配记录
     *
     * 删除选中的匹配记录，并同步更新职业已聘人数和求职者状态。
     *
     * 主要流程：
     * 1. 获取选中行的match_id
     * 2. 查询对应的job_id和seeker_id
     * 3. 执行DELETE语句
     * 4. 更新职业和求职者状态
     * 5. 刷新相关表格和下拉框
     *
     * 异常处理：
     * - SQL异常弹窗提示
     */
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

    /**
     * 添加费用记录
     *
     * 从表单获取用户输入，插入到Fee表。
     *
     * 主要流程：
     * 1. 获取表单输入（用人单位、用人单位费用、求职者、求职者费用）
     * 2. 验证费用为数字
     * 3. 执行INSERT语句
     * 4. 刷新表格和表单
     *
     * 异常处理：
     * - 费用非数字时提示
     * - SQL异常弹窗提示
     */
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

    /**
     * 更新费用记录
     *
     * 根据表单内容和选中行的费用ID，更新Fee表对应记录。
     *
     * 主要流程：
     * 1. 获取表单输入和费用ID
     * 2. 验证费用为数字
     * 3. 执行UPDATE语句
     * 4. 刷新表格和表单
     *
     * 异常处理：
     * - 费用非数字时提示
     * - SQL异常弹窗提示
     */
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

    /**
     * 删除选中的费用记录（通过表格按钮或外部按钮）
     *
     * 主要流程：
     * 1. 获取选中行的fee_id
     * 2. 调用deleteFeeRecord方法
     */
    private void deleteSelectedFeeRecord() {
        int selectedRow = feeTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的费用记录", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int feeId = Integer.parseInt(feeTable.getValueAt(selectedRow, 0).toString());
        deleteFeeRecord(feeId);
    }

    /**
     * 实际执行费用记录删除的逻辑
     *
     * @param feeId 费用ID
     *
     * 主要流程：
     * 1. 弹窗确认
     * 2. 执行DELETE语句
     * 3. 刷新表格和表单
     *
     * 异常处理：
     * - SQL异常弹窗提示
     */
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

    /**
     * 生成职业统计报表
     *
     * 调用存储过程GetJobStats，统计每个职业的需求人数、已聘人数、空缺人数。
     *
     * @return 报表字符串
     */
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

    /**
     * 生成费用统计报表
     *
     * 查询Fee表，统计所有费用记录及总收入。
     *
     * @return 报表字符串
     */
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
        // 获取ResultSet的元数据信息，用于了解结果集的结构
        ResultSetMetaData metaData = rs.getMetaData();

        // ==================== 提取列名 ====================
        // 获取结果集的列数
        int columnCount = metaData.getColumnCount();
        
        // 创建列名向量，用于存储表格的表头
        Vector<String> columnNames = new Vector<>();
        
        // 遍历所有列，提取列名
        for (int column = 1; column <= columnCount; column++) {
            // 使用getColumnName获取列名，列索引从1开始
            columnNames.add(metaData.getColumnName(column));
        }

        // ==================== 提取数据行 ====================
        // 创建数据向量，用于存储所有行的数据
        Vector<Vector<Object>> data = new Vector<>();
        
        // 遍历ResultSet的每一行数据
        while (rs.next()) {
            // 为当前行创建一个向量，用于存储该行的所有列值
            Vector<Object> vector = new Vector<>();
            
            // 遍历当前行的所有列
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                // 使用getObject获取列值，支持所有数据类型
                vector.add(rs.getObject(columnIndex));
            }
            
            // 将当前行数据添加到数据向量中
            data.add(vector);
        }

        // 创建并返回DefaultTableModel对象
        // 参数1：数据向量（所有行的数据）
        // 参数2：列名向量（表格表头）
        return new DefaultTableModel(data, columnNames);
    }

    /**
     * 创建统一风格的按钮
     * 
     * 用于创建系统中所有按钮的统一外观样式，确保界面风格一致。
     * 
     * @param text 按钮显示的文本内容
     * @param bgColor 按钮的背景颜色
     * @return 返回配置好的JButton对象
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);  // 创建按钮并设置文本
        button.setBackground(bgColor);       // 设置按钮背景色
        button.setForeground(Color.BLACK);   // 设置按钮文字颜色为黑色
        button.setFocusPainted(false);       // 禁用焦点边框绘制，使按钮更简洁
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));  // 设置字体为微软雅黑，粗体，12号
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));  // 设置按钮内边距：上下5像素，左右15像素
        return button;  // 返回配置好的按钮
    }

    /**
     * 表格中的按钮渲染器类
     * 
     * 继承自JButton并实现TableCellRenderer接口，用于在表格单元格中显示自定义的"删除"按钮。
     * 当表格需要显示某个单元格时，会调用此类的getTableCellRendererComponent方法。
     * 
     * 主要功能：
     * - 在表格的"操作"列中显示红色的"删除"按钮
     * - 统一按钮的外观样式（红色背景、黑色文字）
     * - 处理按钮的显示状态（选中、焦点等）
     * 
     * 使用场景：
     * - 费用管理表格的"操作"列
     * - 其他需要行内操作按钮的表格
     */
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        
        /**
         * 构造函数
         * 
         * 初始化按钮渲染器，设置按钮为不透明模式。
         * 不透明模式确保按钮背景色能够正确显示。
         */
        public ButtonRenderer() {
            setOpaque(true);  // 设置按钮为不透明，确保背景色可见
        }

        /**
         * 获取表格单元格渲染组件
         * 
         * 这是TableCellRenderer接口的核心方法，当表格需要渲染某个单元格时会被调用。
         * 根据传入的参数设置按钮的显示状态和内容。
         * 
         * @param table 要渲染的表格对象
         * @param value 单元格的值（通常为"删除"字符串）
         * @param isSelected 当前单元格是否被选中
         * @param hasFocus 当前单元格是否具有焦点
         * @param row 行索引
         * @param column 列索引
         * @return 返回配置好的按钮组件，用于在表格中显示
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            // 设置按钮文本，如果value为null则显示"删除"，否则显示value的字符串表示
            setText((value == null) ? "删除" : value.toString());
            
            // 设置按钮背景色为红色（RGB: 205, 92, 92），表示删除操作
            setBackground(new Color(205, 92, 92));
            
            // 设置按钮文字颜色为黑色，确保在红色背景上有良好的可读性
            setForeground(Color.BLACK);
            
            // 返回配置好的按钮组件
            return this;
        }
    }

    /**
     * 表格中的按钮编辑器类
     * 
     * 继承自DefaultCellEditor，用于处理表格单元格中按钮的点击事件。
     * 当用户点击表格中的"删除"按钮时，会触发相应的删除操作。
     * 
     * 主要功能：
     * - 在表格单元格中创建可点击的"删除"按钮
     * - 处理按钮点击事件，执行删除操作
     * - 管理按钮的编辑状态和生命周期
     * 
     * 工作流程：
     * 1. 用户点击表格中的"删除"按钮
     * 2. 触发getTableCellEditorComponent方法，创建编辑状态的按钮
     * 3. 用户点击按钮，触发ActionListener
     * 4. 调用getCellEditorValue方法，执行删除逻辑
     * 5. 调用stopCellEditing方法，结束编辑状态
     */
    class ButtonEditor extends DefaultCellEditor {
        
        /**
         * 按钮组件
         * 用于在表格单元格中显示的删除按钮
         */
        private JButton button;
        
        /**
         * 按钮标签文本
         * 存储按钮显示的文本内容（通常为"删除"）
         */
        private String label;
        
        /**
         * 按钮是否被点击的标志
         * 用于判断用户是否点击了按钮，从而决定是否执行删除操作
         */
        private boolean isPushed;
        
        /**
         * 当前编辑的行索引
         * 记录用户点击的是哪一行的按钮，用于获取对应的数据ID
         */
        private int row;

        /**
         * 构造函数
         * 
         * 初始化按钮编辑器，创建按钮组件并设置事件监听器。
         * 
         * @param checkBox 父类DefaultCellEditor需要的复选框参数（实际不使用）
         */
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);  // 调用父类构造函数
            
            // 创建按钮组件
            button = new JButton();
            
            // 设置按钮为不透明，确保背景色可见
            button.setOpaque(true);
            
            // 设置按钮文字颜色为黑色
            button.setForeground(Color.BLACK);
            
            // 为按钮添加点击事件监听器
            // 当按钮被点击时，调用fireEditingStopped()方法结束编辑状态
            button.addActionListener(e -> fireEditingStopped());
        }

        /**
         * 获取表格单元格编辑器组件
         * 
         * 当用户开始编辑表格单元格时被调用，返回用于编辑的按钮组件。
         * 
         * @param table 要编辑的表格对象
         * @param value 单元格的当前值
         * @param isSelected 当前单元格是否被选中
         * @param row 行索引
         * @param column 列索引
         * @return 返回配置好的按钮组件，用于用户交互
         */
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            // 设置按钮标签文本，如果value为null则显示"删除"
            label = (value == null) ? "删除" : value.toString();
            
            // 设置按钮显示的文本
            button.setText(label);
            
            // 设置按钮背景色为红色，表示删除操作
            button.setBackground(new Color(205, 92, 92));
            
            // 设置按钮文字颜色为黑色
            button.setForeground(Color.BLACK);
            
            // 标记按钮已被点击，准备执行删除操作
            isPushed = true;
            
            // 记录当前编辑的行索引，用于后续获取数据
            this.row = row;
            
            // 返回配置好的按钮组件
            return button;
        }

        /**
         * 获取单元格编辑器的值
         * 
         * 当编辑结束时被调用，返回编辑结果。
         * 如果按钮被点击（isPushed为true），则执行删除操作。
         * 
         * @return 返回按钮的标签文本
         */
        @Override
        public Object getCellEditorValue() {
            // 检查按钮是否被点击
            if (isPushed) {
                // 获取当前行的费用ID（第0列）
                int feeId = Integer.parseInt(feeTable.getValueAt(row, 0).toString());
                
                // 调用删除方法，执行实际的删除操作
                deleteFeeRecord(feeId);
            }
            
            // 重置点击标志
            isPushed = false;
            
            // 返回按钮标签文本
            return label;
        }

        /**
         * 停止单元格编辑
         * 
         * 当编辑状态结束时被调用，重置内部状态。
         * 
         * @return 总是返回true，表示可以停止编辑
         */
        @Override
        public boolean stopCellEditing() {
            // 重置点击标志
            isPushed = false;
            
            // 调用父类方法停止编辑
            return super.stopCellEditing();
        }
    }

    /**
     * 程序入口点 - main方法
     * 
     * 这是Java应用程序的入口方法，负责启动职业介绍信息管理系统。
     * 使用SwingUtilities.invokeLater确保GUI代码在事件调度线程(EDT)中执行，
     * 这是Swing应用程序的标准做法，避免线程安全问题。
     * 
     * 启动流程：
     * 1. 使用SwingUtilities.invokeLater确保在EDT（事件调度线程）中运行
     * 2. 设置系统UI风格，使界面看起来更现代化
     * 3. 创建JobManagementSystem实例
     * 4. 显示主窗口
     * 
     * 技术要点：
     * - SwingUtilities.invokeLater：确保GUI代码在EDT中执行，避免线程安全问题
     * - UIManager.setLookAndFeel：设置系统原生UI风格，提升用户体验
     * - 异常处理：捕获UI风格设置异常，确保程序稳定运行
     * - Lambda表达式：使用现代Java语法简化代码
     * 
     * 线程安全：
     * Swing组件必须在EDT中创建和操作，否则可能导致不可预期的行为。
     * invokeLater方法将Runnable任务提交到EDT队列中，确保在正确的线程中执行。
     * 
     * @param args 命令行参数（本程序未使用，但保留标准main方法签名）
     */
    public static void main(String[] args) {
        // 使用SwingUtilities.invokeLater确保在事件调度线程(EDT)中运行GUI代码
        // 这是Swing应用程序的标准做法，避免线程安全问题
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置系统UI风格，使界面看起来更现代化
                // 使用系统原生的外观和感觉，而不是Java默认的Metal风格
                // 这样可以让应用程序在不同操作系统上显示为原生风格
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
            } catch (Exception e) {
                // 如果设置UI风格失败，打印异常信息但不影响程序运行
                // 程序会使用默认的Metal风格继续运行
                e.printStackTrace();
            }

            // 创建JobManagementSystem实例并显示主窗口
            // 构造函数会自动初始化所有界面组件和加载初始数据
            new JobManagementSystem().setVisible(true);
        });
    }

    /**
     * 清空职业管理表单
     *
     * 清空职业管理页面所有输入框内容，并取消表格选择状态。
     * 通常在添加新记录或刷新数据后调用，为用户提供清洁的输入界面。
     * 
     * 清空内容：
     * - 职业ID输入框（设为空字符串）
     * - 职业分类下拉框（设为第一项）
     * - 用人单位输入框（设为空字符串）
     * - 需求人数输入框（设为空字符串）
     * - 备注输入框（设为空字符串）
     * - 表格选择状态（取消所有选择）
     * 
     * 调用时机：
     * - 点击"清空"按钮时
     * - 添加职业成功后
     * - 更新职业成功后
     * - 删除职业成功后
     */
    private void clearJobFields() {
        jobIdField.setText("");                    // 清空职业ID输入框
        categoryComboBox.setSelectedIndex(0);      // 将分类下拉框设为第一项
        employerField.setText("");                 // 清空用人单位输入框
        requiredField.setText("");                 // 清空需求人数输入框
        notesField.setText("");                    // 清空备注输入框
        jobTable.clearSelection();                 // 取消表格选择状态
    }

    /**
     * 清空求职者管理表单
     *
     * 清空求职者管理页面所有输入框内容，并取消表格选择状态。
     * 通常在添加新记录或刷新数据后调用，为用户提供清洁的输入界面。
     * 
     * 清空内容：
     * - 求职者ID输入框（设为空字符串）
     * - 求职者姓名输入框（设为空字符串）
     * - 性别单选按钮（设为男性）
     * - 聘用状态下拉框（设为第一项"未聘用"）
     * - 表格选择状态（取消所有选择）
     * 
     * 调用时机：
     * - 点击"清空"按钮时
     * - 添加求职者成功后
     * - 更新求职者成功后
     * - 删除求职者成功后
     */
    private void clearSeekerFields() {
        seekerIdField.setText("");                 // 清空求职者ID输入框
        seekerNameField.setText("");               // 清空求职者姓名输入框
        maleRadio.setSelected(true);               // 将性别设为男性（默认值）
        employedComboBox.setSelectedIndex(0);      // 将聘用状态设为第一项"未聘用"
        seekerTable.clearSelection();              // 取消表格选择状态
    }

    /**
     * 清空费用管理表单
     *
     * 清空费用管理页面所有输入框内容，并取消表格选择状态。
     * 通常在添加新记录或刷新数据后调用，为用户提供清洁的输入界面。
     * 
     * 清空内容：
     * - 费用ID输入框（设为空字符串）
     * - 用人单位名称输入框（设为空字符串）
     * - 用人单位费用输入框（设为空字符串）
     * - 求职者姓名输入框（设为空字符串）
     * - 求职者费用输入框（设为空字符串）
     * - 表格选择状态（取消所有选择）
     * 
     * 调用时机：
     * - 点击"清空"按钮时
     * - 添加费用记录成功后
     * - 更新费用记录成功后
     * - 删除费用记录成功后
     */
    private void clearFeeFields() {
        feeIdField.setText("");                    // 清空费用ID输入框
        employerNameField.setText("");             // 清空用人单位名称输入框
        employerFeeField.setText("");              // 清空用人单位费用输入框
        seekerNameField.setText("");               // 清空求职者姓名输入框
        seekerFeeField.setText("");                // 清空求职者费用输入框
        feeTable.clearSelection();                 // 取消表格选择状态
    }
}

/**
 * 职业介绍信息管理系统 - 注释总结
 * 
 * 本系统是一个完整的职业介绍信息管理平台，具有以下特点：
 * 
 * 1. 功能模块：
 *    - 职业分类管理：管理职业的分类信息
 *    - 职业管理：管理具体的职业信息
 *    - 求职者管理：管理求职者信息
 *    - 职业匹配：将求职者与职业进行匹配
 *    - 费用管理：管理费用信息
 *    - 统计报表：生成各种统计报表
 * 
 * 2. 技术特点：
 *    - 使用Swing构建图形用户界面
 *    - 采用MySQL数据库存储数据
 *    - 支持完整的CRUD操作（增删改查）
 *    - 提供数据统计和报表功能
 *    - 使用参数化查询防止SQL注入
 *    - 实现数据完整性保护
 * 
 * 3. 代码结构：
 *    - 清晰的模块化设计
 *    - 详细的注释说明
 *    - 完善的异常处理
 *    - 用户友好的界面设计
 * 
 * 4. 安全特性：
 *    - 数据库连接安全
 *    - 输入数据验证
 *    - 外键约束保护
 *    - 用户操作确认
 * 
 * 作者：系统开发者
 * 版本：1.0
 * 日期：2024
 */

