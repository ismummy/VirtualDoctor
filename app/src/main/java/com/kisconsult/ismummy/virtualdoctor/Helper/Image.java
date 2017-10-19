package com.kisconsult.ismummy.virtualdoctor.Helper;

import com.android.volley.toolbox.ImageLoader;
import com.kisconsult.ismummy.virtualdoctor.R;
import com.kisconsult.ismummy.virtualdoctor.App.EndPoints;
import com.kisconsult.ismummy.virtualdoctor.App.MyApplication;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ISMUMMY on 6/13/2016.
 */
public class Image {
    ImageLoader imageLoader;

    public Image(final CircleImageView image, String path) {
        path = EndPoints.IMG_BASE_URL + "/" + path;
        imageLoader = MyApplication.getInstance().getImageLoader();
        imageLoader.get(path, ImageLoader.getImageListener(
                image, R.drawable.profile, R.drawable.profile));
    }
}
