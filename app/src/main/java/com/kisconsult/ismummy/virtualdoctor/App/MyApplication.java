package com.kisconsult.ismummy.virtualdoctor.App;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.kisconsult.ismummy.virtualdoctor.Helper.MyPreference;
import com.kisconsult.ismummy.virtualdoctor.Util.LruBitmapCache;

/**
 * Created by ISMUMMY on 6/13/2016.
 */
public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication instance;

    private MyPreference pref;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    public MyPreference getPref() {
        if (pref == null) {
            pref = new MyPreference(this);
        }
        return pref;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelRequest() {
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(requestQueue, new LruBitmapCache());
        }
        return imageLoader;
    }
}
