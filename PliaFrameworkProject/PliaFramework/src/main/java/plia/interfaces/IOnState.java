package plia.interfaces;

import plia.framework.GameTime;

/**
 * Created by Wirune on 1/9/2556.
 */
public interface IOnState
{
    void onCreate();
    void onResume();
    void onPause();
    void onDestroy();
}