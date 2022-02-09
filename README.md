# logistics
基于springboot的小区快速服务
由于配置文件涉及阿里云的安全信息，所以暂时下架Yml文件，如需联系我获取Yml文件，下方联系方式

# 开发套件
> 更多后期完成后补充 \
> 后端： springboot+Mybatis-plus+redis+docker  \
> 前端： vue 微信小程序


作者
>Myxiaowang \
> Sjd
> 

>联系方式：
>   QQ:1049835177

快速开始
> 1.部署数据库 \
> 2.安装redis  
> 3.装docker \
> 4.配置环境 

详细说明：

>注意需要配置oos以及阿里云支付的环境

> docker安装mysql 并且本地挂载卷：
> docker run --name mysqls -d -it -p 7896:3306 -e MYSQL_ROOT_PASSWORD=root -v /root/mysqldata:/var/lib/mysql  mysql:5.7 \
> 安全组开放7896端口

>docker 安装redis \
> docker run -it -p 6565:6379 --name redis redis:latest \
> 安全组开放端口6565

> docker安装rabbitmq \
> docker run -d --name rabbitmq3.7.7 -p 5672:5672 -p 15672:15672 -v `pwd`/data:/var/lib/rabbitmq --hostname myRabbit -e RABBITMQ_DEFAULT_VHOST=my_vhost  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin \
>> 引用链接 https://www.cnblogs.com/yufeng218/p/9452621.html \
> 开放安全组端口 5672 15672