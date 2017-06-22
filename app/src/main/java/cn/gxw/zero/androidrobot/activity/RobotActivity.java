package cn.gxw.zero.androidrobot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.turing.androidsdk.HttpRequestListener;
import com.turing.androidsdk.RecognizeListener;
import com.turing.androidsdk.RecognizeManager;
import com.turing.androidsdk.TTSListener;
import com.turing.androidsdk.TTSManager;
import com.turing.androidsdk.TuringManager;

import org.json.JSONException;
import org.json.JSONObject;

import cn.gxw.zero.androidrobot.R;
import cn.gxw.zero.androidrobot.app.MyApplication;

/**
 * Created by Administrator on 2017/2/7 0007.
 *
 * 语音机器人
 */

public class RobotActivity extends Activity {

    private String TAG = MainActivity.class.getSimpleName();

    /**
     * 返回结果，开始说话
     */
    public static final int MSG_SPEECH_START = 0;
    /**
     * 开始识别
     */
    public static final int MSG_RECOGNIZE_RESULT = 1;
    /**
     * 开始识别
     */
    public static final int MSG_RECOGNIZE_START = 2;

    private TTSManager mTtsManager;//百度文本转语音管理器
    private TuringManager mTuringManager;//图铃管理器
    private RecognizeManager mRecognizerManager;//百度语音管理器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        initView();
        initTTS();

    }

    public void speek(View view){
        String result = tv_requset.getText().toString();

        mHandler.obtainMessage(MSG_RECOGNIZE_RESULT, result).sendToTarget();
        tv_requset.setText("");
    }

    private EditText tv_requset;
    TextView tv_respones;

    //实例化view视图
    private void initView() {
        tv_requset = (EditText) findViewById(R.id.tv_requset);
        tv_respones = (TextView) findViewById(R.id.tv_respones);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_SPEECH_START:
                    tv_respones.setText("答：" + (String) msg.obj);
                    mTtsManager.startTTS((String) msg.obj);
                    break;
                case MSG_RECOGNIZE_RESULT:
                    tv_requset.setText("" + msg.obj);
//                    mRecognizerManager.stopRecognize();
                    mTuringManager.requestTuring((String) msg.obj);
//                    tv_requset.setText("答：" +HttpUtils.getRespones((String) msg.obj));
                    break;
                case MSG_RECOGNIZE_START:
                    tv_respones.setText("开始识别");
                    mRecognizerManager.startRecognize();
                    break;
            }
        }

        ;
    };

    /**
     * 实例化 语音系统
     */
    private void initTTS() {

        /** 支持百度，需自行去相关平台申请appid，并导入相应的jar和so文件 */
        mRecognizerManager = MyApplication.getInstents().getmRecognizerManager();
        mTtsManager = MyApplication.getInstents().getmTtsManager();
        mTuringManager = MyApplication.getInstents().getmTuringManager();


//        mTtsManager.setTTSListener(myTTSListener);

        mTuringManager.setHttpRequestListener(myHttpConnectionListener);
        mTtsManager.startTTS("主人！你终于来了。");
    }

    /**
     * 网络请求回调
     */
    HttpRequestListener myHttpConnectionListener = new HttpRequestListener() {

        @Override
        public void onSuccess(String result) {
            if (result != null) {
                try {
                    Log.d(TAG, "result" + result);
                    JSONObject result_obj = new JSONObject(result);
                    if (result_obj.has("text")) {
                        Log.d(TAG, result_obj.get("text").toString());
                        mHandler.obtainMessage(MSG_SPEECH_START,
                                result_obj.get("text")).sendToTarget();
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "JSONException:" + e.getMessage());
                }
            }
        }

        @Override
        public void onFail(int code, String error) {
            Log.d(TAG, "onFail code:" + code + "|error:" + error);
            Log.d(TAG, "onFail code:" + code + "|error:" + error);
            Log.d(TAG, "onFail code:" + code + "|error:" + error);
            Log.d(TAG, "onFail code:" + code + "|error:" + error);
            mHandler.obtainMessage(MSG_SPEECH_START, "网络慢脑袋不灵了").sendToTarget();
        }
    };

    /**
     * 语音识别回调   语音转文字
     */
    RecognizeListener myVoiceRecognizeListener = new RecognizeListener() {

        @Override
        public void onVolumeChange(int volume) {
            // 仅讯飞回调
        }

        @Override
        public void onStartRecognize() {
            // 仅针对百度回调
        }

        @Override
        public void onRecordStart() {

        }

        @Override
        public void onRecordEnd() {

        }

        @Override
        public void onRecognizeResult(String result) {
            Log.d(TAG, "识别结果：" + result);
            if (result == null) {
                mHandler.sendEmptyMessage(MSG_RECOGNIZE_START);
                return;
            }
            mHandler.obtainMessage(MSG_RECOGNIZE_RESULT, result).sendToTarget();
        }

        @Override
        public void onRecognizeError(String error) {
            Log.e(TAG, "识别错误：" + error);
            mHandler.sendEmptyMessage(MSG_RECOGNIZE_START);
        }
    };

    /**
     * TTS回调  文字转语音
     */
    TTSListener myTTSListener = new TTSListener() {

        @Override
        public void onSpeechStart() {
            Log.d(TAG, "onSpeechStart");
        }

        @Override
        public void onSpeechProgressChanged() {

        }

        @Override
        public void onSpeechPause() {
            Log.d(TAG, "onSpeechPause");
        }

        @Override
        public void onSpeechFinish() {
            Log.d(TAG, "onSpeechFinish");
            mHandler.sendEmptyMessage(MSG_RECOGNIZE_START);
        }

        @Override
        public void onSpeechError(int errorCode) {
            Log.d(TAG, "onSpeechError：" + errorCode);
            mHandler.sendEmptyMessage(MSG_RECOGNIZE_START);
        }

        @Override
        public void onSpeechCancel() {
            Log.d(TAG, "TTS Cancle!");
        }
    };


}
