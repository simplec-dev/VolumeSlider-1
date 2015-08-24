package com.simplec.phonegap.plugins.volumeslider;

import java.lang.reflect.Method;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class VolumeSlider extends CordovaPlugin {
	private static final String CREATE_SLIDER = "createVolumeSlider";
	private static final String SHOW_SLIDER = "showVolumeSlider";
	private static final String HIDE_SLIDER = "hideVolumeSlider";
	
	private PopupWindow seekBar = null;

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
				createSlider(data);
				return true;
			} else if (SHOW_SLIDER.equals(action)) {
				Log.e("VolmeSlider", "show slider execute");
				if (seekBar!=null) {
					Log.e("VolmeSlider", "setting visible");
				//	seekBar.setVisibility(View.VISIBLE);
				}
				
				return true;
			} else if (HIDE_SLIDER.equals(action)) {
				Log.e("VolmeSlider", "hide slider execute");
				if (seekBar!=null) {
					Log.e("VolmeSlider", "setting invisible");
				//	seekBar.setVisibility(View.INVISIBLE);
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

	public void createSlider(JSONArray data) {
		try {
			int originx = data.getInt(0);
			int originy = data.getInt(1);
			int width = data.getInt(2);
			int height = data.getInt(3);

			Log.e("VolumeSlider", "originx: "+originx);
			Log.e("VolumeSlider", "originy: "+originy);
			Log.e("VolumeSlider", "width: "+width);
			Log.e("VolumeSlider", "height: "+height);
			
			
			if (seekBar==null) {

		        // Initialize the view  
				LinearLayout ll = new LinearLayout(webView.getContext());        
		        ll.setLayoutParams(new LayoutParams(width, height));     
		        ll.setBackgroundColor(Color.BLUE);

		        // Initialize popup 
		        seekBar = new PopupWindow(ll, width, height);     
		        seekBar.showAtLocation(webView, Gravity.LEFT | Gravity.TOP, originx, originy);

		        // Set popup's window layout type to TYPE_TOAST     
		        Method[] methods = PopupWindow.class.getMethods();
		        for(Method m: methods){
		            if(m.getName().equals("setWindowLayoutType")) {
		                try{
		                    m.invoke(seekBar, WindowManager.LayoutParams.TYPE_TOAST);
		                }catch(Exception e){
		                    e.printStackTrace();
		                }
		                break;
		            }
		        }       
		       /* 
		        AbsoluteLayout oView = new AbsoluteLayout(webView.getContext()); 
				oView.setVisibility(View.VISIBLE);
		        wm.addView(oView, params);
		        
				seekBar = new SeekBar(oView.getContext());
				seekBar.setMax(100);
				// seekBar.setIndeterminate(true);

				ShapeDrawable thumb = new ShapeDrawable(new OvalShape());

				thumb.setIntrinsicHeight(height);
				thumb.setIntrinsicWidth(height/2);
				seekBar.setThumb(thumb);
				seekBar.setProgress(1);
				seekBar.setVisibility(View.INVISIBLE);
				seekBar.setBackgroundColor(Color.BLUE);

				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
				seekBar.setLayoutParams(lp);

				seekBar.setVisibility(View.VISIBLE);
				oView.addView(seekBar);
				
				cordova.getActivity().setContentView(oView, params);

				seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					public void onStopTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
						System.out.println(".....111.......");

					}

					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
						System.out.println(".....222.......");
					}

					public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
						// TODO Auto-generated method stub
						System.out.println(".....333......." + arg1);
					}
				});*/
				Log.e("VolumeSlider", "created slider");
			} else {
				Log.e("VolumeSlider", "updating slider");
			}

		//	AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(width, height, originx, originy);
		//	seekBar.setLayoutParams(lp);

		} catch (Exception e) {
			Log.e("VolumeSlider", e.getMessage());
		}
	}
}
