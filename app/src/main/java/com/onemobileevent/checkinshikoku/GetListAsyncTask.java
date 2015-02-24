package com.onemobileevent.checkinshikoku;

import android.os.AsyncTask;
import android.util.Log;

import com.onemobileevent.checkinshikoku.backend.myApi.MyApi;
import com.onemobileevent.checkinshikoku.backend.myApi.model.CheckInInfoCollection;

import java.io.IOException;

/**
 * AsyncTask for calling getList API.
 */
public class GetListAsyncTask extends AsyncTask<Void, Void, CheckInInfoCollection> {

    private MyApi api;

    @Override
    protected CheckInInfoCollection doInBackground(Void... params) {
        try {
            return api.getList().execute();
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
            return null;
        }
    }

    public GetListAsyncTask(MyApi api) {
        super();
        this.api = api;
    }
}
