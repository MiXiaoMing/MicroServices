# 综述

SpringBoot版本号：2.1.7.RELEASE

SpringCloud版本：Greenwich.SR1

#### Gateway 网关层
    作用：转发、校验、统一
模块名称 | 描述 |  访问端口  
-|-|-
zuul | 网关层 | 6666 |

##### 部署
- zuul：
    ./publish.sh Gateway/zuul zuul application


#### Interfaces 接口层
    作用：入参转化、出参拼装
模块名称 | 描述 |  访问端口  
-|-|-
jbh-if | just be here | 7101 |

##### 部署
- jbh-if：
    ./publish.sh Interfaces/JustBeHere-IF jbh-if application



#### Business 业务层
    作用：逻辑处理
模块名称 | 描述 |  访问端口  
-|-|-
jbh-biz | 用户 | 8101 |

##### 部署
- jbh-biz：
    ./publish.sh Business/JustBeHere-BIZ jbh-biz application



#### MiddlePlatform 中台
    作用：沉淀的处理逻辑
模块名称 | 描述 |  访问端口  
-|-|-
mp-notify | 通知服务(短信、邮件、推送) | 9001 |

##### 部署
- mp-notify：./publish.sh MiddlePlatform/MP-Notify mp-notify application



#### Data 数据层
    作用：数据封装
模块名称 | 描述 |  访问端口  
-|-|-
data-cache | 缓存数据 | 9100 |
data-user | 用户中心数据 | 9101 |
data-order | 订单服务数据 | 9102 |
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
    作用：app端性能数据采集
模块名称 | 描述 |  访问端口  
-|-|-
testdata | 评测数据 | 8900 |




# 详述

#### 响应提速
- ##### redis
    - 常驻redis（不常变动）：商品分类，服务分类
    - 定时长缓存：商品（20分钟），服务（40分钟）
- ##### rabbitmq
    - 异步：mq 异步缓存商品/服务
