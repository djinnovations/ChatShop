package com.dj.infilectmission.screens;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dj.infilectmission.R;
import com.dj.infilectmission.banes.HackyViewPager;
import com.dj.infilectmission.myapp.MyApplication;
import com.dj.infilectmission.myapp.MyPreferenceManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import uk.co.senab.photoview.PhotoView;

public class FavouriteListActivity extends AppCompatActivity {

    private ImageView _ivNoFav;
    private ViewPager mViewPager;
    LinkedHashSet<String> urlSet = new LinkedHashSet<>();
    ArrayList<String> urlArrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        intializeViews();
        setUpFavList();

    }

    private void setUpFavList() {

        MyPreferenceManager mPref = MyApplication.getInstance().getPrefManager();
        ArrayList<String> temp = mPref.getUrlList();
        if (temp != null){
            urlSet.addAll(temp);
        }
        urlArrayList.addAll(urlSet);
        if (urlArrayList != null) {
            if (urlArrayList.size() == 0) {
                displayNoFavList();
            } else {
                mViewPager.setAdapter(new SamplePagerAdapter());
                mViewPager.setVisibility(View.VISIBLE);
                _ivNoFav.setVisibility(View.GONE);
            }
        } else displayNoFavList();
    }


    private void displayNoFavList() {

        Toast.makeText(getApplicationContext(), "No favourites yet!", Toast.LENGTH_SHORT).show();
        _ivNoFav.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
    }

    private void intializeViews() {

        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        //setContentView(mViewPager);
        _ivNoFav = (ImageView) findViewById(R.id.ivNoFav);
        _ivNoFav.setVisibility(View.GONE);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);

    }


    class SamplePagerAdapter extends PagerAdapter {

        private final int[] sDrawables = {R.drawable.image_1, R.drawable.image_1, R.drawable.image_1,
                R.drawable.image_1, R.drawable.image_1, R.drawable.image_1};

        @Override
        public int getCount() {
            if (urlSet != null) {
                return urlSet.size();
            } else
                return 0;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            // photoView.setImageResource(sDrawables[position]);
            setImagesFromFlickrData(photoView, position);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void setImagesFromFlickrData(ImageView imgView, int position) {

        /*final ImageView myImageView;
        if (recycled == null) {
            myImageView = (ImageView) inflater.inflate(R.layout.my_image_view, container, false);
        } else {
            myImageView = (ImageView) recycled;
        }*/

            String url = urlArrayList.get(position);
            Log.d("dj", "FavouriteListActivity - url, position: " + url + "  **&&&&&** " + position);

            Glide.with(FavouriteListActivity.this)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_spin)
                    .crossFade()
                    .into(imgView);

        }

    }


}
