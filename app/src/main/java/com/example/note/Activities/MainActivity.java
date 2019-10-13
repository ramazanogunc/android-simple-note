package com.example.note.Activities;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import com.example.note.Fragments.NotesFragment;
import com.example.note.Fragments.SettingsFragment;
import com.example.note.R;
import com.example.note.Ui.ThemeManager;
import com.google.android.material.navigation.NavigationView;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigation;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeManager(this).prepareTheme();
        setContentView(R.layout.activity_main);
        init();
        prepareToolbar();
        navigation.getMenu().getItem(0).setChecked(true);
        Fragment fragment = NotesFragment.CreateNotesList();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,fragment).commit();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.mainFrame);
        navigation = findViewById(R.id.mainNavigation);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigation.setNavigationItemSelectedListener(this);
    }
    private void prepareToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.navigation_notes:
                loadFragment(NotesFragment.CreateNotesList());
                return true;
            case R.id.navigation_archives:
                loadFragment(NotesFragment.CreateArchiveList());
                return true;
            case R.id.navigation_settings:
                loadFragment(new SettingsFragment());
                return true;
        }
        return false;
    }

}
