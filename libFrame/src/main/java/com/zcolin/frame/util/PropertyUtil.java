/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties配置文件操作类
 */
public class PropertyUtil {
    protected String      fileName; // 文件名称
    protected String      filePath; // 文件路径
    protected InputStream inputStream;//读取配置文件的输入流

    public static InputStreamProperties instance(InputStream inputStream) {
        return new PropertyUtil(inputStream).new InputStreamProperties();
    }

    public static FileProperties instance(String filePath, String fileName) {
        return new PropertyUtil(filePath, fileName).new FileProperties();
    }

    private PropertyUtil(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private PropertyUtil(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    /**
     * 文件类型的，可以读写
     */
    public class FileProperties {
        /**
         * 初始化加载
         */
        public LoadProperties load() {
            LoadProperties loadProp = new LoadProperties();
            loadProp.loadInFileProperties();
            return loadProp;
        }

        /**
         * 存入数据是必须调用此方法获取存入实例
         */
        public EditProperties edit() {
            EditProperties editProp = new EditProperties();
            return editProp;
        }
    }

    /**
     * 输入流的，只能读取。如assets文件
     */
    public class InputStreamProperties {

        /**
         * 初始化加载
         */
        public LoadProperties load() {
            LoadProperties loadProp = new LoadProperties();
            loadProp.loadInInputStreamProperties();
            return loadProp;
        }
    }

    /**
     * 加载配置文件内部类
     */
    public class LoadProperties {

        private Properties props = new Properties();

        private LoadProperties() {
        }

        /**
         * 获取String
         *
         * @param key      Key
         * @param defValue Key不存在的默认值
         */
        public String getString(String key, String defValue) {
            if (key == null) {
                return defValue;
            }
            String str = props.getProperty(key);
            if (str == null) {
                str = defValue;
            }
            return str;
        }

        /**
         * 获取Boolean
         *
         * @param key      Key
         * @param defValue Key不存在的默认值
         */
        public boolean getBoolean(String key, boolean defValue) {
            if (key == null) {
                return defValue;
            }
            String str = props.getProperty(key);
            boolean b = defValue;
            if (str != null) {
                try {
                    b = Boolean.parseBoolean(str);
                } catch (Exception e) {
                    LogUtil.d("PropertyMgr--getBoolean", LogUtil.ExceptionToString(e));
                }
            }
            return b;
        }

        /**
         * 获取Int
         *
         * @param key      Key
         * @param defValue Key不存在的默认值
         */
        public int getInt(String key, int defValue) {
            if (key == null) {
                return defValue;
            }
            String str = props.getProperty(key);
            int i = defValue;
            if (StringUtil.isNotEmpty(str)) {
                try {
                    i = Integer.parseInt(str);
                } catch (Exception e) {
                    LogUtil.d("PropertyMgr--getInt", LogUtil.ExceptionToString(e));
                }
            }
            return i;
        }

        /**
         * 加载文件形式配置文件
         */
        private Properties loadInFileProperties() {
            File file = new File(filePath + "/" + fileName);
            checkFileExist(file);
            FileReader input = null;
            try {
                input = new FileReader(file);
                props.load(input);
            } catch (FileNotFoundException e) {
                LogUtil.d("PropertyMgr--loadInProperties", LogUtil.ExceptionToString(e));
            } catch (IOException e1) {
                LogUtil.d("PropertyMgr--loadInProperties", LogUtil.ExceptionToString(e1));
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        LogUtil.d("PropertyMgr--loadInProperties", LogUtil.ExceptionToString(e));
                    }
                }
            }
            return props;
        }

        /*
         * 加载输入流形式配置文件
         */
        private Properties loadInInputStreamProperties() {
            try {
                props.load(inputStream);
            } catch (FileNotFoundException e) {
                LogUtil.d("PropertyMgr--loadInProperties", LogUtil.ExceptionToString(e));
            } catch (IOException e1) {
                LogUtil.d("PropertyMgr--loadInProperties", LogUtil.ExceptionToString(e1));
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        LogUtil.d("PropertyMgr--loadInProperties", LogUtil.ExceptionToString(e));
                    }
                }
            }
            return props;
        }
    }

    /**
     * 编辑配置文件内部类
     */
    public class EditProperties {

        private Properties props;

        private EditProperties() {
            LoadProperties loadProp = new LoadProperties();
            props = loadProp.loadInFileProperties();
        }

        /**
         * 存入配置值
         *
         * @param key   Key
         * @param value value
         */
        public EditProperties putString(String key, String value) {
            if (key != null && value != null) {
                props.setProperty(key, value);
            }
            return this;
        }

        /**
         * 存入配置值
         *
         * @param key   Key
         * @param value value
         */
        public EditProperties putInt(String key, int value) {
            if (key != null) {
                putString(key, String.valueOf(value));
            }
            return this;
        }

        /**
         * 存入配置值
         *
         * @param key   Key
         * @param value value
         */
        public EditProperties putBoolean(String key, boolean value) {
            if (key != null) {
                putString(key, String.valueOf(value));
            }
            return this;
        }

        /**
         * 移除配置
         *
         * @param key Key
         */
        public EditProperties remove(String key) {
            if (key != null) {
                props.remove(key);
            }
            return this;
        }

        /**
         * 提交更改
         */
        public void commit() {
            loadOutFileProperties(null);
        }

        /**
         * 注释
         *
         * @param comm 注释
         */
        public void commit(String comm) {
            loadOutFileProperties(comm);
        }

        /*
         * 加载 配置文件
         * 
         * @param comm		注释
         */
        private void loadOutFileProperties(String comm) {
            FileWriter output = null;
            try {
                File file = new File(filePath + "/" + fileName);
                checkFileExist(file);
                output = new FileWriter(file);
                props.store(output, comm);
            } catch (FileNotFoundException e) {
                LogUtil.d("PropertyMgr--loadOutProperties", LogUtil.ExceptionToString(e));
            } catch (IOException e1) {
                LogUtil.d("PropertyMgr--loadOutProperties", LogUtil.ExceptionToString(e1));
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        LogUtil.d("PropertyMgr--loadOutProperties", LogUtil.ExceptionToString(e));
                    }
                }
            }
        }

    }

    /**
     * 检查文件是否存在，不存在则创建
     */
    private void checkFileExist(File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
