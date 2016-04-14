package com.dj.infilectmission.screens;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dj.infilectmission.R;
import com.dj.infilectmission.adapters.CoverFlowAdapter;
import com.dj.infilectmission.flickr.CoverFlowDataObject;
import com.dj.infilectmission.myapp.MyApplication;
import com.dj.infilectmission.myapp.MyPreferenceManager;
import com.dj.infilectmission.utils.CustomPair;
import com.dj.infilectmission.utils.InfilectConstants;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class CarouselInterfaceActivity extends AppCompatActivity {

    private FeatureCoverFlow mCoverFlow;
    private CoverFlowAdapter mCoverFlowAdapter;
    private int[] imageResArr = new int[]{R.drawable.image_1, R.drawable.image_2, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4,
            R.drawable.image_1, R.drawable.image_2};

    private MaterialFavoriteButton _mfFav;
    private MaterialFavoriteButton _mfTag;
    private ImageView _ivComment;
    private ArrayList<CoverFlowDataObject> listOfFlickrObj;
    private LinkedHashSet<String> mUrlSet = new LinkedHashSet<>();

    private MyPreferenceManager mPrefManger;
    private View _favBar;
    private View _layTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel_interface);

        ArrayList<CustomPair> mCustomList = getIntent().getParcelableArrayListExtra(InfilectConstants.KEY_PARCEL_PHOTOS);
        String title = getIntent().getStringExtra(InfilectConstants.KEY_PARCEL_TITLE);
        if (title != null) {
            getSupportActionBar().setTitle(title);
        }
        intializeViews(mCustomList);
        setListener();
    }

    private void setListener() {

        _mfFav.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {

                maintainFavList(favorite);
            }
        });

        _mfTag.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {

                if (favorite)
                    Toast.makeText(getApplicationContext(), "Under construction!", Toast.LENGTH_SHORT).show();
            }
        });

        _ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Under construction!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void maintainFavList(boolean favorite) {

        String url = listOfFlickrObj.get(mCoverFlow.getScrollPosition()).getImageUrl();
        if (favorite) {
            boolean status = mUrlSet.add(url);
            if (status) {
                Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Already added", Toast.LENGTH_SHORT).show();
            }

        } else {
            boolean status = mUrlSet.remove(url);
            if (status) {
                Toast.makeText(getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "This item is not in your favorites", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void intializeViews(ArrayList<CustomPair> mCustomList) {

        _favBar = findViewById(R.id.favBar);
        _layTitle = findViewById(R.id.layTitle);
        _layTitle.setVisibility(View.GONE);
        _layTitle.setAlpha(0.3f);
        mPrefManger = MyApplication.getInstance().getPrefManager();
        _mfFav = (MaterialFavoriteButton) findViewById(R.id.mfFav);
        _mfTag = (MaterialFavoriteButton) findViewById(R.id.mfTag);
        _ivComment = (ImageView) findViewById(R.id.ivComment);
        mCoverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
        mCoverFlowAdapter = new CoverFlowAdapter(this, getDataForCoverFlow(mCustomList));
        mCoverFlow.setAdapter(mCoverFlowAdapter);

        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO CoverFlow item clicked
                showTitle(position);
            }
        });

        mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                //TODO CoverFlow stopped to position
                checkFavStat(position);
            }

            @Override
            public void onScrolling() {
                //TODO CoverFlow began scrolling
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showTitle(int position) {

        _layTitle.clearAnimation();
        ((TextView)_layTitle.findViewById(R.id.tvTitle)).setText(listOfFlickrObj.get(position).getPicTitle());
        _layTitle.setVisibility(View.VISIBLE);
        _layTitle.setAlpha(0.3f);
        try {
            startAnim(_layTitle, R.anim.anim_dialog_fall_down);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    startAnim(_layTitle, R.anim.anim_dialog_go_up);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            _layTitle.setVisibility(View.GONE);
                            _layTitle.setAlpha(0.3f);
                        }
                    }, 600);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000);

    }


    private void startAnim(View view, int animResID) throws Exception {

        Animation anim = AnimationUtils.loadAnimation(getBaseContext(), animResID);
        view.startAnimation(anim);
    }


    private void checkFavStat(int position) {
        String url = listOfFlickrObj.get(position).getImageUrl();
        if (mUrlSet.contains(url)) {
            _mfFav.setFavorite(true);
        } else _mfFav.setFavorite(false);
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


    public ArrayList<CoverFlowDataObject> getDataForCoverFlow(ArrayList<CustomPair> mCustomList) {

        ArrayList<CoverFlowDataObject> mList = new ArrayList<>();

        if (mCustomList != null) {
            if (mCustomList.size() == 0) {
                mList = getDefaultData();
            } else {
                for (int i = 0; i < mCustomList.size(); i++) {
                    _favBar.setVisibility(View.VISIBLE);
                    mList.add(new CoverFlowDataObject(mCustomList.get(i).getTitle(), mCustomList.get(i).getUrl()));
                }
            }

        } else {
            mList = getDefaultData();
        }
        listOfFlickrObj = mList;
        return mList;
    }


    private ArrayList<CoverFlowDataObject> getDefaultData() {

        _favBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Zero (0) results found", Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "Setting up default images", Toast.LENGTH_LONG).show();
        ArrayList<CoverFlowDataObject> mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            mList.add(new CoverFlowDataObject(null, null, imageResArr[i]));
        }
        return mList;
    }


    @Override
    protected void onDestroy() {
        addToFavList();
        super.onDestroy();
    }

    private void addToFavList() {

        for (String url : mUrlSet) {
            mPrefManger.addUrlToPref(url);
        }
    }
}
