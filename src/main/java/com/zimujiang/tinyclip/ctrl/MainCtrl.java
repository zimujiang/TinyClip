package com.zimujiang.tinyclip.ctrl;


import com.google.common.collect.Lists;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import com.zimujiang.tinyclip.ffmpeg.runner.BaseCommandOption;
import com.zimujiang.tinyclip.ffmpeg.runner.FFmpegCommandRunner;
import com.zimujiang.tinyclip.ffmpeg.utils.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 主界面控制器
 *
 * @author westinyang
 * @date 2020/3/18 18:13
 */
@Slf4j
public class MainCtrl implements Initializable {

    // 主容器
    public Pane rootPane;
    public Hyperlink hyperlink;

    // 提取Tab
    public Button tqSelectVideoFileBtn;
    public Button tqSelectOutPutDirBtn;
    public Button tqStartBtn;
    public ComboBox tqRateCb;
    public ComboBox tqFormatCb;
    public TextField tqVideoTxt;
    public TextField tqOutPutTxt;
    public ImageView tqimageView;

    // 压制Tab
    public Button yzSelectVideoFileBtn;
    public Button yzSelectSrtFileBtn;
    public Button yzSelectOutPutDirBtn;
    public Button yzStartBtn;
    public TextField yzVideoTxt;
    public TextField yzStrTxt;
    public TextField yzOutPutTxt;
    public ImageView yzimageView;


    public void initialize(URL location, ResourceBundle resources) {
        //log.info("initialize: {}", location.getPath());
        tqRateCb.getItems().addAll("8000","12050","16000","22050","24000","32000","44100","48000");
        tqRateCb.getSelectionModel().select(2);
        tqFormatCb.getItems().addAll("mp3","wav");
        tqFormatCb.getSelectionModel().select(0);
        tqimageView.setVisible(false);
        yzimageView.setVisible(false);
    }

