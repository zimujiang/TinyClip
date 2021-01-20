package com.zimujiang.tinyclip.ffmpeg.runner;


import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.zimujiang.tinyclip.ffmpeg.handler.DefaultCallbackHandler;
import com.zimujiang.tinyclip.ffmpeg.handler.ProcessCallbackHandler;
import com.zimujiang.tinyclip.ffmpeg.model.VideoInfo;
import com.zimujiang.tinyclip.ffmpeg.utils.FFmpegUtils;
import com.zimujiang.tinyclip.ffmpeg.utils.FileUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by tonydeng on 15/4/16.
 */
public class FFmpegCommandRunner {
	private static final Logger log = LoggerFactory.getLogger(FFmpegCommandRunner.class);
	private static ProcessBuilder pb = null;
	private static Process process = null;
	
	
	/**
	 * 获取视频信息
	 *
	 * @param input
	 * @return
	 */
	public static VideoInfo getVideoInfo(File input) {
		VideoInfo vi = new VideoInfo();
		if (input != null && input.exists()) {
			List<String> commands = Lists.newArrayList(BaseCommandOption.getFFprobeBinary());
			commands.add(input.getAbsolutePath());
			vi.setSize(FileUtils.getFineSize(input));
			if (vi.getSize() > 0) {
				return FFmpegUtils.regInfo(runProcess(commands), vi);
			}
		} else {
			if (log.isErrorEnabled())
				log.error("video '{}' is not fount! ", input.getAbsolutePath());
		}

		return vi;
	}
	
	/**
	 * 转为mp3音频8k格式
	 * 
	 * @param input
	 * @return
	 */
	public static String coverToMp3_8K(String input) {
		File f = new File(input);
		File output = FileUtils.getMp3_8K_OutputByInput(f);
		if (input != null && f.exists()) {
			List<String> commands = Lists.newArrayList(BaseCommandOption.getFFmpegBinary());
			commands.add("-i");
			commands.add(input);
			commands.add("-ar");
			commands.add("8000");
			commands.add(output.getAbsolutePath());
			if (StringUtils.isNotEmpty(runProcess(commands))) {
				return output.getAbsolutePath();
			}
		}else {
			if (log.isErrorEnabled())
				log.error("video '{}' convert 8k mp3 '{}' create error!",input,output.getAbsolutePath());
		}
		return null;
	}

	/**
	 * 转为mp3音频格式
	 * 
	 * @param input
	 * @return
	 */
	public static String coverToMp3(String input) {
		File f = new File(input);
		File output = FileUtils.getMp3OutputByInput(f);
		if (input != null && f.exists()) {
			List<String> commands = Lists.newArrayList(BaseCommandOption.getFFmpegBinary());
			commands.add("-i");
			commands.add(input);
			commands.add("-f");
			commands.add("mp3");
			commands.add("-vn");
			commands.add(output.getAbsolutePath());
			if (StringUtils.isNotEmpty(runProcess(commands))) {
				return output.getAbsolutePath();
			}
		}else {
			if (log.isErrorEnabled())
				log.error("video '{}' convert mp3 '{}' create error!",input,output.getAbsolutePath());
		}
		return null;
	}
	
	
	/**
	 * 转为wav音频格式
	 * 
	 * @param input
	 * @return
	 */
	public static String coverToWav(String input) {
		File f = new File(input);
		File output = FileUtils.getWavOutputByInput(f);
		if (input != null && f.exists()) {
			List<String> commands = Lists.newArrayList(BaseCommandOption.getFFmpegBinary());
			commands.add("-i");
			commands.add(input);
			commands.add("-f");
			commands.add("wav");
			commands.add("-ar");
			commands.add("16000");
			commands.add(output.getAbsolutePath());
			if (StringUtils.isNotEmpty(runProcess(commands))) {
				return output.getAbsolutePath();
			}
		}else {
			if (log.isErrorEnabled())
				log.error("video '{}' convert wav '{}' create error!",input,output.getAbsolutePath());
		}
		return null;
	}

	/**
	 * 执行命令
	 *
	 * @param commands
	 * @return
	 */
	public static String runProcess(List<String> commands) {
		try {
			return runProcess(commands, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 执行命令
	 *
	 * @param commands
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	public static String runProcess(List<String> commands, ProcessCallbackHandler handler) {
		if (log.isDebugEnabled())
			log.debug("start to run ffmpeg process... cmd : '{}'", FFmpegUtils.ffmpegCmdLine(commands));
		Stopwatch stopwatch = Stopwatch.createStarted();
		pb = new ProcessBuilder(commands);

		pb.redirectErrorStream(true);

		if (null == handler) {
			handler = new DefaultCallbackHandler();
		}

		String result = null;
		try {
			process = pb.start();
			result = handler.handler(process.getInputStream());
		} catch (Exception e) {
			log.error("errorStream:{}", result, e);
		} finally {
			if (null != process) {
				try {
					process.getInputStream().close();
					process.getOutputStream().close();
					process.getErrorStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			int flag = process.waitFor();
			if (flag != 0) {
				throw new IllegalThreadStateException("process exit with error value : " + flag);
			}
		} catch (InterruptedException e) {
			log.error("wait for process finish error:{}", e);
		} finally {
			if (null != process) {
				process.destroy();
				pb = null;
			}

			stopwatch.stop();
		}
		if (log.isInfoEnabled()) {
			log.info("ffmpeg run {} seconds, {} milliseconds", stopwatch.elapsed(TimeUnit.SECONDS),
					stopwatch.elapsed(TimeUnit.MILLISECONDS));
		}
		//注释掉该句，则返回ffmpeg命令行结果
		result = "共消耗"+stopwatch.elapsed(TimeUnit.SECONDS)+"秒"+stopwatch.elapsed(TimeUnit.MILLISECONDS)+"毫秒";

		return result;
	}
}
