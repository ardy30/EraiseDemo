package com.eraise.eraisedemo.activity;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.eraise.eraisedemo.R;
import com.eraise.eraisedemo.utils.Toast;

/**
 * TTS服务在Android是自带的基础的文字阅读，
 * 高级的或许要使用诸如讯飞等公司的语音服务了
 * 在TextToSpeech.OnInitListener()当中设置语言，
 * 用前需要先检查手机是否已经安装了TTS引擎
 * @author: 思落羽
 * @date: 2014-2-14
 * @Description:
 */
public class TTSDemo extends Activity{
	
	protected EditText et;
	protected Button btn;
	
	protected TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tts_demo);
		
		et = (EditText) findViewById(R.id.et);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new SpeakClick());
		init();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		tts.shutdown();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 0) {
			/* TTS引擎安装检测通过 */
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				/* 初始化TTS实例对象 */
				tts = new TextToSpeech(this, new TTSInitListener());
			} else {
				/* 安装TTS引擎 */
				Intent installIntent= new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}
	
	protected void init() {
		
		checkTTSEngineInstall();
	}
	
	/**
	 * 检查TTS引擎是否已安装，结果在 onActivityResult 中显示
	 */
	protected void checkTTSEngineInstall() {
		
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, 0);
	}
	
	protected class SpeakClick implements OnClickListener {

		@Override
		public void onClick(View v) {

			String text = et.getText().toString().trim();
			if (text.length() > 0) {
				if (!tts.isSpeaking())
					tts.stop();
				if (TextToSpeech.SUCCESS == tts.speak(text, TextToSpeech.QUEUE_ADD, null)) {
					Toast.showToast("说话成功");
				} else {
					Toast.showToast("说话失败");
				}
			}
		}
		
	}
	
	protected class TTSInitListener implements TextToSpeech.OnInitListener {

		@Override
		public void onInit(int status) {

			switch (status) {
			case TextToSpeech.SUCCESS:
				Toast.showToast("TTS服务初始化成功");
				int result = tts.setLanguage(Locale.US);
				if (result == TextToSpeech.LANG_MISSING_DATA || 
						result == TextToSpeech.LANG_NOT_SUPPORTED) {
					Toast.showToast("不支持语言");
				} else {
					tts.speak("Hello!", TextToSpeech.QUEUE_ADD, null);
				}
				break;
			case TextToSpeech.ERROR:
				Toast.showToast("TTS服务初始化失败");
				break;
			}
		}
		
	}
	
}
