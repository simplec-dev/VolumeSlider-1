package com.simplec.phonegap.plugins.volumeslider;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class VolumeSlider extends CordovaPlugin {
	private static final String CREATE_SLIDER = "createVolumeSlider";
	private static final String SHOW_SLIDER = "showVolumeSlider";
	private static final String HIDE_SLIDER = "hideVolumeSlider";

	@Override
	public boolean execute(String action, JSONArray data, final CallbackContext callbackContext) throws JSONException {
		try {
			if (CREATE_SLIDER.equals(action)) {
				return true;

			} else if (SHOW_SLIDER.equals(action)) {
				return true;

			} else if (HIDE_SLIDER.equals(action)) {
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

			SeekBar seekBar = new SeekBar(webView.getContext());
			seekBar.setMax(100);
			// seekBar.setIndeterminate(true);

			ShapeDrawable thumb = new ShapeDrawable(new OvalShape());

			thumb.setIntrinsicHeight(height);
			thumb.setIntrinsicWidth(height/2);
			seekBar.setThumb(thumb);
			seekBar.setProgress(1);
			seekBar.setVisibility(View.VISIBLE);
			seekBar.setBackgroundColor(Color.BLUE);

			AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(width, height, originx, originy);
			seekBar.setLayoutParams(lp);
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
		} catch (Exception e) {

		}
	}
}
