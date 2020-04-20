#### 系统技术介绍

##### 一、系统技术架构图
![image](https://dancing-robot.oss-cn-shenzhen.aliyuncs.com/%E4%B8%96%E5%B1%B9%E5%95%86%E5%9F%8E%E6%8A%80%E6%9C%AF%E6%9E%B6%E6%9E%84%E5%9B%BE.jpg?Expires=1558488160&OSSAccessKeyId=TMP.AgG4yGQCnOYmKa0ynqQ2FQ18ecr3kuap7X5dRYa0vgHbebZyiskMX8gTD4ZNADAtAhQjLFNJMYseRNXhEMrU9tFJDZ198wIVAMqMy3_rZ80sxReFjHZWFx82WeH7&Signature=mBaQJQa%2FTw0kIS7XzFywauYMwdA%3D)

##### 二、技术栈

技术 | 简介  | 官网
---|---|---
SpringBoot | 基础构建框架，用于快速整合各资源 |https://spring.io/projects/spring-boot
Spring Framework| 底层容器 | https://spring.io/projects/spring-framework
Mybatis| 数据持久层框架 | http://www.mybatis.org/
Spring Cloud| 微服务框架 |https://springcloud.cc/
Eureka| 注册中心，用于服务注册与发现 | https://springcloud.cc/
Zuul| 服务网关 | https://springcloud.cc/
Feign| 服务调用 | https://springcloud.cc/
Ribbon| 服务负载均衡 | https://springcloud.cc/
Hystrix| 服务容错 | https://springcloud.cc/
Turbine| 服务聚合监控 | https://springcloud.cc/
Actuator|应用监控|https://springcloud.cc/
Apollo| 配置中心 | https://github.com/ctripcorp/apollo
Lombok| 一个通过注解自动生成get/set方法的类库 | https://www.projectlombok.org/
通用Mapper|Mybatis通用方法框架 | https://github.com/abel533/Mapper
PageHelper| 数据分页框架 | https://pagehelper.github.io/
Hibernate Validator| 参数校验框架 | 
Maven| 项目构建管理 | http://maven.apache.org/


##### 三、服务模块划分
- shiyi-register：注册中心
- shiyi-gateway：服务网关
- shiyi-hystrix-turbine：服务聚合监控
- shiyi-common-tool：系统依赖的工具包
- shiyi-common-core：系统依赖的数据库操作依赖包
- shiyi-code-generator：Mybatis代码自动生成工具
- shiyi-admin-web：管理后台
- shiyi-user：用户服务
- shiyi-account：账户服务
- shiyi-product：商品服务
- shiyi-trade：交易服务服务
- shiyi-public：公共服务
- shiyi-quotation：行情服务
- shiyi-cash：出入金服务


##### 四、服务分包原则
###### 1.定义服务模块，命名规则：shiyi-xxxx
###### 2.定义服务对外feign接口（如果需要），命名规则：shiyi-xxxx-feign
- base：定义服务接口,该接口被Feign接口继承,被具体的服务接口实现,命名规则IXxxBase
- client包：定义Feign服务接口,该接口直接继承对应的Base接口,命名规则:XxxFeign
- hystrix：定义Feign接口的fallback实现,该类直接实现XxxFeign接口,命名规则:XxxHystrix
- bean：定义服务接口传输对象,dto为接口参数对象,命名规则:XxxDto,vo为接口返回对象,命名规则:XxxVo

###### 3.定义服务实现，命名规则：shiyi-xxxx-service
- common:公共包
- dao:数据库操作层mapper
- model:数据库实体对象
- service:数据服务层
- web:对外接口层,admin为B端后台接口,类命名规则为AdminXxxxController,接口URL前缀为/admin,
- api为C端接口,类命名规则为ApiXxxxController,需要登录鉴权的类命名规则为AuthApiXxxxController,接口URL前缀为/api
- XxxxServiceApplication:启动类

##### 五、常用组件用法介绍


- Mybatis单表数据查询

```
boolean save(T model);//持久化

void save(List<T> models);//批量持久化

boolean deleteById(Long id);//通过主鍵刪除

void deleteByIds(String ids);//批量刪除 eg：ids -> “1,2,3,4”

boolean update(T model);//更新

boolean updateByConditionSelective(T model, Condition condition);//根据条件更新

T findById(Integer id);//通过ID查找

T findById(Long id);//通过ID查找

T findBy(String fieldName, Object value); //通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束

List<T> findByIds(String ids);//通过多个ID查找//eg：ids -> “1,2,3,4”

List<T> findByCondition(Condition condition);//根据条件查找

List<T> findAll();//获取所有

int selectCountByCondition(Condition condition); //count

MyPageInfo<T> pageList(Condition condition, PageParam pageParam); //分页
    
```

- 分页查询

```
public MyPageInfo<UserPageVo> pageList(UserPageDto userPageDto) {
    Condition condition = new Condition(User.class);
    Example.Criteria criteria = condition.createCriteria();
    if (!StringUtils.isEmpty(userPageDto.getMobile())) {
        criteria.andEqualTo("mobile", userPageDto.getMobile());
    }
    if (!StringUtils.isEmpty(userPageDto.getUserNo())) {
        criteria.andEqualTo("userNo", userPageDto.getUserNo());
    }
    MyPageInfo<User> myPageInfo = pageList(condition, PageParam.buildWithDefaultSort(userPageDto.getCurrentPage(), userPageDto.getPageSize()));
    return PageUtil.transform(myPageInfo, UserPageVo.class);
}
```
- feign服务接口调用

```
// 获取账户余额信息
ApiResult<AccountInfoVo> apiResult = accountFeign.accountInfo(userNo);
if (apiResult.hasSuccess()) {
    // 业务逻辑处理
}
```
