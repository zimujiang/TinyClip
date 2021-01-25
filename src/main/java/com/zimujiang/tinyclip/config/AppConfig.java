package com.zimujiang.tinyclip.config;

import cn.hutool.setting.Setting;

/**
 * 应用配置
 *
 * @author westinyang
 * @date 2020/3/18 18:13
 */
public class AppConfig {
    // 应用标题
    public static String title = "TinyClip";
    // 应用图标
    public static String icon = "";
    // 窗口宽度
    public static int stageWidth = 640;
    // 窗口高度
    public static int stageHeight = 480;
    // 允许调整窗口尺寸
    public static boolean stageResizable = true;

    public static void init() {
        Setting setting = new Setting("app.properties");
        title = setting.get("title");
        icon = setting.get("icon");
        stageWidth = setting.getInt("stage.width");
        stageHeight = setting.getInt("stage.height");
        stageResizable = setting.getBool("stage.resizable");
    }

}
