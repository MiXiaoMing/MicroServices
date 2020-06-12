# 综述

SpringBoot版本号：2.1.7.RELEASE

SpringCloud版本：Greenwich.SR1

#### Interfaces 接口层
模块名称 | 描述 |  访问端口  
-|-|-
jbh-if | just be here | 7101 |

##### 部署
- jbh-if：
    ./publish.sh Interfaces/JustBeHere-IF jbh-if application



#### Business 业务层
模块名称 | 描述 |  访问端口  
-|-|-
jbh-biz-user | 用户 | 8101 |

##### 部署
- jbh-biz：
    ./publish.sh Business/JustBeHere-BIZ jbh-biz application



#### MiddlePlatform 中台
模块名称 | 描述 |  访问端口  
-|-|-
mp-sms | 短信服务 | 9001 |
mp-user | 统一用户服务 | 9002 |

##### 部署
- mp-sms：./publish.sh MiddlePlatform/MP-Sms mp-sms application
- mp-user：./publish.sh MiddlePlatform/MP-User mp-user application



#### Data 数据层
模块名称 | 描述 |  访问端口  
-|-|-
data-cache | 缓存数据 | 9100 |
data-user | 用户中心数据 | 9101 |
jbh-mysql | jbh 数据 | 9201 |

##### 部署
- data-cache：./publish.sh Data/Data-Cache data-cache application
- data-user： ./publish.sh Data/Data-User data-user application
- jbh-mysql： ./publish.sh Data/JustBeHere-Mysql jbh-mysql application

##### 部署问题
- mysql：需要将mysql设置到 servers_base网络
    - docker run --restart=always -p 3306:3306 --net servers_basic --name mysql -v /jbh/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root -d mysql:5.6
    - navicat连接报错：1045 - Access denied for user 'root'@'ip' (using password: YES)
        - GRANT ALL PRIVILEGES ON *.* TO root@"%" IDENTIFIED BY "root"; 
        - flush privileges; 





### 评测平台
模块名称 | 描述 |  访问端口  
-|-|-
testdata | 评测数据 | 8900 |