package com.zimujiang.tinyclip.utils;

import com.google.common.collect.Lists;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by tonydeng on 15/4/17.
 */
public class FileUtils {
	private static final Logger log = LoggerFactory.getLogger(FileUtils.class);
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
	 * 获得文件大小
	 * 
	 * @param input
	 * @return
	 */
	public static long getFineSize(File input) {
		if (input != null && input.exists()) {
			try {
				return new FileInputStream(input).available();
			} catch (IOException e) {
				if (log.isErrorEnabled())
					log.error("getFineSize error:'{}'", e.getMessage());
				e.printStackTrace();
			}
		}
		return 0;
	}


	/**
	 * 根据视频文件获得HLS的m3u8文件
	 *
	 * @param input
	 * @return
	 */
	public static File getM3U8OutputByInput(File input) {
		String videoName = getFileName(input);
		if (StringUtils.isNotEmpty(videoName)) {
			File outputPath = new File(input.getParent() + File.separator + PATH_HLS + File.separator + videoName);
			if (createDirectory(outputPath)) {
				return new File(outputPath.getAbsolutePath() + File.separator + videoName + EXTEND_M3U8);
			}
		}
		return null;
	}

	/**
	 * 通过m3u8文件获得该目录下的所有ts文件
	 *
	 * @param m3u8
	 * @return
	 */
	public static List<File> findTS(File m3u8) {
		if (m3u8 != null && m3u8.exists()) {
			final File path = m3u8.getParentFile();
			if (path.isDirectory()) {
				FilenameFilter filter = new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(EXTEND_TS);
					}
				};
				return Lists.newArrayList(path.listFiles(filter));
			}
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * 根据视频文件获得生成的mp4文件地址
	 *
	 * @param input
	 * @return
	 */
	public static File getMp4OutputByInput(File input) {
		String videoName = getFileName(input);
		if (StringUtils.isNotEmpty(videoName) && !"mp4".equals(getFileExtend(input))) {
			return new File(input.getParent() + File.separator + videoName + EXTEND_MP4);
		}
		return null;
	}

	/**
	 * 根据视频文件获得生成的mp3文件地址
	 *
	 * @param input
	 * @return
	 */
	public static File getMp3OutputByInput(File input) {
		String videoName = getFileName(input);
		if (StringUtils.isNotEmpty(videoName)) {
			return new File(input.getParent() + File.separator + videoName + EXTEND_MP3);
		}
		return null;
	}

	/**
	 * 根据视频文件获得生成的mp3文件16k地址
	 *
	 * @param input
	 * @return
	 */
	public static File getMp3_16K_OutputByInput(File input) {
		String videoName = getFileName(input);
		if (StringUtils.isNotEmpty(videoName)) {
			return new File(input.getParent() + File.separator + videoName + "_16k" + EXTEND_MP3);
		}
		return null;
	}
	
	/**
	 * 根据视频文件获得生成的mp3文件8k地址
	 *
	 * @param input
	 * @return
	 */
	public static File getMp3_8K_OutputByInput(File input) {
		String videoName = getFileName(input);
		if (StringUtils.isNotEmpty(videoName)) {
			return new File(input.getParent() + File.separator + videoName + "_8k" + EXTEND_MP3);
		}
		return null;
	}

	/**
	 * 根据视频文件获得生成的wav文件地址
	 *
	 * @param input
	 * @return
	 */
	public static File getWavOutputByInput(File input) {
		String videoName = getFileName(input);
		if (StringUtils.isNotEmpty(videoName)) {
			return new File(input.getParent() + File.separator + videoName + EXTEND_WAV);
		}
		return null;
	}

	/**
	 * 根据视频文件获得生成的SRT文件地址
	 *
	 * @param input
	 * @return
	 */
	public static String getSRTOutputByInput(File input) {
		String videoName = getFileName(input);
		if (StringUtils.isNotEmpty(videoName)) {
			return input.getParent() + File.separator + videoName + EXTEND_SRT;
		}
		return null;
	}

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
			return file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
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
