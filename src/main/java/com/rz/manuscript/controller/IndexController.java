package com.rz.manuscript.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rz.manuscript.client.RepeatRateClient;
import com.rz.manuscript.common.CacheManager;
import com.rz.manuscript.common.Captcha;
import com.rz.manuscript.common.CurrentLoginUserManager;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.entity.Customer;
import com.rz.manuscript.entity.Manuscript;
import com.rz.manuscript.entity.Supplier;
import com.rz.manuscript.entity.User;
import com.rz.manuscript.pojo.entity.CacheEntity;
import com.rz.manuscript.pojo.vo.CalcRateRequest;
import com.rz.manuscript.pojo.vo.CaptchaVo;
import com.rz.manuscript.pojo.vo.LoginVo;
import com.rz.manuscript.service.ICustomerService;
import com.rz.manuscript.service.IManuscriptService;
import com.rz.manuscript.service.ISupplierService;
import com.rz.manuscript.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Base64;

@RestController
@RequestMapping("/index")
@Api(tags = "登录页")
@Slf4j
public class IndexController {

    @Resource
    private IUserService iUserService;

    @Resource
    private ICustomerService iCustomerService;

    @Resource
    private ISupplierService iSupplierService;

    @Resource
    private IManuscriptService iManuscriptService;

    @Resource
    private RepeatRateClient repeatRateClient;

    @Autowired
    private HttpServletRequest request;

    //调取获得验证码
    //请求的时候添加一个时间戳
    //http://localhost:8080/captcha?date=1121212
    @GetMapping(value = "/captcha")
    @ApiOperation(value = "验证码")
    public ResultEntity<CaptchaVo> captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        response.setDateHeader("Expires", 0);
//        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
//        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
//        response.setHeader("Pragma", "no-cache");
//        response.setContentType("image/jpeg");

