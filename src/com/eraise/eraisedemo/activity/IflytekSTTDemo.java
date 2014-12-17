package com.eraise.eraisedemo.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.eraise.eraisedemo.R;
import com.eraise.eraisedemo.utils.Toast;
import com.iflytek.speech.ErrorCode;
import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.RecognizerListener;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechRecognizer;

/**
 * 语音识别
 * 讯飞语音API需要安装讯飞语音+才能使用，可以把apk放在assets中安装。
 * 讯飞语音的库是 SpeechApi.jar
 * @author: 思落羽
 * @date: 2014-2-18
 * @Description:
 */
public class IflytekSTTDemo extends Activity{
	
	/* 语音识别类 */
	protected SpeechRecognizer recognizer;
	/* 识别监听 */
	protected SpeechListener sListener;
	
	protected EditText et;
	protected Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iflytekstt_demo);
		
		initView();
		init();
	}
	
	protected void initView() {
		
		et = (EditText) findViewById(R.id.et);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				et.setText(null);
				/* 开始录音识别 */
				recognizer.startListening(sListener);
			}
			
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		/* 停止录音识别 */
		recognizer.stopListening(sListener);
	}
	
	protected void init() {
		
		recognizer = 
				new SpeechRecognizer(getApplicationContext(), new Initialization());
		/* 语种设置为中文 zh_cn */
		recognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		/* 前端点超时 4000 毫秒 */
		recognizer.setParameter(SpeechConstant.VAD_BOS, "4000");
		/* 后端点超时 1000 毫秒 */
		recognizer.setParameter(SpeechConstant.VAD_EOS, "1000");
		/* 设置为点标点符号  */
		recognizer.setParameter(SpeechConstant.PARAMS, "asr_ptt=1");
		sListener = new SpeechListener();
	}
	
	/**
	 * 语音识别返回的结果是JSON对象，需要进行解析。
	 * JSON对象内包括了JSONArray->JSONObject->JSONArray->JSONObject叶结点
	 * {"sn":1,"ls":false,"bg":0,"ed":0,"ws":[
	 * 		{"bg":0,"cw":[{"w":"难道","sc":0}
	 * 	]},{"bg":0,"cw":[
	 * 		{"w":"你","sc":0}
	 * 	]},{"bg":0,"cw":[
	 * 		{"w":"就","sc":0}
	 * 	]},{"bg":0,"cw":[
	 * 		{"w":"不","sc":0}
	 * 	]},{"bg":0,"cw":[
	 * 		{"w":"应该","sc":0}
	 * 	]},{"bg":0,"cw":[
	 * 		{"w":"是","sc":0}
	 * 	]},{"bg":0,"cw":[
	 * 		{"w":"那样","sc":0}
	 * 	]},{"bg":0,"cw":[
	 * 		{"w":"吧","sc":0}
	 * 	]}
	 * ]}
	 * @param str
	 * @return
	 */
	protected String getWord(String str) {
		
		StringBuilder sb = new StringBuilder();
		try {
			JSONObject jObj = new JSONObject(str);
			JSONArray ws = jObj.getJSONArray("ws");
			for (int i = 0; i < ws.length(); i ++) {
				JSONObject wsObj = ws.getJSONObject(i);
				JSONArray cw = wsObj.getJSONArray("cw");
				sb.append(cw.getJSONObject(0).getString("w"));
				/* 多候选才需要 */
//				for (int j = 0; j < cw.length(); j ++) {
//					sb.append(cw.getJSONObject(i).getString("w"));
//				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 初始化监听
	 * @author: 思落羽
	 * @date: 2014-2-18
	 * @Description:
	 */
	protected class Initialization implements InitListener {

		@Override
		public void onInit(ISpeechModule arg0, int arg1) {
			
			if (arg1 == ErrorCode.SUCCESS) {
				Toast.showToast("初始化完成");
			} else {
				Toast.showToast("初始化失败");
			}
		}
		
	}
	
	/**
	 * 语音识别监听
	 * @author: 思落羽
	 * @date: 2014-2-18
	 * @Description:
	 */
	protected class SpeechListener extends RecognizerListener.Stub {

		@Override
		public void onBeginOfSpeech() throws RemoteException {
			
			Toast.showToast("开始聆听");
		}

		@Override
		public void onEndOfSpeech() throws RemoteException {
			
			Toast.showToast("聆听完毕");
		}

		@Override
		public void onError(int arg0) throws RemoteException {
			
			Toast.showToast("出错了!错误代码 " + arg0);
		}

		/**
		 * 有结果的时候调用 
		 */
		@Override
		public void onResult(RecognizerResult result, boolean isLast)
				throws RemoteException {

//			System.out.println(result.getResultString());
			/* 获取到结果的字符串 */
			final String r = getWord(result.getResultString());
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					
					et.append(r);
				}
				
			});
		}

		@Override
		public void onVolumeChanged(int volume) throws RemoteException {
			
//			System.out.println("说话音量" + volume);
		}
	}
	
}
