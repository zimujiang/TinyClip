package com.zimujiang.tinyclip.utils;

import org.bytedeco.javacpp.Loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {
    public static void main(String[] args){
        String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
        ProcessBuilder pb = new ProcessBuilder(ffmpeg,"-i","/Users/semoon/Desktop/Xm8pXs8hBGiW4wTWrsSMW23dyRN2kAyf.mp4","-vf","'subtitles=/Users/semoon/Desktop/Xm8pXs8hBGiW4wTWrsSMW23dyRN2kAyf.srt'","/Users/semoon/Desktop/Xm8pXs8hBGiW4wTWrsSMW23dyRN2kAyf_subtitle.mp4");
        try {
            System.out.println(pb.command().toString());
            pb.redirectErrorStream(true);
            //pb.start().waitFor();
            Process process =  pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
