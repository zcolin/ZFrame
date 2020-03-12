/*
 * *********************************************************
 *   author   colin
 *   email    wanglin2046@126.com
 *   date     20-3-12 下午4:45
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
