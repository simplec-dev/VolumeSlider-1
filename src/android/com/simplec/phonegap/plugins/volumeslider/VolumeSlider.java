package com.simplec.phonegap.plugins.volumeslider;

import java.lang.reflect.Method;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.AudioManager;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;

public class VolumeSlider extends CordovaPlugin {
	private static final String CREATE_SLIDER = "createVolumeSlider";
	private static final String SHOW_SLIDER = "showVolumeSlider";
	private static final String HIDE_SLIDER = "hideVolumeSlider";
	
	private PopupWindow seekBarWindow = null;
	private int originx = 0;
	private int originy = 0;
	private int width = 0;
	private int height = 0;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		// TODO Auto-generated method stub
		super.initialize(cordova, webView);
	}

	@Override
	public boolean execute(String action, JSONArray data, final CallbackContext callbackContext) throws JSONException {
		try {
			if (CREATE_SLIDER.equals(action)) {
				Log.e("VolmeSlider", "creating slider execute");

				originx = data.getInt(0);
				originy = data.getInt(1);
				width = data.getInt(2);
				height = data.getInt(3);
	
				return true;
			} else if (SHOW_SLIDER.equals(action)) {
				if (seekBarWindow!=null) {
					seekBarWindow.dismiss();
					seekBarWindow = null;
				}
				
				createSlider(callbackContext);
		        seekBarWindow.showAtLocation(webView, Gravity.LEFT | Gravity.TOP, originx, originy);
				
				return true;
			} else if (HIDE_SLIDER.equals(action)) {
				if (seekBarWindow!=null) {
					seekBarWindow.dismiss();
					seekBarWindow = null;
				}
				
				return true;
			} else {
				callbackContext.error(action + " is not a supported function.");
				return false;
			}
		} catch (Exception e) {
			callbackContext.error(e.getMessage());
			return false;
		}
	}
	
	private class VolumeSeekBarListener implements SeekBar.OnSeekBarChangeListener {
		private CallbackContext callbackContext;
		
		public VolumeSeekBarListener(CallbackContext callbackContext) {
			this.callbackContext = callbackContext;
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			int progress = seekBar.getProgress();
		
    		Log.e("VolumeSlider", "setting volume callback (stop) "+progress+"  max="+seekBar.getMax());
			setVolume(progress);
			
            PluginResult progressResult = new PluginResult(PluginResult.Status.OK, progress);
            progressResult.setKeepCallback(true);
            callbackContext.sendPluginResult(progressResult);
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			int progress = seekBar.getProgress();
			
    		Log.e("VolumeSlider", "setting volume callback (start) "+progress+"  max="+seekBar.getMax());
    		setVolume(progress);
			
            PluginResult progressResult = new PluginResult(PluginResult.Status.OK, progress);
            progressResult.setKeepCallback(true);
            callbackContext.sendPluginResult(progressResult);
		}

		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    		Log.e("VolumeSlider", "setting volume callback (change) "+progress+"  max="+seekBar.getMax());
			setVolume(progress);
			
            PluginResult progressResult = new PluginResult(PluginResult.Status.OK, progress);
            progressResult.setKeepCallback(true);
            callbackContext.sendPluginResult(progressResult);
		}
    }

	public void createSlider(CallbackContext callbackContext) {
		try {
			if (seekBarWindow!=null) {
				seekBarWindow.dismiss();
				seekBarWindow = null;
			}			

			if (seekBarWindow==null) {
		        // Initialize the view  
				LinearLayout ll = new LinearLayout(webView.getContext());        
		        ll.setLayoutParams(new LayoutParams(width, height));     
		        ll.setBackgroundColor(Color.TRANSPARENT);

		        // Initialize popup 
		        seekBarWindow = new PopupWindow(ll, width, height);    

		        // Set popup's window layout type to TYPE_TOAST     
		        Method[] methods = PopupWindow.class.getMethods();
		        for(Method m: methods){
		            if(m.getName().equals("setWindowLayoutType")) {
		                try{
		                    m.invoke(seekBarWindow, WindowManager.LayoutParams.TYPE_TOAST);
		                }catch(Exception e){
		                    e.printStackTrace();
		                }
		                break;
		            }
		        }		        

		        SeekBar seekBar = new SeekBar(webView.getContext());
		        
		        float[] outR = new float[] {6,6,6,6,6,6,6,6};  
		        ShapeDrawable thumb = new ShapeDrawable(new RoundRectShape(outR, null, null));      
		        thumb.setIntrinsicHeight(height);
		        thumb.setIntrinsicWidth(height);    
		        thumb.getPaint().setColor(Color.GRAY);
		        seekBar.setThumb(thumb);
		        seekBar.setPadding(height, 0, height, 0);

	        	AudioManager am = (AudioManager) webView.getContext().getSystemService(Context.AUDIO_SERVICE);
		    	int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				seekBar.setMax(max);

				int iVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
	    		Log.e("VolumeSlider", "setting progress value "+iVolume+"  max="+max);
				seekBar.setProgress(iVolume);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
				params.gravity = Gravity.CENTER_HORIZONTAL;
				seekBar.setLayoutParams(params);
				
				ll.addView(seekBar);

				seekBar.setOnSeekBarChangeListener(new VolumeSeekBarListener(callbackContext));
			} else {
				Log.e("VolumeSlider", "updating slider");
			}

		//	AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(width, height, originx, originy);
		//	seekBar.setLayoutParams(lp);

		} catch (Exception e) {
			Log.e("VolumeSlider", e.getMessage());
		}
	}
	
	public void setVolume(int streamVolume) {
		try {
        	AudioManager am = (AudioManager) webView.getContext().getSystemService(Context.AUDIO_SERVICE);
	    	int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    	
	    	if (Math.round(streamVolume)==0) {
	    		am.setStreamMute(AudioManager.STREAM_MUSIC, true);
	    	} else {
	    		try {
	    			for (int i=1; i<50; i++) {
	            		am.setStreamMute(AudioManager.STREAM_MUSIC, false);
	    			}
	    		} catch (Exception e) {
	    			
	    		}

		    	Log.e("VolumeSlider", "setting volume for stream "+streamVolume+" max="+max);
	        	am.setStreamVolume(AudioManager.STREAM_MUSIC, streamVolume, 0);
	    	}
		} catch (Exception e) {
			Log.e("VolumeSlider", e.getMessage());
		}
	}
}
