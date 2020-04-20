package com.baibei.shiyi.admin.modules.security.constants;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通用按钮权限表达式,用在@PreAuthorize(hasAnyRole(@authorityExpression.delete('prefix'),'ADMIN'))注解上,
 * 定义功能的通用权限表达式方便权限的统一性,其中ADMIN代表特定的权限,如果这是按钮的话,有菜单的话以菜单的前缀+功能标识
 */
@Component("authorityExpression")
public class AuthorityExpression {
//    {
//        label: 'QUERY' // 查询
//    },
//    {
//        label: 'NEW' // 新增
//    },
//    {
//        label: 'DELETE' // 删除
//    },
//    {
//        label: 'EXPORT' // 导出
//    },
//    {
//        label: 'SWITCH' // 开关
//    },
//    {
//        label: 'EDIT' // 编辑 查看
//    },
//    {
//        label: 'SIGN_OUT' // 签退
//    },
//    {
//        label: 'INIT_PAYMENT' // 发起出入金对账
//    },
//    {
//        label: 'GENERATE_BUSINESS' // 生成业务返还数据
//    },
//    {
//        label: 'GENERATE_LIQUIDATION_FILE' // 生成清算文件
//    },
//    {
//        label: 'INIT_LIQUIDATION' // 发起清算
//    },
//    {
//        label: 'LIQUIDATION_PROGRESS' // 查看清算进度
//    },
//    {
//        label: 'GET_STANDING_BOOK' // 获取台账
//    },
//    {
//        label: 'REJECT' // 驳回 拒绝
//    },
//    {
//        label: 'APPLY' // 通过 应用
//    }
//


    private static final String DELETE = "DELETE";

    private static final String EDIT = "EDIT";

    private static final String QUERY = "QUERY";

    private static final String NEW = "NEW";

    private static final String EXPORT = "EXPORT";

    private static final String IMPORT = "IMPORT";

    private static final String SWITCH = "SWITCH";

    private static final String SIGN_OUT = "SIGN_OUT";

    private static final String INIT_PAYMENT = "INIT_PAYMENT";

    private static final String GENERATE_BUSINESS = "GENERATE_BUSINESS";

    private static final String GENERATE_LIQUIDATION_FILE = "GENERATE_LIQUIDATION_FILE";

    private static final String INIT_LIQUIDATION = "INIT_LIQUIDATION";

    private static final String LIQUIDATION_PROGRESS = "LIQUIDATION_PROGRESS";

    private static final String GET_STANDING_BOOK = "GET_STANDING_BOOK";

    private static final String REJECT = "REJECT";

    private static final String APPLY = "APPLY";

    private static final String EXECUTE = "EXECUTE";

    private static final List<Map<String, Object>> authority = new ArrayList<>();


    /**
     * 分隔符
     */
    public static final String SPLIT_SYMBOL = "_";

    /**
     * 通用的删除权限表达式
     *
     * @param prefix
     * @return
     */
    public String delete(String prefix) {
        return prefix + SPLIT_SYMBOL + DELETE;
    }

    /**
     * 通用的修改权限表达式
     *
     * @param prefix
     * @return
     */
    public String edit(String prefix) {
        return prefix + SPLIT_SYMBOL + EDIT;
    }

    public String fundTransfer(String prefix) {
        return prefix + SPLIT_SYMBOL + "FUNDTRANSFER";
    }

    public String fundTransferSingle(String prefix) {
        return prefix + SPLIT_SYMBOL + "FUNDTRANSFER_SINGLE";
    }
    /**
     * 查询
     *
     * @param prefix
     * @return
     */
    public String find(String prefix) {
        return prefix;
    }

    /**
     * 保存
     *
     * @param prefix
     * @return
     */
    public String add(String prefix) {
        return prefix + SPLIT_SYMBOL + NEW;
    }

    /**
     * 文件导出
     *
     * @param prefix
     * @return
     */
    public String exportFile(String prefix) {
        return prefix + SPLIT_SYMBOL + EXPORT;
    }

    /**
     * 文件导入
     *
     * @param prefix
     * @return
     */
    public String importFile(String prefix) {
        return prefix + SPLIT_SYMBOL + IMPORT;
    }

    /**
     * 执行
     *
     * @param prefix
     * @return
     */
    public String execute(String prefix) {
        return prefix + SPLIT_SYMBOL + EXECUTE;
    }

    /**
     * 开关
     *
     * @param prefix
     * @return
     */
    public String switchButton(String prefix) {
        return prefix + SPLIT_SYMBOL + SWITCH;
    }

    /**
     * 签退
     *
     * @param prefix
     * @return
     */
    public String signOut(String prefix) {
        return prefix + SPLIT_SYMBOL + SIGN_OUT;
    }

    /**
     * 出入金对账
     *
     * @return
     */
    public String initPayment(String prefix) {
        return prefix + SPLIT_SYMBOL + INIT_PAYMENT;
    }

    /**
     * 生成业务返还数据
     */
    public String generateBusiness(String prefix) {
        return prefix + SPLIT_SYMBOL + GENERATE_BUSINESS;
    }

    /**
     * 生成清算文件
     *
     * @param prefix
     * @return
     */
    public String generateLiquidationFile(String prefix) {
        return prefix + SPLIT_SYMBOL + GENERATE_LIQUIDATION_FILE;
    }

    /**
     * 发起清算
     *
     * @return
     */
    public String initLiquidation(String prefix) {
        return prefix + SPLIT_SYMBOL + INIT_LIQUIDATION;
    }

    /**
     * 查看清算进度
     *
     * @param prefix
     * @return
     */
    public String liquidationProgress(String prefix) {
        return prefix + SPLIT_SYMBOL + LIQUIDATION_PROGRESS;
    }

    /**
     * 获取台账
     *
     * @param prefix
     * @return
     */
    public String getStandingBook(String prefix) {
        return prefix + SPLIT_SYMBOL + GET_STANDING_BOOK;
    }

    /**
     * 拒绝
     *
     * @param prefix
     * @return
     */
    public String reject(String prefix) {
        return prefix + SPLIT_SYMBOL + REJECT;
    }

    /**
     * 同意
     *
     * @param prefix
     * @return
     */
    public String apply(String prefix) {
        return prefix + SPLIT_SYMBOL + APPLY;
    }


    /**
     * 获取系统通用的权限
     *
     * @return
     */
    public List<Map<String, Object>> getSystemCommonAuthority() {
        return authority;
    }

}
