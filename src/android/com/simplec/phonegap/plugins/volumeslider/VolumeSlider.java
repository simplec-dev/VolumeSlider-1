package com.simplec.phonegap.plugins.volumeslider;

import java.lang.reflect.InvocationTargetException;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;

public class VolumeSlider extends CordovaPlugin {
	private static final String LOG_TAG = "VolumeSlider";
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
				Log.e(LOG_TAG, "create");
				Log.e(LOG_TAG, "creating slider execute");

				DisplayMetrics screenDensity = cordova.getActivity().getResources().getDisplayMetrics();
				Log.v(LOG_TAG, "----------------------------");
				Log.v(LOG_TAG, "webview.xdpi = "+screenDensity.xdpi);
				Log.v(LOG_TAG, "webview.ydpi = "+screenDensity.ydpi);
				Log.v(LOG_TAG, "webview.density = "+screenDensity.density);
				Log.v(LOG_TAG, "webview.densityDpi = "+screenDensity.densityDpi);
				Log.v(LOG_TAG, "webview.scaledDensity = "+screenDensity.scaledDensity);

				originx = (int)(((float)data.getInt(0)) * screenDensity.density);
				originy = (int)(((float)data.getInt(1)) * screenDensity.density);
				width = (int)(((float)data.getInt(2)) * screenDensity.density);
				height = (int)(((float)data.getInt(3)) * screenDensity.density);

				return true;
			} else if (SHOW_SLIDER.equals(action)) {
				Log.e(LOG_TAG, "show");
				if (seekBarWindow != null) {
					seekBarWindow.dismiss();
					seekBarWindow = null;
				}

				createSlider(callbackContext);

				View thisView = getWebViewFromPlugin();
				Log.e(LOG_TAG, "creating slider for view: "+thisView);
				seekBarWindow.showAtLocation(thisView, Gravity.LEFT | Gravity.TOP, originx, originy);

				return true;
			} else if (HIDE_SLIDER.equals(action)) {
				Log.e(LOG_TAG, "hide");
				if (seekBarWindow != null) {
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

			Log.v(LOG_TAG, "setting volume callback (stop) " + progress + "  max=" + seekBar.getMax());
			setVolume(progress);

			PluginResult progressResult = new PluginResult(PluginResult.Status.OK, progress);
			progressResult.setKeepCallback(true);
			callbackContext.sendPluginResult(progressResult);
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			int progress = seekBar.getProgress();

			Log.v(LOG_TAG, "setting volume callback (start) " + progress + "  max=" + seekBar.getMax());
			setVolume(progress);

			PluginResult progressResult = new PluginResult(PluginResult.Status.OK, progress);
			progressResult.setKeepCallback(true);
			callbackContext.sendPluginResult(progressResult);
		}

		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			Log.v(LOG_TAG, "setting volume callback (change) " + progress + "  max=" + seekBar.getMax());
			setVolume(progress);

			PluginResult progressResult = new PluginResult(PluginResult.Status.OK, progress);
			progressResult.setKeepCallback(true);
			callbackContext.sendPluginResult(progressResult);
		}
	}

	public void createSlider(CallbackContext callbackContext) {
		try {

			Log.v(LOG_TAG, "----------------------------");
			
			Log.v(LOG_TAG, "createSlider 1");
			if (seekBarWindow != null) {
				Log.v(LOG_TAG, "createSlider 2");
				seekBarWindow.dismiss();
				seekBarWindow = null;
			}

			if (seekBarWindow == null) {
				Log.v(LOG_TAG, "createSlider 3");
				// Initialize the view
				LinearLayout ll = new LinearLayout(webView.getContext());
				ll.setLayoutParams(new LayoutParams(width, height));
				ll.setBackgroundColor(Color.TRANSPARENT);

				// Initialize popup
				seekBarWindow = new PopupWindow(ll, width, height);

				Log.v(LOG_TAG, "createSlider 4");
				// Set popup's window layout type to TYPE_TOAST
				Method[] methods = PopupWindow.class.getMethods();
				for (Method m : methods) {
					if (m.getName().equals("setWindowLayoutType")) {
						try {
							Log.v(LOG_TAG, "createSlider 5");
							m.invoke(seekBarWindow, WindowManager.LayoutParams.TYPE_TOAST);
							Log.v(LOG_TAG, "createSlider 6");
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
				}

				SeekBar seekBar = new SeekBar(webView.getContext());

				Log.v(LOG_TAG, "createSlider 7");
				float[] outR = new float[] { 6, 6, 6, 6, 6, 6, 6, 6 };
				ShapeDrawable thumb = new ShapeDrawable(new RoundRectShape(outR, null, null));
				thumb.setIntrinsicHeight(height);
				thumb.setIntrinsicWidth(height);
				thumb.getPaint().setColor(Color.GRAY);
				seekBar.setThumb(thumb);
				seekBar.setPadding(height, 0, height, 0);
				seekBar.setVisibility(View.VISIBLE);

				Log.v(LOG_TAG, "createSlider 8");
				AudioManager am = (AudioManager) webView.getContext().getSystemService(Context.AUDIO_SERVICE);
				int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				seekBar.setMax(max);

				int iVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
				// Log.e(LOG_TAG, "setting progress value "+iVolume+"
				// max="+max);
				seekBar.setProgress(iVolume);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
				params.gravity = Gravity.CENTER_HORIZONTAL;
				seekBar.setLayoutParams(params);
				seekBar.bringToFront();

				Log.v(LOG_TAG, "createSlider 9");
				ll.addView(seekBar);
				ll.setVisibility(View.VISIBLE);

				seekBar.setOnSeekBarChangeListener(new VolumeSeekBarListener(callbackContext));
				Log.v(LOG_TAG, "createSlider 10");
			} else {
				Log.e(LOG_TAG, "updating slider");
			}

			// AbsoluteLayout.LayoutParams lp = new
			// AbsoluteLayout.LayoutParams(width, height, originx, originy);
			// seekBar.setLayoutParams(lp);

		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
		}
	}

	public void setVolume(int streamVolume) {
		try {
			AudioManager am = (AudioManager) webView.getContext().getSystemService(Context.AUDIO_SERVICE);
			int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

			if (Math.round(streamVolume) == 0) {
				am.setStreamMute(AudioManager.STREAM_MUSIC, true);
			} else {
				try {
					for (int i = 1; i < 50; i++) {
						am.setStreamMute(AudioManager.STREAM_MUSIC, false);
					}
				} catch (Exception e) {

				}

				// Log.e(LOG_TAG, "setting volume for stream
				// "+streamVolume+" max="+max);
				am.setStreamVolume(AudioManager.STREAM_MUSIC, streamVolume, 0);
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
		}
	}
    
    public WebView getWebViewFromPlugin() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	WebView thisView = null;
		boolean CORDOVA_4 = Integer.valueOf(CordovaWebView.CORDOVA_VERSION.split("\\.")[0]) >= 4;
		Log.e(LOG_TAG, "CORDOVA_4: "+CORDOVA_4);
		if (CORDOVA_4) {
			if (webView.getClass().isAssignableFrom(WebView.class)) {
				Log.e(LOG_TAG, "it is assignable");
				thisView = ((WebView) webView);
			} else {
				Log.e(LOG_TAG, "using reflection to get method getView");
				Method m = webView.getClass().getDeclaredMethod("getView", null);

				Log.e(LOG_TAG, "got method: "+m);
				thisView = (WebView) m.invoke(webView);//  webView.getView());
				Log.e(LOG_TAG, "invoked method");
			}
		} else {
			thisView = ((WebView) webView);
		}
		
		return thisView;
    }
}
