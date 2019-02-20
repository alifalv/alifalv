package com.legal.app.service;

public class Constant {
    // 审核通过
    public static final int USER_IS_AUTHENTICATION_CHECK = 2;
    // 审核不通过
    public static final int USER_IS_AUTHENTICATION_UNCHECK = 3;
    // 启用
    public static final int USER_USER_STATE_ENABLE       = 1;
    // 禁用
    public static final int USER_USER_STATE_DISABLED     = 0;
    // 签约
    public static final int USER_IS_CONTRACT_CONTRACT    = 1;
    // 取消签约
    public static final int USER_IS_CONTRACT_UN_CONTRACT = 0;
    // 推荐
    public static final int USER_IS_PUSH_PUSHED          = 1;
    // 未推荐
    public static final int USER_IS_PUSH_UN_PUSH         = 0;

    // 未删除
    public static final int UN_DELETE                    = 0;
    // 已删除
    public static final int DELETED                      = 1;

    // 审核通过
    public static final int BUSINESS_CHECKED             = 2;
    // 审核不通过
    public static final int BUSINESS_CHECKED_UN          = 3;

    // 锁定
    public static final int LOCKED                       = 2;
    // 解锁
    public static final int UN_LOCKED                    = 1;

    // 禁用
    public static final int DISABLED                     = 1;
    // 启用
    public static final int ENABLE                       = 0;

    // banner 图资源类型
    public static final String RESOURCES_BANNER          = "RESOURCES_BANNER";

    // 未发送
    public static final int UN_SEND                      = 0;
    // 已发送
    public static final int SENDED                       = 1;
}