        //OutputStream os = response.getOutputStream();
        //返回验证码和图片的map
        CaptchaVo res = new CaptchaVo();
        Map<String, Object> map = Captcha.getImageCode(86, 37);
        String simpleCaptcha = "simpleCaptcha";

//        request.getSession().setAttribute(simpleCaptcha, map.get("strEnsure").toString().toLowerCase());
//        request.getSession().setAttribute("codeTime", System.currentTimeMillis());
        String cacheKey = UUID.randomUUID().toString();
        String cacheValue = map.get("strEnsure").toString().toLowerCase();
        CacheEntity cacheEntity = new CacheEntity(cacheKey, cacheValue, 60 * 1000, false);
        CacheManager.putCache(cacheKey, cacheEntity);
        try {
            BufferedImage image = (BufferedImage) map.get("image");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
            ImageIO.write(image, "png", baos);//写入流中
            byte[] bytes = baos.toByteArray();//转换成字节

            String png_base64 = Base64.getEncoder().encodeToString(bytes);//转换成base64串
            png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
            res.setCaptchaData("data:image/png;base64," + png_base64);
            res.setCaptchaAccessToken(cacheKey);
            return new ResultEntity<CaptchaVo>(res);
        } catch (IOException e) {
            return new ResultEntity<CaptchaVo>(-1, "验证码获取失败");//BaseResult.error();
        }
    }

    /**
     *
     */
    @PostMapping(value = "/login")
    @ApiOperation(value = "登录")
    public ResultEntity<String> login(@RequestBody LoginVo data) throws Exception {

        // 获得验证码对象
        CacheEntity cko = CacheManager.getCacheInfo(data.getCaptchaToken());
        if (cko == null) {
            return new ResultEntity(-1, "验证码已失效，请刷新验证码！");
        }
        String captcha = "";
        try {
            captcha = (String) cko.getValue();
        } catch (Exception e) {
            return new ResultEntity(-1, "验证码已失效，请刷新验证码！");
        }
        String checkCode = data.getCheckCode().toLowerCase();
        if (StringUtils.isEmpty(checkCode) || captcha == null || !(checkCode.equalsIgnoreCase(captcha))) {
            return new ResultEntity(-1, "验证码错误，请重新输入！");

        } else {
            String userName = data.getUserName();
            String password = data.getPassword();
            String accessToken = UUID.randomUUID().toString();
            if (data.getLoginType().equals(2))//供应商
            {
                LambdaQueryWrapper<Supplier> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userLambdaQueryWrapper.eq(Supplier::getName, userName);
                userLambdaQueryWrapper.eq(Supplier::getPassword, password);
                Supplier c = iSupplierService.getOne(userLambdaQueryWrapper);
                if (c == null || c.getId() < 1)
                    return new ResultEntity<>(-1, "用户名密码错误");
                User one = new User();

                BeanUtil.copyProperties(c, one);
                one.setUserType(2);
                // 在这里可以处理自己需要的事务，比如验证登陆等
                // request.getSession().setAttribute("userId", one.getId());
                //  CurrentLoginUserManager.currentLoginUser.put(one.getId(), one);
                CacheManager.clearOnly(data.getCaptchaToken());

                CacheManager.putCache(accessToken, new CacheEntity(accessToken, one, 0, false));
                return new ResultEntity(200, accessToken, "验证通过！");
            } else if (data.getLoginType().equals(3))//客户
            {
                LambdaQueryWrapper<Customer> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userLambdaQueryWrapper.eq(Customer::getName, userName);
                userLambdaQueryWrapper.eq(Customer::getPassword, password);
                Customer c = iCustomerService.getOne(userLambdaQueryWrapper);
                if (c == null || c.getId() < 1)
                    return new ResultEntity<>(-1, "用户名密码错误");
                User one = new User();

                BeanUtil.copyProperties(c, one);
                one.setUserType(3);
                // 在这里可以处理自己需要的事务，比如验证登陆等
                // request.getSession().setAttribute("userId", one.getId());
                //  CurrentLoginUserManager.currentLoginUser.put(one.getId(), one);
                CacheManager.clearOnly(data.getCaptchaToken());

                CacheManager.putCache(accessToken, new CacheEntity(accessToken, one, 0, false));
                return new ResultEntity(200, accessToken, "验证通过！");
            } else {

                LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userLambdaQueryWrapper.eq(User::getName, userName);
                userLambdaQueryWrapper.eq(User::getPassword, password);
                User one = iUserService.getOne(userLambdaQueryWrapper);
                if (one == null || one.getId() < 1)
                    return new ResultEntity<>(-1, "用户名密码错误");
                // 在这里可以处理自己需要的事务，比如验证登陆等
                // request.getSession().setAttribute("userId", one.getId());
//                CurrentLoginUserManager.currentLoginUser.put(one.getId(), one);
                one.setUserType(1);
                CacheManager.clearOnly(data.getCaptchaToken());

                CacheManager.putCache(accessToken, new CacheEntity(accessToken, one, 0, false));
            }

            return new ResultEntity(200, accessToken, "验证通过！");

        }

    }

    @GetMapping(value = "/loginOut")
    public ResultEntity<Boolean> loginOut() throws Exception {

        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return new ResultEntity(true);
        }
        CacheManager.clearOnly(accessToken);
        // 在这里可以处理自己需要的事务，比如验证登陆等
        // CurrentLoginUserManager.currentLoginUser.remove(((User) cacheInfo.getValue()).getId());
        return new ResultEntity(true);
    }

    @GetMapping(value = "/reCalcRate")
    public ResultEntity<Boolean> reCalcRate() throws Exception {

        reCalcRateAll();
//        String accessToken = request.getHeader("accessToken");
//        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
//        if (cacheInfo == null) {
//            return new ResultEntity(true);
//        }
//        CacheManager.clearOnly(accessToken);

        // 在这里可以处理自己需要的事务，比如验证登陆等
        // CurrentLoginUserManager.currentLoginUser.remove(((User) cacheInfo.getValue()).getId());
        return new ResultEntity(true);
    }

    // @Async("CalcRateControllerThreadExecutor")
    public void reCalcRateAll() {
        int pageIndex = 1;
        Page<Manuscript> s = new Page<>(pageIndex, 10L);
        Page<Manuscript> page1 = iManuscriptService.page(s);

        for (Manuscript item : page1.getRecords()) {
            CalcRateRequest request = new CalcRateRequest();
            request.setContext(item.getContent());
            request.setManuscriptId(item.getId());
            repeatRateClient.calcRate(request);
        }
        while (page1.hasNext()) {
            pageIndex++;
            s = new Page<>(pageIndex, 10L);
            page1 = iManuscriptService.page(s);
            for (Manuscript item : page1.getRecords()) {
                CalcRateRequest request = new CalcRateRequest();
                request.setContext(item.getContent());
                request.setManuscriptId(item.getId());
                repeatRateClient.calcRate(request);
            }
        }

    }
}
