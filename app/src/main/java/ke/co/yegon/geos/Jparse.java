package ke.co.yegon.geos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Jparse extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = Jparse.class.getSimpleName();
    private ProgressDialog pDialog;
    SearchView searchView;

    ela listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader, listSubHeader, listScroll;
    HashMap<String, List<String>> listDataChild;
    // URL to get contacts JSON
    Button play, read, share;

    boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
//                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_parsed);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), fck.class);
                startActivity(intent);
            }
        });



        expListView = findViewById(R.id.lvExp);
        if (internet_connection()) {
            new GetContacts().execute();
        }else{
            nonet();
        }

//        expListView.setOnChildClickListener(myListItemClicked);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.pay) {
            // Handle the camera action
            //foot
            Intent intent = new Intent(getApplicationContext(), m.class);
            startActivity(intent);
        } else if (id == R.id.products) {
            //foot
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        } else if (id == R.id.hire) {
            //foot
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        } else if (id == R.id.login) {
            //foot
            Intent intent = new Intent(getApplicationContext(),l.class);
            startActivity(intent);
        } else if (id == R.id.app_share) {
            String shareUrl = "https://yegon.co.ke/survey-gis?ie=UTF-8&source=android-browser&q=download";
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, shareUrl);
            startActivity(Intent.createChooser(share, getApplicationContext().getString(R.string.share)));

        } else if (id == R.id.feedback) {
            Intent intent = new Intent(getApplicationContext(), fck.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
         protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Jparse.this);
            pDialog.setTitle("Service running");
            pDialog.setMessage("Acquiring guides...");
            pDialog.setCancelable(false);
            pDialog.show();
         }
        @Override
        protected Void doInBackground(Void... arg0) {
            hhl sh = new hhl();
            // Making a request to url and getting response
            String url = "https://yegon.pythonanywhere.com/response";
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

             if (jsonStr != null) {
                 listDataHeader = new ArrayList<String>();
                 listSubHeader = new ArrayList<String>();
                 listScroll = new ArrayList<String>();
                 listDataChild = new HashMap<String, List<String>>();
                try {

//                    JSONObject jsonObj = new JSONObject(jsonStr);
                     // Getting JSON Array node
                    JSONArray contacts = new JSONArray(jsonStr);
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        // God Sent Adding Header data or Passing values to Expanded list for display in scrollview
                        JSONObject field = c.getJSONObject("fields");
                        listDataHeader.add(field.getString("title"));
                        // God Sent
                        listSubHeader.add(field.getString("category"));
                        listScroll.add("By:\t"+field.getString("author")+"\n\n"+ field.getString("pretext" ));

                        // Adding child data for lease offer
                        List<String> lease_offer = new ArrayList<String>();
                        lease_offer.add("Author:" + field.getString("author"));
                        lease_offer.add("Source:" + field.getString("source_url"));
                        lease_offer.add(field.getString("content"));

                        // Header into Child data
                        listDataChild.put(listDataHeader.get(i), lease_offer);

                    }

                } catch (final JSONException e) {

                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }

            } else {

                 Log.e(TAG, "Couldn't get json from server.");

             }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (internet_connection()) {
                //Dismiss the progress dialog
                if (pDialog.isShowing())
                    pDialog.dismiss();
                super.onPostExecute(result);
                //call constructor
                listAdapter = new ela(getApplicationContext(), listDataHeader,listSubHeader, listScroll, listDataChild);
                // setting list adapter
                expListView.setAdapter(listAdapter);
            }
            else{
                pDialog.dismiss();
                nonet();
            }
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
                    return false;
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
    //showErrorToast
    private void showError(Exception e) {
        String message = "Error: " + e.getMessage();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private  void nonet(){
        ImageView img = findViewById(R.id.error);
        img.setImageResource(R.drawable.side_nav_bar);
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
            builder.setView(R.layout.about).setTitle("About").setIcon(R.mipmap.ic_launcher).setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }

            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        catch (Exception e){
            showError(e);
        }
    }
//DoubleBackPressed
boolean doubleback = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (doubleback){
            super.onBackPressed();
            return;
        }

        this.doubleback = true;
        Toast.makeText(getApplicationContext(),"Press again to exit",Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleback = false;
            }
        },2000);

    }


}
