package com.scy.scaledandroundedrecyclerview;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Zoom central image recycler view
 * http://stackoverflow.com/a/35311728
 */
public class MagnificationLinearLayoutManager extends LinearLayoutManager {
	private static final String TAG = "MagnificationLinearLayoutManager";

	// Shrink the cards around the center up to "mShrinkAmount"
	private float mShrinkAmount = 0.4f;
	// The cards will be at "mShrinkAmount" when they are "mShrinkDistance" of the way between the center and the edge.
	private float mShrinkDistance = 1.f;

	public MagnificationLinearLayoutManager(Context context) {
		super(context, HORIZONTAL, false);
	}

	public MagnificationLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
		super(context, orientation, reverseLayout);
	}

	public MagnificationLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void setOrientation(int orientation) {
		super.setOrientation(orientation);
		checkOrientation(orientation);
	}

	private static void checkOrientation(int orientation) {
		if (orientation != HORIZONTAL) {
			throw new IllegalArgumentException("Unsupported orientation!!! orientation=" + orientation);
		}
	}

	public void setShrinkAmount(float shrinkAmount) {
		mShrinkAmount = shrinkAmount;
	}

	public void setShrinkDistance(float shrinkDistance) {
		mShrinkDistance = shrinkDistance;
	}

	private void calculateTransition() {
		final float midpoint = getWidth() * 0.5f;
		final float d0 = 0.f;
		final float d1 = mShrinkDistance * midpoint;
		final float s0 = 1.f;
		final float s1 = 1.f - mShrinkAmount;

		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			float childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) * 0.5f;
			float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
			float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
			child.setScaleX(scale);
			child.setScaleY(scale);

			onScaleChanged(child);
		}
	}

	protected void onScaleChanged(View child) {
	}

	@CallSuper
	@Override
	public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
		int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
		calculateTransition();
		return scrolled;
	}

	@CallSuper
	@Override
	public void onLayoutCompleted(RecyclerView.State state) {
		super.onLayoutCompleted(state);
		calculateTransition();
	}

}

