package com.example.drawsample;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity
{
	canvasview CanvasView;
	ImageView IV_Pen, IV_Erase, IV_Save, IV_Load, IV_Clear;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		CanvasView = new canvasview(this);
		RelativeLayout RL_BaseView = (RelativeLayout) findViewById(R.id.RL_BaseDrawView);
		RL_BaseView.addView(CanvasView);
		IV_Pen = (ImageView) findViewById(R.id.IV_Pen);
		IV_Erase = (ImageView) findViewById(R.id.IV_Erase);
		IV_Save = (ImageView) findViewById(R.id.IV_Save);
		IV_Load = (ImageView) findViewById(R.id.IV_Load);
		IV_Clear = (ImageView) findViewById(R.id.IV_Clear);
		IV_Pen.setOnClickListener(listener);
		IV_Erase.setOnClickListener(listener);
		IV_Save.setOnClickListener(listener);
		IV_Load.setOnClickListener(listener);
		IV_Clear.setOnClickListener(listener);
	}

	private ImageView.OnClickListener listener = new ImageView.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
				case R.id.IV_Pen:
				{
					CanvasView.setMode(modeFlag.PEN);
					break;
				}
				case R.id.IV_Erase:
				{
					CanvasView.setMode(modeFlag.ERASE);
					break;
				}
				case R.id.IV_Save:
				{
					CanvasView.saveCanvas();
					break;
				}
				case R.id.IV_Load:
				{
					CanvasView.loadCanvas();
					break;
				}
				case R.id.IV_Clear:
				{
					CanvasView.clearView();
				}
			}
		}
	};
}
