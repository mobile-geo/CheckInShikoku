package com.onemobileevent.checkinshikoku;

import android.os.AsyncTask;
import android.util.Log;

import com.onemobileevent.checkinshikoku.backend.myApi.MyApi;
import com.onemobileevent.checkinshikoku.backend.myApi.model.CheckInInfo;

import java.io.IOException;

/**
 * AsyncTask for calling Check In API.
 */
public class CheckInAsyncTask extends AsyncTask<Integer, Void, CheckInInfo> {

    private MyApi api;

    @Override
    protected CheckInInfo doInBackground(Integer... params) {
        try {
            return api.checkIn(params[0].intValue()).execute();
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
            return null;
        }
    }

    public CheckInAsyncTask(MyApi api) {
        super();
        this.api = api;
    }
}
