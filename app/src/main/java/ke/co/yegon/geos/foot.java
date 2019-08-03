package ke.co.yegon.geos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;


public class foot extends AppCompatActivity{
    Button b1;
    String link;
    WebView webView;
    ProgressBar progressBar;
    SearchView searchView;
    String valFromAct1;
    boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        webView = findViewById(R.id.webView);
        webView.saveState(savedInstanceState);
        if (savedInstanceState != null) {
        webView.restoreState(savedInstanceState);
        }
        else {
            loadLink();
        }
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        valFromAct1 = getIntent().getExtras().getString("Value");
        if (valFromAct1 != null) {
            webView.loadUrl("https://yegon.pythonanywhere.com/blog?ie=UTF-8&source=android-browser&q=" + valFromAct1);
        }else{
           nonet();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // search item in action bar
        final MenuItem searchItem = menu.findItem(R.id.action_search);

            // make it visible
            searchItem.setVisible(true);

            searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {

            // listener to process query
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchItem.collapseActionView();

                    try {
                        String q = URLEncoder.encode(query, "UTF-8");
                        webView.loadUrl("https://yegon.pythonanywhere.com/blog?ie=UTF-8&source=android-browser&q=" + q);
                        Toast.makeText(foot.this,"Searching...",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        showError(e);
                        return true;
                    }

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // do nothing
                    return true;
                }


            });
            // listener to collapse action view when soft keyboard is closed
            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        searchItem.collapseActionView();
                    }
                }
            });

        }
        return true;
    }

    private  void nonet(){
        ImageView img = findViewById(R.id.error);
        img.setImageResource(android.R.drawable.stat_notify_error);
        TextView tv1 = findViewById(R.id.err);
        tv1.setText("Service requires internet connection");
        Snackbar.make(findViewById(R.id.toolbar), "Limited internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }).show();
    }


    private void showError(Exception e) {
        String message = "Error: " + e.getMessage();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    //Search
//    private void Search() {
//        String message = "Search button clicked! ";
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_about) {
            dialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //AlertDialog
    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        try {
            builder.setTitle(this.getString(R.string.action_about)).setMessage(R.string.about_text).setIcon(R.mipmap.ic_launcher).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }

            });
            builder.setNegativeButton("Changelog", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        catch (Exception e){
            showError(e);
        }
    }


    public void loadLink() {
        WebSettings webSetting = webView.getSettings();
        webSetting.setBuiltInZoomControls(false);
        webView.setWebViewClient(new WebViewClient());
        webSetting.setJavaScriptEnabled(true);
    }

    public class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

    }


}
