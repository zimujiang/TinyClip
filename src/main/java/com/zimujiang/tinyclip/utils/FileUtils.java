package com.zimujiang.tinyclip.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by tonydeng on 15/4/17.
 */
public class FileUtils {

	private static final String PATH_SRCEENTSHOT = "srceent";
	private static final String PATH_HLS = "hls";
	private static final String EXTEND_JPG = ".jpg";
	private static final String EXTEND_M3U8 = ".m3u8";
	private static final String EXTEND_TS = ".ts";
	private static final String EXTEND_MP4 = ".mp4";
	private static final String EXTEND_MP3 = ".mp3";
	private static final String EXTEND_WAV = ".wav";
	private static final String EXTEND_SRT = ".srt";

	/**
	 * 创建目录
	 *
	 * @param path
	 * @return
	 */
	public static boolean createDirectory(String path) {
		if (StringUtils.isNotEmpty(path)) {
			return createDirectory(new File(path));
		}
		return false;
	}

	/**
	 * 创建目录
	 *
	 * @param path
	 * @return
	 */
	public static boolean createDirectory(File path) {
		if (path.exists()) {
			return path.isDirectory();
		} else {
			return path.mkdirs();
		}
	}

	/**
	 * 获得文件名
	 *
	 * @param file
	 * @return
	 */
	public static String getFileName(File file) {
		if (file != null && file.exists() && file.isFile()) {
			if (file.getName().lastIndexOf(".") > 0)
				return file.getName().substring(0, file.getName().lastIndexOf(".")).toLowerCase();
			else
				return file.getName();
		}
		return null;
	}

	/**
	 * 获得文件扩展名
	 *
	 * @param file
	 * @return
	 */
	public static String getFileExtend(File file) {
		if (file != null && file.exists() && file.isFile() && file.getName().lastIndexOf(".") > 0) {
			return file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
		}
		return null;
	}
	
	public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
	
}
