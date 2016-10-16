package com.scy.scaledandroundedrecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
	private static final String TAG = "RecyclerViewAdapter";

	private final List<Item> items = new ArrayList<>();

	RecyclerViewAdapter() {
		generateItems();
	}

	private void generateItems() {
		List<String> colorCodes = getColorCodes();
		for (int i = 0; i < 16; i++) {
			for (String colorCode : colorCodes) {
				items.add(new Item(colorCode));
			}
			Collections.reverse(colorCodes);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.update(items.get(position));
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	private static final class Item {
		private final int color;

		Item(@NonNull String colorCode) {
			color = Color.parseColor(colorCode);
		}
	}

	static final class ViewHolder extends RecyclerView.ViewHolder {
		private final ImageView imageView;

		ViewHolder(View itemView) {
			super(itemView);
			imageView = (ImageView) itemView.findViewById(R.id.imageView);
		}

		void update(Item item) {
			imageView.setImageDrawable(RoundedDrawable.fromColor(item.color));
		}
	}

	static RecyclerView.LayoutManager newLayoutManager(Context context) {
		return new MagnificationLinearLayoutManager(context) {
			private final float BASE_CORNER_RADIUS = 0;

			@Override
			protected void onScaleChanged(View child) {
				ImageView imageView = (ImageView) child.findViewById(R.id.imageView);
				RoundedDrawable drawable = (RoundedDrawable) imageView.getDrawable();

				final float childLeft = getDecoratedLeft(child);
				final float childTop = getDecoratedTop(child);
				final float childRight = getDecoratedRight(child);
				final float childBottom = getDecoratedBottom(child);
				final float childWidth = childRight - childLeft;
				final float childHeight = childBottom - childTop;

				final float midpoint = getWidth() * 0.5f;
				final float childMidpoint = (childRight + childLeft) * 0.5f;
				final float distance = Math.abs(childMidpoint - midpoint);
				final float maxRadius = Math.max(childWidth, childHeight) * 0.5f;

				if (distance < getWidth() * 0.5f) {
					float radius = maxRadius - distance / (getWidth() * 0.5f) * (maxRadius - BASE_CORNER_RADIUS);
					drawable.setCornerRadius(radius);
				} else {
					drawable.setCornerRadius(BASE_CORNER_RADIUS);
				}
			}
		};
	}

	/**
	 * Gradient color from RGB(0, 242, 235) to RGB(77, 88, 223).
	 */
	private static List<String> getColorCodes() {
		final int count = 8;
		String[] baseColorCodes = new String[count];
		for (int i = 0; i < count; i++) {
			baseColorCodes[i] = "#"
				+ String.format("%02X", (int) Math.floor(((double) 77 - 0) * i / count + 0))
				+ String.format("%02X", (int) Math.floor(((double) 88 - 242) * i / count + 242))
				+ String.format("%02X", (int) Math.floor(((double) 223 - 235) * i / count + 235));
		}
		return Arrays.asList(baseColorCodes);
	}

}
