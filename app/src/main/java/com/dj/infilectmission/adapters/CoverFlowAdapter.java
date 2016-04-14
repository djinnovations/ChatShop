package com.dj.infilectmission.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dj.infilectmission.R;
import com.dj.infilectmission.flickr.CoverFlowDataObject;

import java.util.ArrayList;

/**
 * Created by COMP on 4/13/2016.
 */
public class CoverFlowAdapter extends BaseAdapter {

    private ArrayList<CoverFlowDataObject> mAdapterItemsList;
    private Context mContext;

    public CoverFlowAdapter(Activity activityContext, ArrayList<CoverFlowDataObject> mAdapterItems) {

        this.mAdapterItemsList = mAdapterItems;
        this.mContext = activityContext;
    }


    public void addItems(int index, CoverFlowDataObject mcObj) {
        mAdapterItemsList.add(index, mcObj);
    }

    @Override
    public int getCount() {
        Log.d("dj", "item count: "+mAdapterItemsList.size());
        return mAdapterItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdapterItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.layout_coverflow_adapter, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.ivCoverPic = (ImageView) rowView.findViewById(R.id.ivCoverPic);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        if (holder != null) {
            int resId = mAdapterItemsList.get(position).getImgResId();
            if (resId == 0) {
                //// TODO: 4/13/2016
                setImagesFromFlickrData(holder.ivCoverPic, position);
            } else {
                holder.ivCoverPic.setImageResource(mAdapterItemsList.get(position).getImgResId());
            }

            Log.d("dj", "titles: "+mAdapterItemsList.get(position).getPicTitle());
        }

        return rowView;
    }

    public void setImagesFromFlickrData(ImageView imgView, int position) {

        /*final ImageView myImageView;
        if (recycled == null) {
            myImageView = (ImageView) inflater.inflate(R.layout.my_image_view, container, false);
        } else {
            myImageView = (ImageView) recycled;
        }*/

        String url = mAdapterItemsList.get(position).getImageUrl();
        Log.d("dj", "url, position: "+url + "  **&&&&&** " + position);

        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_spin)
                .crossFade()
                .into(imgView);

    }


    class ViewHolder {
        public ImageView ivCoverPic;
    }


}
