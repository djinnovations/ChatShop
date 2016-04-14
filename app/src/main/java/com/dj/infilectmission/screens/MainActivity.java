package com.dj.infilectmission.screens;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dj.infilectmission.R;
import com.dj.infilectmission.flickr.server.ServerCommunication;
import com.dj.infilectmission.myapp.ConnectionDetector;
import com.dj.infilectmission.myapp.MyApplication;
import com.dj.infilectmission.utils.CustomPair;
import com.dj.infilectmission.utils.InfilectConstants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import co.devcenter.android.ChatView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class MainActivity extends AppCompatActivity {

    private ChatView chatView;
    private RelativeLayout _child;
    private final String[] APPARELS = new String[]{"t-shirt", "tshirt", "top", "pant", "dress", "t-shirts", "tops", "pants"};
    private SmoothProgressBar _linePb;
    Dialog overLaydialog;
    private String title;
    private ConnectionDetector mConDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new_cord);

        intializeViews();
        //getFrobs();
        setUpChatView();

    }

    private void intializeViews() {

        mConDetector = MyApplication.getInstance().getmConDetector();
        _linePb = (SmoothProgressBar) findViewById(R.id.linePb);
        chatView = (ChatView) findViewById(R.id.chat_view);
        _child = (RelativeLayout) findViewById(R.id.child);
    }


    private void setUpChatView() {

        chatView.setChatListener(new ChatView.ChatListener() {
            @Override
            public void userIsTyping() {

                // do something while user is typing
            }

            @Override
            public void userHasStoppedTyping() {

                // do something when user has stopped typing
            }

            @Override
            public void onMessageReceived(String message, long timestamp) {

                // do something when chat view receives a message
                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean sendMessage(String message, long timestamp) {

                // do something when the user hits the send button
                if (mConDetector.isNetworkAvailable()){
                    /*int length = validateText(message);
                    if (length >= 1) {
                        if (length == 1) {
                            return checkStat(message);
                        } else {
                            genericInfoMsg("One item at a time", R.color.colorRed);
                            return false;
                        }
                    }*/
                    return checkStat(message);
                }else {
                    genericInfoMsg("No network connection!", R.color.colorRed);
                    return false;
                }

                //startCoverFlowActivity();
                //chatView.newMessage("My name is DJphy and I am...");

            }
        });

        chatView.setChatClickListener(new ChatView.ChatItemClickListener() {
            @Override
            public void onChatItemClicked(View view) {

                if (mConDetector.isNetworkAvailable()){
                    TextView tv = (TextView) view.findViewById(R.id.message_text_view);
                    String message = tv.getText().toString().trim();
                    title = message;
                    sendRequestToFetch(message);
                }else {
                    genericInfoMsg("No network connection!", R.color.colorRed);
                }

                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadingAction(boolean isLoading){

        if (overLaydialog == null){
            overLaydialog = floatingOverlay();
        }
        if (isLoading){
            Toast.makeText(getApplicationContext(), "loading..hang on!", Toast.LENGTH_LONG).show();
            _linePb.setVisibility(View.VISIBLE);
            overLaydialog.show();
        }else {
            _linePb.setVisibility(View.GONE);
            overLaydialog.dismiss();
        }

    }


    private Dialog floatingOverlay() {

        Dialog dialog = new Dialog(this);
        WindowManager.LayoutParams tempParams = new WindowManager.LayoutParams();
        tempParams.copyFrom(dialog.getWindow().getAttributes());

		/*tempParams.width = dialogWidthInPx;
        tempParams.height = dialogHeightInPx;*/
        tempParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        tempParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        tempParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        tempParams.dimAmount = 0.0f;

        View overLay = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_overlay, null);
        /*ProgressBar pb = (ProgressBar) overLay.findViewById(R.id.progressBarOverlay);
        pb.setIndeterminate(true);
        TextView tvTemp = (TextView) overLay.findViewById(R.id.tvOverlayInfo);
        if (infoMsg != null)
            tvTemp.setText(infoMsg);
        tvTemp.setTextColor(new ResourceReader(getActivity()).getColorFromResource(colorResId));*/
        dialog.setContentView(overLay);
        //dialog.setCancelable(false);
        dialog.getWindow().setAttributes(tempParams);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    private boolean checkStat(String message) {

        if (message.equalsIgnoreCase(APPARELS[0]) || message.equalsIgnoreCase(APPARELS[1])
                || message.equalsIgnoreCase(APPARELS[5])) {
            //sendRequestToFetch("t-shirts");
            chatView.newMessage("t-shirts");
            return true;
        } else if (message.equalsIgnoreCase(APPARELS[2]) || message.equalsIgnoreCase(APPARELS[6])) {
            //sendRequestToFetch("tops");
            chatView.newMessage("tops");
            return true;
        } else if (message.equalsIgnoreCase(APPARELS[3]) || message.equalsIgnoreCase(APPARELS[7])) {
            chatView.newMessage("pants");
            //sendRequestToFetch("pants");
            return true;
        } else if (message.equalsIgnoreCase(APPARELS[4])) {
            chatView.newMessage("dress");
            //sendRequestToFetch("dress");
            return true;
        } else {
            //genericInfoMsg("Only t-shirt, top, pant, dress are currently available!", R.color.colorRed);
            chatView.newMessage(message);
            return true;
        }
    }

    private void sendRequestToFetch(final String tag) {
        //// TODO: 4/12/2016
        loadingAction(true);
        new Thread(){
            @Override
            public void run() {
                super.run();
                ServerCommunication serverComm = ServerCommunication.getInstance();
                serverComm.getPublicPhotosUrlList(tag, mSearchListener);
            }
        }.start();
    }


    ServerCommunication.SearchPhotosListener mSearchListener = new ServerCommunication.SearchPhotosListener() {
        @Override
        public void onPhotoSearchFinish(final ArrayList<CustomPair> mList) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    loadingAction(false);
                    startCoverFlowActivity(mList);
                }
            });
        }
    };


    private void startCoverFlowActivity(ArrayList<CustomPair> mList) {

        Intent intent = new Intent(this, CarouselInterfaceActivity.class);
        intent.putParcelableArrayListExtra(InfilectConstants.KEY_PARCEL_PHOTOS, mList);
        intent.putExtra(InfilectConstants.KEY_PARCEL_TITLE, title);
        startActivity(intent);
    }


    private int validateText(String input) {

        String[] textArr = input.split(" ");
        int size = textArr.length;
        return size;
    }


    private void genericInfoMsg(String message, int colorResId) {

        String errorMsg = message;
        Snackbar tempBar = Snackbar.make(chatView, errorMsg, Snackbar.LENGTH_LONG);
        setSnackBarTextColor(tempBar, colorResId);
        assignViewToNormalPos(tempBar);
        tempBar.show();
    }


    private Snackbar setSnackBarTextColor(Snackbar tempSnBar, int colorResId) {

        View sbView = tempSnBar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(MyApplication.getInstance().getResourceReader().getColorFromResource(colorResId));
        return tempSnBar;
    }


    private void assignViewToNormalPos(Snackbar mSnackBar) {

        Snackbar.Callback callback = new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event == Snackbar.Callback.DISMISS_EVENT_SWIPE) {
                    _child.setTranslationY(0);
                    return;
                } else {
                    super.onDismissed(snackbar, event);
                }

            }
        };

        mSnackBar.setCallback(callback);
    }












    /*public void getFrobs() {

        //final Handler mUiHandler = new Handler();
        new Thread() {

            public void run() {

                //final ProgressDialog mPgd = null;
                *//*mUiHandler.post(new Runnable() {
                    public void run() {

                        mPgd = new ProgressDialog(mContext);
                        mPgd.setTitle(mContext.getResources().getString(R.string.server_comm_text));
                        mPgd.show();
                    }
                });*//*

                String user_id_label = "user_id=";
                String name_label = "name=";
                String email_label = "email=";
                String platform_label = "platform=";
                String ampersand = "&";

                Uri.Builder checkUserUri = Uri.parse(InfilectConstants.ENDPOINT).buildUpon();

                checkUserUri.appendQueryParameter(ServerKeys.KEY_METHOD, ServerKeys.REQUEST_METHOD_PUBLIC_PICS);
                checkUserUri.appendQueryParameter(ServerKeys.KEY_APIKEY, InfilectConstants.FLICKR_API_KEY);
                checkUserUri.appendQueryParameter(ServerKeys.KEY_USER_ID, InfilectConstants.FLICKR_USER_ID);
                *//*checkUserUri.appendQueryParameter(ServerKeys.KEY_APISIG, getMd5HashedString(InfilectConstants.FLICKR_API_SECRET
                +ServerKeys.KEY_APIKEY+InfilectConstants.FLICKR_API_KEY
                +"format"+"json"+ServerKeys.KEY_METHOD+ServerKeys.REQUEST_METHOD_FROB));*//*
                checkUserUri.appendQueryParameter("format", "json");
                //checkUserUri.appendQueryParameter(ServerJsonKeys.PLATFORM_KEY, getEncodedString(platform));

                String queryUrl = checkUserUri.build().toString();
                JSONObject userCheckJsonResponse = exceuteGetRequest(queryUrl);
                if (userCheckJsonResponse != null)
                    Log.d("dj", "check user method: " + userCheckJsonResponse.toString());

            }

        }.start();
    }*/




    public static final String getMd5HashedString(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            Log.d("dj", "MD5 hashed value : "+hexString.toString());
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);*/

        switch (item.getItemId()) {
            case R.id.fav_list: startFavActivity();
                return true;
            case android.R.id.home: finish();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startFavActivity() {

        Intent intent = new Intent(this, FavouriteListActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
