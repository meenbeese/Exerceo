package com.health.openworkout

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI

import com.google.android.material.navigation.NavigationView
import com.health.openworkout.core.OpenWorkout


class MainActivity : AppCompatActivity() {
    private var mAppBarConfiguration: AppBarConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration
            .Builder(R.id.nav_home, R.id.nav_main_preferences, R.id.nav_trainings)
            .setDrawerLayout(drawer)
            .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView, navController)

        navigationView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.nav_help -> startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/oliexdev/openWorkout")
                    )
                )

                R.id.nav_about -> showAboutDialog()
            }
            NavigationUI.onNavDestinationSelected(menuItem, navController)
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        OpenWorkout.getInstance().initTrainingPlans()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    private fun showAboutDialog() {
        val aboutMsg = SpannableString(resources.getString(R.string.label_about_info))

        val dialog = AlertDialog.Builder(this)
            .setTitle(
                resources.getString(R.string.app_name) + " " + String.format(
                    "v%s (%d)",
                    BuildConfig.VERSION_NAME,
                    BuildConfig.VERSION_CODE
                )
            )
            .setMessage(aboutMsg)
            .setIcon(R.drawable.ic_openworkout)
            .setPositiveButton(resources.getString(R.string.label_ok), null)
            .create()

        dialog.show()
    }
}
