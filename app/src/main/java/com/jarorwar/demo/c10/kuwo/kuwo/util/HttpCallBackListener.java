package com.jarorwar.demo.c10.kuwo.kuwo.util;

/**
 * Created by marvinmin on 10/18/16.
 */

public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
