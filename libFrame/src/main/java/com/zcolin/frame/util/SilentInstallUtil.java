package com.zcolin.frame.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 静默安装工具类（需要设备拥有root权限）
 */
public class SilentInstallUtil {

    /**
     * @param apkPath 要安装的apk文件的路径
     *
     * @return 安装成功返回true，安装失败返回false
     */
    public static boolean install(String apkPath) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 切换到具有root权限的账号（前提是设备拥有root权限）
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(StandardCharsets.UTF_8));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            LogUtil.e("silentInstallMsg", "install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            LogUtil.e("silentInstallException", e.toString());
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                LogUtil.e("silentInstallException", e.toString());
            }
        }
        return result;
    }

    /**
     * 判断设备是否拥有root权限
     */
    public static boolean isRoot() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        //检查是否有su文件
        if (new File(binPath).exists() && isExecutable(binPath)) {
            return true;
        }
        return new File(xBinPath).exists() && isExecutable(xBinPath);
    }

    /**
     * 检查su文件是否可运行
     */
    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x') {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return false;
    }

}