    /**
     * 选择提取开始按钮点击事件
     * @param actionEvent
     */
    public void tqStartBtnClick(ActionEvent actionEvent) {
        String input = tqVideoTxt.getText();
        String outputDir = tqOutPutTxt.getText();
        String rate = tqRateCb.getValue().toString();
        String format = tqFormatCb.getValue().toString().toLowerCase();
        File f = new File(input);
        File d = new File(outputDir);
        String videoName = "TinyClip_"+FileUtils.getFileName(f)+"_"+Long.toString(System.currentTimeMillis()/1000L);
        File output = new File(outputDir + File.separator + videoName +"."+ format);
        if(input!=null && outputDir!=null && d.exists() && f.exists()) {
            List<String> commands = Lists.newArrayList(BaseCommandOption.getFFmpegBinary());
            commands.add("-i");
            commands.add(input);
            commands.add("-ar");
            commands.add(rate);
            commands.add(output.getAbsolutePath());
            Task<String> task = new Task<String>() {
                @Override public String call() {
                    return FFmpegCommandRunner.runProcess(commands);
                }
            };
            task.setOnRunning((e) -> {
                tqStartBtn.setText("正在处理");
                tqStartBtn.setDisable(true);
                tqimageView.setVisible(true);
            });
            task.setOnSucceeded((e) -> {
                tqStartBtn.setText("开始提取");
                tqStartBtn.setDisable(false);
                tqimageView.setVisible(false);
                try{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("提取完成,"+task.get());
                    alert.show();
                }catch (Exception ex){
                    log.error("获取ffmpeg返回失败");
                }
            });
            task.setOnFailed((e) -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("处理失败");
                alert.show();
            });
            new Thread(task).start();
        }else{
            Platform.setImplicitExit(false);
            Platform.runLater(() -> {
                tqStartBtn.setText("开始提取");
                tqStartBtn.setDisable(false);
                tqimageView.setVisible(false);
            });
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("请确保输入视频和输出目录有效");
            alert.show();
        }
    }

    /**
     * 选择压制开始按钮点击事件
     * @param actionEvent
     */
    public void yzStartBtnClick(ActionEvent actionEvent) {
        String inputvideo = yzVideoTxt.getText();
        String inputsrt = yzStrTxt.getText();
        String outputDir = yzOutPutTxt.getText();
        File video = new File(inputvideo);
        File srt = new File(inputvideo);
        File d = new File(outputDir);
        String videoName = "TinyClip_"+FileUtils.getFileName(video)+"_"+Long.toString(System.currentTimeMillis()/1000L);
        File output = new File(outputDir + File.separator + videoName +".mp4");
        if(inputvideo!=null && inputsrt!=null && outputDir!=null && srt.exists() && d.exists() && video.exists()) {
            List<String> commands = Lists.newArrayList(BaseCommandOption.getFFmpegBinary());
            commands.add("-i");
            commands.add(inputvideo);
            commands.add("-vf");
            commands.add("subtitles="+inputsrt);
            commands.add(output.getAbsolutePath());
            Task<String> yztask = new Task<String>() {
                @Override public String call() {
                    return FFmpegCommandRunner.runProcess(commands);
                }
            };
            yztask.setOnRunning((e) -> {
                yzStartBtn.setText("正在处理");
                yzStartBtn.setDisable(true);
                yzimageView.setVisible(true);
            });
            yztask.setOnSucceeded((e) -> {
                yzStartBtn.setText("开始提取");
                yzStartBtn.setDisable(false);
                yzimageView.setVisible(false);
                try{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("压制完成,"+yztask.get());
                    alert.show();
                }catch (Exception ex){
                    log.error("获取ffmpeg返回失败");
                }
            });
            yztask.setOnFailed((e) -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("压制失败");
                alert.show();
            });
            new Thread(yztask).start();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("请确保视频、字幕、输出目录有效");
            alert.show();
        }
    }



    /**
     * 选择输出目录按钮单击事件
     * @param actionEvent
     */
    public void tqSelectOutPutDirBtnClick(ActionEvent actionEvent) {
        Window window = rootPane.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择输出目录");
        File directory = directoryChooser.showDialog(window);
        if (directory != null) {
            tqOutPutTxt.setText(directory.getAbsolutePath());
        }
    }

    /**
     * 选择输出目录按钮单击事件
     * @param actionEvent
     */
    public void yzSelectOutPutDirBtnClick(ActionEvent actionEvent) {
        Window window = rootPane.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择输出目录");
        File directory = directoryChooser.showDialog(window);
        if (directory != null) {
            yzOutPutTxt.setText(directory.getAbsolutePath());
        }
    }

    /**
     * 选择提取视频路径按钮单击事件
     * @param actionEvent
     */
    public void tqSelectVideoFileBtnClick(ActionEvent actionEvent) {
        Window window = rootPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        // 文件类型过滤器
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("视频文件 (*.mp4)", "*.mp4", "*.MP4");
        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showOpenDialog(window);
        String fileAbsolutePath = file == null ? "" : file.getAbsolutePath();

        if (file != null) {
            tqVideoTxt.setText(fileAbsolutePath);
            tqOutPutTxt.setText(file.getParentFile().getAbsolutePath());
        }
    }

    /**
     * 选择视频路径按钮单击事件
     * @param actionEvent
     */
    public void yzSelectVideoFileBtnClick(ActionEvent actionEvent) {
        Window window = rootPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        // 文件类型过滤器
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("视频文件 (*.mp4)", "*.mp4", "*.MP4");
        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showOpenDialog(window);
        String fileAbsolutePath = file == null ? "" : file.getAbsolutePath();

        if (file != null) {
            yzVideoTxt.setText(fileAbsolutePath);
            yzOutPutTxt.setText(file.getParentFile().getAbsolutePath());
        }
    }

    /**
     * 选择字幕路径按钮单击事件
     * @param actionEvent
     */
    public void yzSelectSrtFileBtnClick(ActionEvent actionEvent) {
        Window window = rootPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        // 文件类型过滤器
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("字幕文件 (*.srt)", "*.srt", "*.SRT");
        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showOpenDialog(window);
        String fileAbsolutePath = file == null ? "" : file.getAbsolutePath();

        if (file != null) {
            yzStrTxt.setText(fileAbsolutePath);
        }
    }

    /**
     * 超链接单击事件
     * @param actionEvent
     */
    public void hyperlinkClick(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://www.zimujiang.com"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
