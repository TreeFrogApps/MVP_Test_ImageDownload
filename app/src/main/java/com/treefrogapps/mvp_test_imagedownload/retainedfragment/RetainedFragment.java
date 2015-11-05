package com.treefrogapps.mvp_test_imagedownload.retainedfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.HashMap;

public class RetainedFragment extends Fragment {

    public static final String RETAINED_FRAGMENT_TAG = "com.treefrogapps.mvp_test_imagedownload.retainedfragment.tag";
    private HashMap<String, Object> mObject = new HashMap<String, Object>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }


    public void putObject(String key, Object object) {

        mObject.put(key, object);
    }

    public Object getObject(String key) {

        return mObject.get(key);
    }
}
