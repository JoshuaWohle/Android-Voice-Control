package com.findarato.cyanide;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CyanideRobotActivity extends Activity implements OnClickListener, OnInitListener, OnUtteranceCompletedListener {
	
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 12345;
	
    EditText server = null;
    EditText port = null;
    
    TextToSpeech tts = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        
        final Button buttonStart = (Button) findViewById(R.id.button_start);
        final Button buttonStop = (Button) findViewById(R.id.button_stop);
        final Button buttonSpeech = (Button) findViewById(R.id.button_speech);
        tts = new TextToSpeech(this, this);
        tts.setOnUtteranceCompletedListener(this);
        server = (EditText) findViewById(R.id.text_ip);
        port = (EditText) findViewById(R.id.text_port);
        
        port.setText("9002");
        
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonSpeech.setOnClickListener(this);
        
    }
    
    private void startVoiceRecognitionActivity() {
    	Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    	intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
    	intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please tell the robot what to do.");
    	intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    	intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
    	startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String serverString = server.getText().toString();
        int portInt = Integer.parseInt(port.getText().toString());
        
    	if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
    		ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    		for(String match : matches) {
    			if(match.equalsIgnoreCase("start robot") || match.equalsIgnoreCase("start") || match.equalsIgnoreCase("start cleaning")) {
    				tts.speak("Starting cleaning now", TextToSpeech.QUEUE_FLUSH, null);
    				new networkRequest(serverString, portInt, "START").execute();
    			}
    			else if(match.equalsIgnoreCase("stop robot") || match.equalsIgnoreCase("stop") || match.equalsIgnoreCase("stop cleaning")) {
    				tts.speak("Stopping cleaning now, returning to my charging dock.", TextToSpeech.QUEUE_FLUSH, null);
        			new networkRequest(serverString, portInt, "STOP", true).execute();
    			}
    		}
    		super.onActivityResult(requestCode, resultCode, data);
    	}
    }

	@Override
	public void onClick(View v) {
		
        String serverString = server.getText().toString();
        int portInt = Integer.parseInt(port.getText().toString());
		
		switch(v.getId()) {
		case R.id.button_start :
			new networkRequest(serverString, portInt, "START").execute();
			break;
		case R.id.button_stop :
			new networkRequest(serverString, portInt, "STOP").execute();
			break;
		case R.id.button_speech :
			HashMap<String, String> extra = new HashMap<String, String>();
			extra.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "start voice recognition");
			tts.speak("Hello master, what would you like me to do ?", TextToSpeech.QUEUE_ADD, extra);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUtteranceCompleted(String utteranceId) {
		if(utteranceId.equals("start voice recognition"))
			startVoiceRecognitionActivity();
	}
}