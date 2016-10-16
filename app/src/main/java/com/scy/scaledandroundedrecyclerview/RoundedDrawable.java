package com.scy.scaledandroundedrecyclerview;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * Inspired from com.makeramen.roundedimageview.RoundedDrawable
 * https://github.com/vinc3m1/RoundedImageView
 */
public final class RoundedDrawable extends Drawable {
	private static final String TAG = "RoundedDrawable";

	private final RectF bounds = new RectF();

	private final int bitmapWidth;
	private final int bitmapHeight;
	private final RectF bitmapRect = new RectF();

	private final Paint bitmapPaint;
	private final BitmapShader bitmapShader;
	private final Matrix shaderMatrix = new Matrix();

	private float cornerRadius;

	public RoundedDrawable(Bitmap bitmap) {
		bitmapWidth = bitmap.getWidth();
		bitmapHeight = bitmap.getHeight();
		bitmapRect.set(0, 0, bitmapWidth, bitmapHeight);

		bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		bitmapPaint = new Paint();
		bitmapPaint.setStyle(Paint.Style.FILL);
		bitmapPaint.setAntiAlias(true);
		bitmapPaint.setShader(bitmapShader);
	}

	public static RoundedDrawable fromColor(int color) {
		Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(color);
		return new RoundedDrawable(bitmap);
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, bitmapPaint);
	}

	@Override
	public void setAlpha(int alpha) {
		bitmapPaint.setAlpha(alpha);
		invalidateSelf();
	}

	@Override
	public void setColorFilter(ColorFilter colorFilter) {
		bitmapPaint.setColorFilter(colorFilter);
		invalidateSelf();
	}

	@Override
	public void setDither(boolean dither) {
		bitmapPaint.setDither(dither);
		invalidateSelf();
	}

	@Override
	public void setFilterBitmap(boolean filter) {
		bitmapPaint.setFilterBitmap(filter);
		invalidateSelf();
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public int getIntrinsicWidth() {
		return bitmapWidth;
	}

	@Override
	public int getIntrinsicHeight() {
		return bitmapHeight;
	}

	@Override
	protected void onBoundsChange(@NonNull Rect bounds) {
		super.onBoundsChange(bounds);

		this.bounds.set(bounds);
		updateShaderMatrix();
	}

	private void updateShaderMatrix() {
		shaderMatrix.reset();
		shaderMatrix.setRectToRect(bitmapRect, bounds, Matrix.ScaleToFit.FILL);
		bitmapShader.setLocalMatrix(shaderMatrix);
	}

	public float getCornerRadius() {
		return cornerRadius;
	}

	public void setCornerRadius(float radius) {
		cornerRadius = radius;
		invalidateSelf();
	}

}
