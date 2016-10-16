package com.scy.scaledandroundedrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

final class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
	private static final String TAG = "RecyclerViewAdapter";

	private final List<Item> items = new ArrayList<>();

	RecyclerViewAdapter() {
		generateItems();
	}

	private void generateItems() {
		for (int i = 0; i < 128; i++) {
			items.add(new Item());
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
		private static final Random RANDOM = new Random();

		private final int color;

		Item() {
			color = RANDOM.nextInt() | 0xFF000000;
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

}
