package com.simplec.phonegap.plugins.volumeslider;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class VolumeSlider extends CordovaPlugin {
	private static final String CREATE_SLIDER = "createVolumeSlider";
	private static final String SHOW_SLIDER = "showVolumeSlider";
	private static final String HIDE_SLIDER = "hideVolumeSlider";
	
	private SeekBar seekBar = null;

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
					seekBar.setVisibility(View.VISIBLE);
				}
				
				return true;
			} else if (HIDE_SLIDER.equals(action)) {
				Log.e("VolmeSlider", "hide slider execute");
				if (seekBar!=null) {
					Log.e("VolmeSlider", "setting invisible");
					seekBar.setVisibility(View.INVISIBLE);
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

			if (seekBar==null) {
		        WindowManager wm = (WindowManager) webView.getContext().getSystemService(Context.WINDOW_SERVICE);
		        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
		        		wm.getDefaultDisplay().getWidth(),
		        		wm.getDefaultDisplay().getHeight(),
		                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
		                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
		                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
		                PixelFormat.TRANSLUCENT);
		        params.gravity = Gravity.LEFT | Gravity.TOP;

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

				AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(width, height, originx, originy);
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
				});
				Log.e("VolumeSlider", "created slider");
			} else {
				Log.e("VolumeSlider", "updating slider");
			}

			AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(width, height, originx, originy);
			seekBar.setLayoutParams(lp);

		} catch (Exception e) {
			Log.e("VolumeSlider", e.getMessage());
		}
	}
}
