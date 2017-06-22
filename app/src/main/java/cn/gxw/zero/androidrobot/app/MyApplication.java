package cn.gxw.zero.androidrobot.app;

import android.app.Application;

import com.turing.androidsdk.RecognizeManager;
import com.turing.androidsdk.TTSManager;
import com.turing.androidsdk.TuringManager;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public class MyApplication extends Application{
    private static MyApplication myApplication = null;
    private TTSManager mTtsManager;//文本转语音管理器
    private RecognizeManager mRecognizerManager;//百度语音管理器
    private TuringManager mTuringManager;//图铃机器人管理器

    public static MyApplication getInstents(){
        if (myApplication ==null)
             new Throwable(new NullPointerException());
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        initBD_YuYin();
    }

    public RecognizeManager getmRecognizerManager(){
        return mRecognizerManager;
    }
    public TTSManager getmTtsManager(){
        return mTtsManager;
    }
    public TuringManager getmTuringManager(){
        return mTuringManager;
    }


    /**
     * 初始化百度语音
     */
    private void initBD_YuYin() {
//        /** 支持百度，需自行去相关平台申请appid，并导入相应的jar和so文件 */
        mRecognizerManager = new RecognizeManager(this, Constants.BD_APP_KEY, Constants.BD_SECRET_KEY);
        mTtsManager = new TTSManager(this, Constants.BD_APP_KEY, Constants.BD_SECRET_KEY);
        mTuringManager = new TuringManager(this, Constants.APP_KEY, Constants.SECRET_KEY);
    }
}
