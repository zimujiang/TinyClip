package com.zimujiang.tinyclip.ctrl;

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
import com.zimujiang.tinyclip.utils.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.bytedeco.javacpp.Loader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * 主界面控制器
 *
 * @author westinyang
 * @date 2020/3/18 18:13
 */
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


    public void initialize(URL location, ResourceBundle resources) {
        //log.info("initialize: {}", location.getPath());
        tqRateCb.getItems().addAll("8000","12050","16000","22050","24000","32000","44100","48000");
        tqRateCb.getSelectionModel().select(2);
        tqFormatCb.getItems().addAll("mp3","wav");
        tqFormatCb.getSelectionModel().select(0);
        tqimageView.setVisible(false);
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
        String videoName = "TinyClip_"+FileUtils.getFileName(f)+"_"+ System.currentTimeMillis() / 1000L;
        if(input!=null && outputDir!=null && d.exists() && f.exists()) {
            String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
            ProcessBuilder pb = new ProcessBuilder(ffmpeg,"-i",input,"-ar",rate,outputDir + File.separator + videoName +"."+ format);
            Task<String> task = new Task<String>() {
                @Override public String call() {
                    StopWatch sw = new StopWatch();
                    String r = "";
                    try {
                        sw.start();
                        pb.start().waitFor();
                        sw.stop();
                        r = "提取完成，耗时："+sw.toString();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        r = "进程被中断";
                    } catch (IOException e) {
                        e.printStackTrace();
                        r = "IO异常";
                    } finally {
                        if(!sw.isStopped()){
                            sw.stop();
                        }
                        return r;
                    }
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
                    alert.setContentText(task.get());
                    alert.show();
                }catch (Exception ex){
                    ex.printStackTrace();
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
