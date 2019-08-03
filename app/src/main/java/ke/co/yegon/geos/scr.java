package ke.co.yegon.geos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

public class scr extends AppCompatActivity {

    String valFromEla,valFromElaC;
    TextView scr_content;
    VideoView videoView;
    WebView bs_web;
    private AlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        valFromEla = getIntent().getExtras().getString("Header");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(valFromEla);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_media_play);

        valFromElaC = getIntent().getExtras().getString("Content");
        scr_content = findViewById(R.id.scr_content);
        scr_content.setText(valFromElaC);
//        scr_content.setText(getString(R.string.large_text));
        View bottomSheet = findViewById(R.id.design_bottom_sheet);
        TextView tv1 = findViewById(R.id.bs_text);
        tv1.setText(valFromEla);

        //BotttomSheet
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        //Write your Logic here
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        //Write your Logic here
                        //Dismiss the progress dialog

                        fab.setImageResource(android.R.drawable.ic_media_play);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        //Write your Logic here
                        videoView = findViewById(R.id.bs_video);
                        videoView.setVideoURI(Uri.parse("http://techslides.com/demos/sample-videos/small.3gp"));
                        videoView.setMediaController(new MediaController(scr.this));
                        videoView.requestFocus();
                        videoView.start();

//                        //VideoWebView
//                        String vimeoVideo = "<html><body><iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/sa-TUpSx1JA?rel=0\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body></html>" ;
//                        WebView webView = findViewById
//                                (R.id.bs_web);
//                        webView.setWebViewClient( new WebViewClient() {
//                            @Override
//                            public boolean shouldOverrideUrlLoading (WebView
//                                                                             webView, WebResourceRequest request) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                    webView.loadUrl(request.getUrl().toString());
//                                }
//                                return true ;
//                            }
//                        });
//                        WebSettings webSettings = webView.getSettings();
//                        webSettings.setJavaScriptEnabled( true );
//                        webView.loadData(vimeoVideo, "text/html" , "utf-8" );

                        final  ProgressBar bs_pr = findViewById(R.id.bs_progress);
                        bs_pr.setVisibility(View.VISIBLE);
                        fab.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                                return true;
                            }
                        });
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        //Write your Logic here
                        videoView.setVideoURI(Uri.parse(""));
                        // Showing progress dialog
                        pDialog = new ProgressDialog(scr.this);
                        pDialog.setMessage("Stopping video");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (pDialog.isShowing())
                                    pDialog.dismiss();
                            }
                        },500);
                        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                                return true;
                            }
                        });
                        fab.setImageResource(android.R.drawable.ic_media_play);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        //Write your Logic here
                        videoView.setVideoURI(Uri.parse(""));
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //Write your Logic here
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        //Tooltip
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
