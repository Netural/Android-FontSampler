package com.fontsampler.manager;

import android.os.AsyncTask;
import android.util.Log;

import com.fontsampler.listener.OnTaskCompled;

import java.io.File;

/**
 * Created by sebastian on 26.06.14.
 */
public class FontReaderTask extends AsyncTask<String, Integer, String[]> {

    private OnTaskCompled callback;


    public FontReaderTask(OnTaskCompled callback) {
        this.callback = callback;
    }

    protected String[] doInBackground(String... urls) {
        Log.i("Fagment", "start list");
        // get.getExternalCacheDir().getAbsolutePath()
        File dir = new File(urls[0]);
        String[] fileNames = null;

        if (dir.exists()) {
            File file[] = dir.listFiles();
            Log.d("Files", "Size: " + file.length);
            fileNames = new String[file.length];
            for (int i = 0; i < file.length; i++) {
                Log.d("Files", "FileName:" + file[i].getName());
                fileNames[i] = file[i].getName();
            }
            return fileNames;
        }

        return fileNames;
    }

    @Override
    protected void onPostExecute(String[] files) {

        if (files == null) {
            files = new String[1];
            files[0] = "";
        }
        callback.onSucess(files);

    }
}


