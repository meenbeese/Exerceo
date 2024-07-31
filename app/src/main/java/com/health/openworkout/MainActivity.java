package com.health.openworkout;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.health.openworkout.core.OpenWorkout;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_main_preferences, R.id.nav_trainings)
                .setDrawerLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_help:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/oliexdev/openWorkout")));
                        break;
                    case R.id.nav_about:
                        showAboutDialog();
                        break;
                }

                NavigationUI.onNavDestinationSelected(menuItem, navController);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        OpenWorkout.getInstance().initTrainingPlans();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showAboutDialog() {
        final SpannableString aboutMsg = new SpannableString(getResources().getString(R.string.label_about_info));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.app_name) + " " + String.format("v%s (%d)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE))
                .setMessage(aboutMsg)
                .setIcon(R.drawable.ic_openworkout)
                .setPositiveButton(getResources().getString(R.string.label_ok), null)
                .create();

        dialog.show();
    }

}
