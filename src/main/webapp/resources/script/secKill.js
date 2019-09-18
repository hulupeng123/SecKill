//存放主要交互逻辑的js代码
//javaScript模块化（package.类名.方法）
//相当于java的模块化，调用方式为secKill.detail.init()
var secKill={
    //封装秒杀相关ajax的url
    URL:{
        now:function () {
            return '/secKill/time/now';
        },
        exposer:function (secKillId) {
            return '/secKill/'+secKillId+'/exposer';
        },
        execution:function (secKillId,md5) {
            return '/secKill/'+secKillId+'/'+md5+'/execution';
        }
    },
    //验证手机号码
    validatePhone:function (userPhone) {
        if (userPhone && userPhone.length==11 && !isNaN(userPhone)){
            return true;//直接判断对象是否为空，如果是空的话是undefined 返回false，isNaN非数字返回true
        }else {
            return false;
        }
    },
//详情页面秒杀逻辑
    detail:{
        //详情页面初始化
        init:function (params) {
            console.log("获取手机号");
            //手机登录与验证进行交互
            //规划交互流程
            //从cookie中查手机号
            var userPhone=$.cookie('userPhone');
            //验证手机号
            if (!secKill.validatePhone(userPhone)) {
                console.log("未填写手机号");
                //绑定手机，控制输出
                var killPhoneModal=$('#killPhoneModal');
                    killPhoneModal.modal({
                        show:true,  //显示弹出层
                        backdrop:'static',//禁止位置关闭
                        keyboard:false  //关闭键盘事件
                    });
                    $('#killPhoneBtn').click(function () {
                        console.log("手机号按钮点击了。。。")
                        var inputPhone=$("#killPhoneKey").val();
                        console.log("input=="+inputPhone);
                        if (secKill.validatePhone(inputPhone)){
                            //电话写入cookie中（7天过期）
                            $.cookie('userPhone',inputPhone,{expires:7,path:'/secKill'});
                            //验证通过，刷新页面
                            window.location.reload();
                        }else{
                            //todo错误文案信息写入前端
                            $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                        }
                    });
            }else{
                console.log("从cookie中找到号码，开始计时");
                //已经登录，就开始计时
                var startTime=params['startTime'];
                var endTime=params['endTime'];
                var secKillId=params['secKillId'];
                console.log("开始时间======="+startTime);
                console.log("结束时间======="+endTime);
                $.get(secKill.URL.now(),{},function (result) {
                    console.log(result['success']);

                    if (result && result['success']) {
                        //var nowTime=secKill.convertTime(result['data']);
                        var nowTime=result['data'];
                        console.log("服务器当前时间:"+nowTime);
                        secKill.countDown(secKillId,nowTime,startTime,endTime);
                    }
                })
            }
        }
    },
    handlerSecKill: function (secKillId, node) {
    //获取秒杀地址，控制显示器，执行秒杀
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        console.log("开始秒杀获取地址");
        $.get(secKill.URL.exposer(secKillId),{},function(result){
            //在回调函数种执行交互流程
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //开启秒杀获取，秒杀地址
                    var md5=exposer['md5'];
                    var killUrl=secKill.URL.execution(secKillId,md5);
                    console.log("秒杀地址==="+killUrl);
                    //绑定一次秒杀时间
                    $('#killBtn').one('click',function () {
                        //执行秒杀请求
                        //1、先禁用按钮
                        $(this).addClass('disable');
                        //发送秒杀请求，执行秒杀
                        $.post(killUrl,{},function (result) {
                            if (result && result['success']){
                                var killResult=result['data'];//获取秒杀暴露地址的对象
                                var state=killResult['state'];//获取秒杀状态
                                var stateInfo=killResult['stateInfo'];//获取秒杀状态的信息
                                //显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                }else {
                    //未开启秒杀时间（浏览器计时偏差）
                    var start=exposer['start'];
                    var end=exposer['end'];
                    var now=exposer['now'];
                    secKill.countDown(secKillId,now,start,end);
                }
            }else {
                console.log('result====='+result);
            }
        })
    },
    countDown: function (secKillId, nowTime, startTime, endTime) {
        //debugger;
        console.log("秒杀商品的id："+secKillId+"服务器当前时间："+nowTime+"秒杀开始时间："+startTime+"秒杀结束时间："+endTime);
        //显示倒计时文本域
        var secKillBox=$('#secKill-box');
        if (nowTime>endTime){
            //秒杀结束
            secKillBox.html("秒杀结束");
        } else if (nowTime<startTime){
            //秒杀还未开始
            var killTime=new Date(startTime+1000);//todo 防止时间偏移量加1秒
            secKillBox.countdown(killTime,function (event) {
                //事件格式化
                var format=event.strftime("秒杀倒计时: %D天 %H时 %M分 %S秒");
                console.log(format);
                secKillBox.html(format);
            }).on('finish.countdown',function () {
                //时间完成后，获取秒杀地址，控制业务逻辑
                console.log("准备执行回调,获取秒杀地址,执行秒杀");
                console.log("倒计时结束");
                secKill.handlerSecKill(secKillId, secKillBox);
            })

        }else{
            //秒杀开始
            console.log("秒杀开始");
            secKill.handlerSecKill(secKillId,secKillBox);
        }
    }
};