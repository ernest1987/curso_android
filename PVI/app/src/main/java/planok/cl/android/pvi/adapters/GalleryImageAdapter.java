package planok.cl.android.pvi.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import planok.cl.android.pvi.R;

/**
 * Created by Jaime Perez Varas on 29-07-2016.
 */
public class GalleryImageAdapter extends BaseAdapter {
    private Context mContext;


    public GalleryImageAdapter(Context context) {
        mContext = context;
    }

    public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);

        i.setImageResource(mImageIds[index]);
        i.setLayoutParams(new Gallery.LayoutParams(200, 200));

        i.setScaleType(ImageView.ScaleType.FIT_XY);


        return i;
    }

    public Integer[] mImageIds = {
            R.drawable.camera_black,
            R.drawable.chevron_circle_down_green,
            R.drawable.comment_black,
            R.drawable.minus_square,
            R.drawable.comment_green,
            R.drawable.cumple_des_green,
            R.drawable.chevron_circle_down_red,
            R.drawable.logo_plan_ok
    };

}