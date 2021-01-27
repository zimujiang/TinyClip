package com.zimujiang.tinyclip.ctrl;

import com.zimujiang.tinyclip.utils.FileUtils;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.lang3.time.StopWatch;
import org.bytedeco.javacpp.Loader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 字幕处理TabCtrl
 */
public class SubtitleConvertCtrl implements Initializable {

    public TextField subTxt;
    public TextField subOutPutTxt;
    public Button subSelectFileBtn;
    public Button subSelectOutPutDirBtn;
    public Button subStartBtn;
    public ComboBox subFormatCb;
    public ImageView subimageView;

    public void initialize(URL location, ResourceBundle resources) {
        subFormatCb.getItems().addAll("srt","ass");
        subFormatCb.getSelectionModel().select(0);
        subimageView.setVisible(false);
    }


    /**
     * 字幕格式转换
     * @param actionEvent
     */
    public void subStartBtnClick(ActionEvent actionEvent) {
        String input = subTxt.getText();
        String outputDir = subOutPutTxt.getText();
        String format = subFormatCb.getValue().toString().toLowerCase();
        File f = new File(input);
        File d = new File(outputDir);
        String subName = "TinyClip_"+ FileUtils.getFileName(f)+"_"+ System.currentTimeMillis() / 1000L;
        if(input!=null && outputDir!=null && d.exists() && f.exists()) {
            String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
            ProcessBuilder pb = new ProcessBuilder(ffmpeg,"-i",input,outputDir + File.separator + subName +"."+ format);
            Task<String> task = new Task<String>() {
                @Override public String call() {
                    StopWatch sw = new StopWatch();
                    String r = "";
                    try {
                        sw.start();
                        pb.start().waitFor();
                        sw.stop();
                        r = "转换完成，耗时："+sw.toString();
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
                subStartBtn.setText("正在处理");
                subStartBtn.setDisable(true);
                subimageView.setVisible(true);
            });
            task.setOnSucceeded((e) -> {
                subStartBtn.setText("开始转换");
                subStartBtn.setDisable(false);
                subimageView.setVisible(false);
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
                alert.setContentText("转换失败");
                alert.show();
            });
            new Thread(task).start();


        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("请确保输入字幕和输出目录有效");
            alert.show();
        }


    }

    /**
     * 选择输出目录按钮单击事件
     * @param actionEvent
     */
    public void subSelectOutPutDirBtnClick(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择输出目录");
        File directory = directoryChooser.showDialog(subSelectOutPutDirBtn.getScene().getWindow());
        if (directory != null) {
            subOutPutTxt.setText(directory.getAbsolutePath());
        }
    }

    /**
     * 选择字幕路径按钮单击事件
     * @param actionEvent
     */
    public void subSelectFileBtnClick(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        // 文件类型过滤器
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("字幕文件 (*.srt、*.ass)", "*.srt", "*.ass");
        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showOpenDialog(subSelectFileBtn.getScene().getWindow());
        String fileAbsolutePath = file == null ? "" : file.getAbsolutePath();
        if (file != null) {
            subTxt.setText(fileAbsolutePath);
            subOutPutTxt.setText(file.getParentFile().getAbsolutePath());
        }
    }
}
