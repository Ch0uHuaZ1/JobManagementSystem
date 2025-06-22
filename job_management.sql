-- 创建数据库
CREATE DATABASE IF NOT EXISTS job_management;
USE job_management;

-- 职业分类表
CREATE TABLE JobCategory (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL
);

-- 职业信息表
CREATE TABLE Job (
    job_id INT PRIMARY KEY AUTO_INCREMENT,
    category_id INT,
    employer VARCHAR(100) NOT NULL,
    required_count INT NOT NULL,
    hired_count INT DEFAULT 0,
    notes TEXT,
    FOREIGN KEY (category_id) REFERENCES JobCategory(category_id)
);

-- 求职者表
CREATE TABLE JobSeeker (
    seeker_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    gender ENUM('男', '女') NOT NULL,
    employed BOOLEAN DEFAULT false
);

-- 职业匹配表
CREATE TABLE JobMatching (
    match_id INT PRIMARY KEY AUTO_INCREMENT,
    job_id INT,
    seeker_id INT,
    FOREIGN KEY (job_id) REFERENCES Job(job_id),
    FOREIGN KEY (seeker_id) REFERENCES JobSeeker(seeker_id)
);

-- 费用表
CREATE TABLE Fee (
    fee_id INT PRIMARY KEY AUTO_INCREMENT,
    employer_name VARCHAR(100),
    employer_fee DECIMAL(10,2),
    seeker_name VARCHAR(100),
    seeker_fee DECIMAL(10,2)
);

-- 触发器：新增匹配时更新状态
DELIMITER $$
CREATE TRIGGER AfterInsertMatching
AFTER INSERT ON JobMatching
FOR EACH ROW
BEGIN
    -- 更新职业的已聘人数
    UPDATE Job
    SET hired_count = hired_count + 1
    WHERE job_id = NEW.job_id;

    -- 更新求职者的聘用状态
    UPDATE JobSeeker
    SET employed = true
    WHERE seeker_id = NEW.seeker_id;
END$$
DELIMITER ;

-- 触发器：删除匹配时更新状态
DELIMITER $$
CREATE TRIGGER AfterDeleteMatching
AFTER DELETE ON JobMatching
FOR EACH ROW
BEGIN
    -- 更新职业的已聘人数
    UPDATE Job
    SET hired_count = hired_count - 1
    WHERE job_id = OLD.job_id;

    -- 更新求职者的聘用状态
    UPDATE JobSeeker
    SET employed = false
    WHERE seeker_id = OLD.seeker_id;
END$$
DELIMITER ;

-- 存储过程：查询各种职业的需求数和已聘用数
DELIMITER $$
CREATE PROCEDURE GetJobStats()
BEGIN
    SELECT
        j.employer,
        j.required_count,
        j.hired_count,
        (j.required_count - j.hired_count) AS vacancy
    FROM Job j
    ORDER BY j.required_count DESC;
END$$
DELIMITER ;

-- 添加外键约束
ALTER TABLE JobMatching
ADD CONSTRAINT fk_job
FOREIGN KEY (job_id) REFERENCES Job(job_id)
ON DELETE CASCADE;

ALTER TABLE JobMatching
ADD CONSTRAINT fk_seeker
FOREIGN KEY (seeker_id) REFERENCES JobSeeker(seeker_id)
ON DELETE CASCADE;

-- 插入示例数据
INSERT INTO JobCategory (category_name) VALUES
('信息技术'),
('金融'),
('教育'),
('医疗'),
('建筑');

INSERT INTO Job (category_id, employer, required_count, notes) VALUES
(1, 'ABC科技公司', 5, '需要Java开发经验'),
(2, 'XYZ银行', 3, '金融相关专业优先'),
(3, '希望小学', 2, '需要有教师资格证'),
(4, '仁爱医院', 4, '医学相关专业'),
(5, '城市建设集团', 3, '有项目管理经验优先');

INSERT INTO JobSeeker (name, gender) VALUES
('张三', '男'),
('李四', '女'),
('王五', '男'),
('赵六', '女'),
('钱七', '男');

INSERT INTO Fee (employer_name, employer_fee, seeker_name, seeker_fee) VALUES
('ABC科技公司', 5000.00, '张三', 200.00),
('XYZ银行', 4500.00, '李四', 200.00),
('希望小学', 3000.00, '王五', 150.00);
