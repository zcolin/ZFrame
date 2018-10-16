/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.permission;

/**
 * Enum class to handle the different states
 * of permissions since the PackageManager only
 * has a granted and denied state.
 */
enum Permissions {
    /**
     * 授权
     */
    GRANTED,
    /**
     * 拒绝
     */
    DENIED,
    /**
     * 权限信息未发现
     */
    NOT_FOUND
}
