package com.songguoliang.springboot.util;

import ws.schild.jave.*;

import java.io.File;

public class Compress {

    /**

     * 保存视频File的路径，原视频名，压缩后视频名

     */

    public static File compressionVideo(String sourcePath, String sourceName, String picPath, String picName, int rate) {

        File source = new File(sourcePath + File.separator + sourceName);
        File target = new File(picPath + File.separator + picName);

        try {
            MultimediaObject object = new MultimediaObject(source);
            AudioInfo audioInfo = object.getInfo().getAudio();
            //  视频属性设置
            int maxBitRate = 128000;
            int maxSamplingRate = 44100;
            int bitRate = 800000;
            int maxFrameRate = 20;
            int maxWidth = 1280;
            AudioAttributes audio = new AudioAttributes();
            // 设置通用编码格式

            /** 音频*/
            audio.setCodec("aac");
            // 设置最大值：比特率越高，清晰度/音质越好
            // 设置音频比特率,单位:b (比特率越高，清晰度/音质越好，当然文件也就越大 128000 = 128kb)
            if (audioInfo.getBitRate() > maxBitRate) {
                audio.setBitRate(new Integer(maxBitRate));
            }
            audio.setBitRate(audioInfo.getBitRate());
            // 设置重新编码的音频流中使用的声道数（1 =单声道，2 = 双声道（立体声））。如果未设置任何声道值，则编码器将选择默认值 0。
            audio.setChannels(audioInfo.getChannels());
            // 设置编码时候的音量值，未设置为0,如果256，则音量值不会改变
            // audio.setVolume(256);
            if (audioInfo.getSamplingRate() > maxSamplingRate) {
                audio.setSamplingRate(maxSamplingRate);
            }
            audio.setSamplingRate(audioInfo.getSamplingRate());
            //  视频编码属性配置

            /** 视频*/
            VideoInfo videoInfo = object.getInfo().getVideo();
            VideoAttributes video = new VideoAttributes();
            video.setCodec("h264");
            // 设置音频比特率,单位:b (比特率越高，清晰度/音质越好，当然文件也就越大 800000 = 800kb)
            if (videoInfo.getBitRate() > bitRate) {
                video.setBitRate(bitRate);
            }
            video.setBitRate(videoInfo.getBitRate());
            // 视频帧率：15 f / s 帧率越低，效果越差
            // 设置视频帧率（帧率越低，视频会出现断层，越高让人感觉越连续），视频帧率（Frame rate）是用于测量显示帧数的量度。所谓的测量单位为每秒显示帧数(Frames per Second，简：FPS）或“赫兹”（Hz）。
            if (videoInfo.getFrameRate() > maxFrameRate) {
                video.setFrameRate(maxFrameRate);
            }
            video.setFrameRate(new Integer((int) videoInfo.getFrameRate()));
            // 限制视频宽高
            int width = videoInfo.getSize().getWidth();
            int height = videoInfo.getSize().getHeight();
            if (width > maxWidth) {
                float rat = (float) width / maxWidth;
                video.setSize(new VideoSize(maxWidth, (int) (height / rat)));
            }

            video.setSize(new VideoSize(width / rate, height / rate));


            EncodingAttributes attr = new EncodingAttributes();
            attr.setFormat("mp4");
            attr.setAudioAttributes(audio);
            attr.setVideoAttributes(video);
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attr);
//            System.out.println("压缩总耗时：" + (System.currentTimeMillis() - time) / 1000);
            return target;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (target.length() > 0) {
//                source.delete();
            }
        }
        return source;
    }

}
