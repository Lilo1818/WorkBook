
package com.mci.firstidol.utils;

import java.io.File;

import com.mci.firstidol.fragment.welfare.Common;

import android.media.MediaRecorder;


public class AudioRecorder {
    private static int SAMPLE_RATE_IN_HZ = 8000;

    private MediaRecorder mMediaRecorder;
    private String mPath;

    public AudioRecorder() {
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }

        mPath = Common.getBasePath() + Common.AUDIO_DIR + System.currentTimeMillis() + ".amr";
        File directory = new File(Common.getBasePath() + Common.AUDIO_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void start() {
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
            mMediaRecorder.setOutputFile(mPath);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getAmplitude() {
        if (mMediaRecorder != null) {
            return (mMediaRecorder.getMaxAmplitude());
        } else {
            return 0;
        }
    }

    public void deleteAudioFile() {
        File file = new File(mPath);
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public String getAudioRecordPath() {
        return mPath;
    }
}