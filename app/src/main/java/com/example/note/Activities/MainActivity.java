package com.example.note.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import com.example.note.R;
import com.example.note.Ui.ThemeManager;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigation;
    DrawerLayout drawerLayout;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeManager(this).prepareTheme();
        setContentView(R.layout.activity_main);
        init();
        prepareToolbar();
        prepareNavigation();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.mainFrame);
        navigation = findViewById(R.id.mainNavigation);
        drawerLayout = findViewById(R.id.drawerLayout);
    }
    private void prepareToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void prepareNavigation(){
        navController = Navigation.findNavController(this, R.id.mainFrame);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigation, navController);
        navigation.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.mainFrame),
                drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawers();
        return NavigationUI.onNavDestinationSelected(menuItem,navController) || super.onOptionsItemSelected(menuItem);
    }

}