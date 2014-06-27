package com.fontsampler.listener;

/**
 * Created by sebastian on 26.06.14.
 */
public interface OnTaskCompled {

    public void onSucess(String[] result);

    public void onError(Exception ex, String message);
}
