package org.faceit.demo;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomSwipeAdapter extends PagerAdapter {
    public static ArrayList<String> listContent;
    private Context ctx;
    int[] hair = new int[1];
    private int[] image_resources = this.hair;
    private LayoutInflater layoutinflater;

    public CustomSwipeAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public int getCount() {
        return this.image_resources.length;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    public Object instantiateItem(ViewGroup container, int position) {
        this.layoutinflater = (LayoutInflater) this.ctx.getSystemService("layout_inflater");
        View item_view = this.layoutinflater.inflate(C0196R.layout.swipe_layout, container, false);
        TextView textView = (TextView) item_view.findViewById(C0196R.id.image_count);
        ((ImageView) item_view.findViewById(C0196R.id.image_view)).setImageResource(this.image_resources[position]);
        textView.setText("Hairstyle");
        container.addView(item_view);
        return item_view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
