-- 秒杀执行的存储过程
-- 在商业数据库应用中，例如金融、企业、政府等等，存储过程的使用非常广泛
-- 而在互联网行业，存储过程很少使用，一个重要的原因是MySQL的广泛使用，
-- 而MySQL的存储过程的功能很弱（跟商业数据库相比）；另外也跟互联网行业变化快有一定的关系。
DROP PROCEDURE execute_seckilled 
DELIMITER $$

-- 将命令行结束符  ; 转换为 $$
-- 定义存储过程
-- 参数 ：in输入参数，out输出参数
-- row_count():上一条修改的sql类型（delete、insert、update）所影响的行数
-- row_count: 0 未修改的数据，>0修改的行数，<0 sql语句错误或者没有执行sql语句
CREATE PROCEDURE `seckill`.`execute_seckilled` (
  IN v_seckill_id BIGINT,
  IN v_phone BIGINT,
  IN v_kill_time TIMESTAMP,
  OUT r_result INT
) -- 创建储存过程
BEGIN
  -- 开始执行
  DECLARE insert_count INT DEFAULT 0 ;
  -- 定义变量
  START TRANSACTION ;
  -- 开启事物管理
  INSERT IGNORE INTO success_killed (
    seckill_id,
    user_phone,
    state,
    create_time
  ) 
  VALUES
    (
      v_seckill_id,
      v_phone,
      0,
      v_kill_time
    ) ;
  -- 执行insert语句
  SELECT 
    ROW_COUNT() INTO insert_count ;
  -- 返回影响行数
  IF (insert_count = 0) 
  THEN ROLLBACK ;
  -- 事务回滚
  SET r_result = - 1 ; 
  -- 重复秒杀
  ELSEIF (insert_count < 0) 
  THEN ROLLBACK ;
  -- 事务回滚
  SET R_RESULT = - 2 ;
  -- 系统内部错误
  ELSE 
  UPDATE 
    seckill 
  SET
    number = number - 1 
  WHERE seckill_id = v_seckill_id 
    AND end_time > v_kill_time 
    AND start_time < v_kill_time 
    AND number > 0 ;
  -- 执行update语句
  SELECT 
    ROW_COUNT() INTO insert_count ;
  -- 返回影响行数
  IF (insert_count = 0) 
  THEN ROLLBACK ;
  -- 事务回滚
  SET r_result = 0 ;
  -- 秒杀结束
  ELSEIF (insert_count < 0) 
  THEN ROLLBACK ;
  -- 事务回滚
  SET r_result = - 2 ;
  -- 系统内部问题
  ELSE COMMIT ;
  -- 提交,事务结束
  SET r_result = 1 ;
  -- 返回执行成功
  END IF ;
  -- 结束IF语句
  END IF ;
  -- 结束IF语句
END ;
-- 结束储存过程
$$
-- 结束sql
-- 存储过程定义结束

DELIMITER ;
-- 定义变量
SET @r_result = - 3 ;
CALL execute_seckilled (1002, 13243237331, NOW(), @r_result) ;
-- 获取结果
SELECT 
  @r_result ;
-- 存储过程
-- 1:存储过程优化：事务行级锁持有的时间
-- 2:不要过度依赖存储过程
-- 3:简单的逻辑可以应用存储过程
-- 4:QPS:一个秒杀单6000/qp
