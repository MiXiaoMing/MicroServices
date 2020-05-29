# Data Layer  数据层


SpringBoot版本号：2.1.7.RELEASE

SpringCloud版本：Greenwich.SR1


模块名称 | 描述 |  访问端口  
-|-|-
data-cache | 缓存数据 | 9100 |
data-user | 用户中心数据 | 9101 |


#### 部署
- data-cache：./publish.sh Data/Data-Cache data-cache application
- data-user： ./publish.sh Data/Data-User data-user application



##### 部署问题
- mysql：需要将mysql设置到 servers_base网络
    - docker run --restart=always -p 3306:3306 --net servers_basic --name mysql -v /jbh/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root -d mysql:5.6
    - navicat连接报错：1045 - Access denied for user 'root'@'ip' (using password: YES)
        - GRANT ALL PRIVILEGES ON *.* TO root@"%" IDENTIFIED BY "root"; 
        - flush privileges; 
