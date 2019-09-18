package com.seckill.web.controller;

import com.alibaba.fastjson.JSON;
import com.seckill.web.dto.Exposer;
import com.seckill.web.dto.SecKillExecution;
import com.seckill.web.dto.SecKillResult;
import com.seckill.web.enums.SecKillStateEnum;
import com.seckill.web.exception.RepeatKillException;
import com.seckill.web.exception.SecKillCloseException;
import com.seckill.web.model.SecKill;
import com.seckill.web.service.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * 编写处理器调用service业务，来进行前后端交互
 * 异常处理，使用更规范的springMvc的全局异常处理，不需要每个方法都加try-catch
 *
 * @author hulupeng
 * @date 2019/9/16 11:22
 */
@Controller
@RequestMapping("/secKill") //总的拦截路径，在拦截路径前面加上就可以了
public class SecKillController {
    //定义日志文件(slf4j文件)
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //自动装载service
    @Autowired
    SecKillService secKillService;

    //秒杀商品列表页
    @RequestMapping(value = "/list", method = RequestMethod.GET) //只能是get请求
    public String list(Model model) throws Exception {
        //使用model来设置参数，返回视图
        List<SecKill> secKillList = secKillService.getSecKillList();
        model.addAttribute("secKillList", secKillList);
        return "list";       // 前缀和后缀在spring-web.xml中已配好，实际路径是“/WEB-INF/jsp/list.jsp”
    }

    //秒杀商品详细页
    @RequestMapping(value = "/{secKillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("secKillId") Long secKillId, Model model) throws Exception {
        if (secKillId == null) {
            return "redirect:/secKill/list";    //重定向到秒杀商品列表页面（地址URL改变）
        }
        SecKill secKill = secKillService.getSecKillById(secKillId);
        if (secKill == null) {
            return "forward:/secKill/list";     //转发到秒杀商品列表页面（地址URl不改变）
        }
        model.addAttribute("secKill", secKill);
        return "detail";
    }

    //ajax、json暴露秒杀接口的方法
    @RequestMapping(value = "/{secKillId}/exposer",
            method = RequestMethod.GET,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody   //以json格式输出
    public SecKillResult<Exposer> exposer(@PathVariable("secKillId") Long secKillId) {
        SecKillResult<Exposer> result;
        try {
            Exposer exposer = secKillService.exportSecKillUrl(secKillId);
            result = new SecKillResult<>(true, exposer);             // 暴露接口成功
        } catch (Exception e) {
            //logger.error(e.getMessage(),e);                                  // 打印日志
            result = new SecKillResult<>(false, e.getMessage());       // 暴露接口失败
        }
        return result;
    }

    //执行秒杀操作
    @RequestMapping(value = "/{secKillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SecKillResult<SecKillExecution> execute(@PathVariable("secKillId") Long secKillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "userPhone", required = false) Long userPhone) {
        if (userPhone == null) {
            //判断手机号码是否为空
            return new SecKillResult<>(false, "未注册");
        }
        try {
            //执行存储过程
            SecKillExecution execution = secKillService.executeSecKillProcedure(secKillId, userPhone, md5);
            return new SecKillResult<>(true, execution);
        } catch (RepeatKillException e) {
            SecKillExecution execution = new SecKillExecution(secKillId, SecKillStateEnum.REPEAT_KILL);
            return new SecKillResult<>(true, execution); //重复秒杀
        } catch (SecKillCloseException e) {
            SecKillExecution execution = new SecKillExecution(secKillId, SecKillStateEnum.END);
            return new SecKillResult<>(true, execution); //秒杀结束
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            SecKillExecution execution = new SecKillExecution(secKillId, SecKillStateEnum.INNER_ERROR);
            return new SecKillResult<>(true, execution); //系统异常
        }
    }

    //获取当前的时间
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SecKillResult<Long> time() {
        Date now = new Date();
        System.out.println("当前时间===" + new SecKillResult<>(true, now.getTime()).getData());
        System.out.println("时间获取的状态===" + new SecKillResult<>(true, now.getTime()).isSuccess());
        System.out.println("????======" + JSON.toJSONString(new SecKillResult<>(true, now.getTime())));
        return new SecKillResult<>(true, now.getTime());
    }
}
