package com.zimujiang.tinyclip;

import com.zimujiang.tinyclip.config.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) throws InterruptedException {
        AppConfig.init();
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    public void start(Stage stage) throws Exception {
        // 加载并创建主场景
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
        // root.getStylesheets().add(ResourceUtil.getResource("css/Main.css").toExternalForm());
        Scene scene = new Scene(root, AppConfig.stageWidth, AppConfig.stageHeight);
        // 设置窗口信息
        stage.setTitle(AppConfig.title);
        stage.setResizable(AppConfig.stageResizable);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(AppConfig.icon)));
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

}
