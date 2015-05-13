package com.example.drawsample;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class canvasview extends View
{
	private Path drawPath;
	private Paint drawPaint, canvasPaint;
	private Canvas drawCanvas;
	private Bitmap canvasBitmap;
	private float mX, mY;
	private int NowModeFlag = modeFlag.PEN;
	private Context context;
	private boolean isFirst = true;

	public canvasview(Context context)
	{
		super(context);
		this.context = context;
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(50);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		drawPaint.setColor(Color.GREEN);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}

	public void setMode(int mode)
	{
		switch (mode)
		{
			case modeFlag.PEN:
			{
				drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
				break;
			}
			case modeFlag.ERASE:
			{
				drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
				break;
			}
		}
	}

	public void saveCanvas()
	{
		AlertDialog.Builder saveDialog = new AlertDialog.Builder(this.context);
		saveDialog.setTitle("Save drawing");
		saveDialog.setMessage("Save drawing to device Gallery?");
		saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				// save drawing
				try
				{
					// String fileName = Environment.getExternalStorageDirectory() + "/Pictures/"+ UUID.randomUUID().toString()+".png";
					String fileName = Environment.getExternalStorageDirectory() + "/Pictures/test.png";
					OutputStream stream = new FileOutputStream(fileName);
					canvasBitmap.compress(CompressFormat.PNG, 100, stream);
					stream.close();
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				Toast.makeText(context, "Save successful!", Toast.LENGTH_LONG).show();
			}
		});
		saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});
		saveDialog.show();
	}

	public void loadCanvas()
	{
		canvasBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Pictures/test.png").copy(Bitmap.Config.ARGB_8888, true);
		drawCanvas = new Canvas(canvasBitmap);
		invalidate();
		Toast.makeText(context, "Load successful!", Toast.LENGTH_LONG).show();

	}

	public void clearView()
	{
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		isFirst = true;
		invalidate();
		Toast.makeText(context, "Clear All!", Toast.LENGTH_LONG).show();

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (isFirst)
		{
			isFirst = !isFirst;
			canvas.drawColor(Color.TRANSPARENT);
		}
		canvas.drawPath(drawPath, drawPaint);
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float touchX = event.getX();
		float touchY = event.getY();
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				touch_start(touchX, touchY);
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(touchX, touchY);
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				break;
			default:
				return false;
		}
		invalidate();
		return true;
	}

	private void touch_move(float x, float y)
	{
		if (NowModeFlag == modeFlag.ERASE)
		{
			drawPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
		}
		drawPath.lineTo(mX, mY);
		drawCanvas.drawPath(drawPath, drawPaint);
		drawPath.reset();
		drawPath.moveTo(mX, mY);
		mX = x;
		mY = y;
	}

	private void touch_start(float x, float y)
	{
		drawPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_up()
	{
		drawPath.reset();
	}
}
