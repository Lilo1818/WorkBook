package com.mci.firstidol.callbacks_and_listeners;

/**
 * Created by wang on 2015/7/23.
 */
public interface DownLoadCallback {
    public void Success();
    public void Fail();
    public void getProcess(int process);
}
