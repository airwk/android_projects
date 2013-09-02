package plia.manager;

import android.content.Context;

import plia.interfaces.IState;

/**
 * Created by Wirune on 1/9/2556.
 */
public abstract class Manager implements IState
{
    private Context context;

    protected Manager()
    {

    }

    public Context getContext()
    {
        return context;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }
}
