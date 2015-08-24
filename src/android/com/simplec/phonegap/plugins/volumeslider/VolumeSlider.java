package com.simplec.phonegap.plugins.volumeslider;

import java.lang.reflect.Method;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
	int originx;
	int originy;
	int width;
	int height;

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
				
				createSlider();
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

	public void createSlider() {
		try {
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
				seekBar.setMax(100);
				seekBar.setProgress(50);
				ShapeDrawable thumb = new ShapeDrawable(new OvalShape());

				thumb.setIntrinsicHeight(height);
				thumb.setIntrinsicWidth(height);
				thumb.setAlpha(128);
				thumb.getPaint().setColor(Color.BLUE);
				
				seekBar.setThumb(thumb);
				seekBar.setProgress(1);
				seekBar.setProgressDrawable(new ColorDrawable(Color.BLUE));
				seekBar.setVisibility(View.VISIBLE);
			//	seekBar.setBackgroundColor(Color.BLUE);
				
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
				seekBar.setLayoutParams(params);
				
				ll.addView(seekBar);

		       /* 

				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
				seekBar.setLayoutParams(lp);

				seekBar.setVisibility(View.VISIBLE);
				oView.addView(seekBar);

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
