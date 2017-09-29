package com.vnk.smartcity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.vnk.smartcity.officer.OfficerHomeFragment;
import com.vnk.smartcity.officer.UpdateComMainFragment;
import com.vnk.smartcity.officer.ViewComplaintsOfficerFragment;

import static com.vnk.smartcity.R.id.imageView;

public class OfficerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        Fragment fragment = new OfficerHomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_officer, fragment);
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //Adding Username in Navigation HEADER
        TextView textViewUsername = (TextView) findViewById(R.id.tv_username);
        TextView textViewEmail = (TextView) findViewById(R.id.textViewEma);
        SharedPreferences sp = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        textViewUsername.setText(sp.getString(Config.KEY_USER_NAME, null));
        String emailid = sp.getString(Config.USER_EMAIL_SHARED_PREF, null);
        textViewEmail.setText(emailid);


        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .withBorder(5)
                .bold().fontSize(80)
                .width(100)  // width in px
                .height(100) // height in px
                .endConfig()
                .buildRound(String.valueOf(emailid.charAt(0)), (getResources().getColor(R.color.colorPrimary))); ///Dj update here to take dynamic letter


        ImageView image = (ImageView) findViewById(imageView);
        image.setImageDrawable(drawable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id) {
        Fragment fragment = null;
        if (id == R.id.n_home) {
            fragment = new OfficerHomeFragment();
        } else if (id == R.id.n_complaints) {
            fragment = new ViewComplaintsOfficerFragment();

        } else if (id == R.id.n_update_status) {
            fragment = new UpdateComMainFragment();

        } else if (id == R.id.n_profile) {

        } else if (id == R.id.n_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Smart City Pune.\n App to easily solve basic PMC problems.\nLink: http://github.com/vnkdj5");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.n_logout) {
            logout();
        }


        if (fragment != null) {
           /* Bundle bundle = new Bundle();
            bundle.putInt("spinner_id", 0);
            fragment.setArguments(bundle);*/
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                    .add(fragment, "frag1")
                    .addToBackStack("frag");
            ft.replace(R.id.content_officer, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(item.getItemId());

        return true;
    }

    public void shareApp(View v) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Smart City Pune.\n App to easily solve basic PMC problems.\nLink: http://github.com/vnkdj5");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    //Logout function
    public void logout() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to username
                        editor.putString(Config.USER_EMAIL_SHARED_PREF, "");
                        editor.putString(Config.USER_TYPE, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(OfficerMainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
