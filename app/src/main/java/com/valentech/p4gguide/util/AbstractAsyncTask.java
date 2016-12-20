package com.valentech.p4gguide.util;

import android.os.AsyncTask;

/**
 * Created by JD on 12/16/2016.
 */

public class AbstractAsyncTask extends AsyncTask<Void, Void, Void> {
    private Runnable task;
    private Runnable doAfter;

    public AbstractAsyncTask run(Runnable taskIn) {
        task = taskIn;
        return this;
    }

    public AbstractAsyncTask andThen(Runnable doAfterIn) {
        doAfter = doAfterIn;
        return this;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        if(task != null) {
            task.run();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        //if we had been blocking, run the runnable
        if(doAfter != null) {
            doAfter.run();
        }
    }

}
