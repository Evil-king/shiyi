/**
 * Created by 会跳舞的机器人 on 2019/5/10.
 * common:公共包
 * dao:数据库操作层mapper
 * model:数据库实体对象
 * service:数据服务层
 * web:对外接口层,admin为B端后台接口,类命名规则为AdminXxxxController,接口URL前缀为/admin,
 * api为C端接口,类命名规则为ApiXxxxController,需要登录鉴权的类命名规则为AuthApiXxxxController,无需登录鉴权的接口URL前缀为/api,需要登录鉴权接口URL前缀为/auth/api
 * shiyi为服务之间调用的接口,类命名规则为ShiyiXxxxController,接口URL前缀为/shiyi
 * XxxxServiceApplication:启动类
 */
package com.baibei.shiyi.publicc;