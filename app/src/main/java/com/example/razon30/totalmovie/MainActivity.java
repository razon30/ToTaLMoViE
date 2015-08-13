package com.example.razon30.totalmovie;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class MainActivity extends AppCompatActivity implements MaterialTabListener ,View.OnClickListener{

    //for search
    private SearchBox search;
    private Toolbar toolbar;


    //for toolbar
    //private Toolbar toolbar;
    EditText etSearch;
    Button btnSearch;
    String urlPre = "http://api.themoviedb.org/3/search/";
    String multiPost = "multi?api_key=f246d5e5105e9934d3cd4c4c181d618d&query=";
    String keyword = "";

    //for tab
    private TabLayout tabHost;

    //for viewPager
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ViewGroup containerAppBar;

    public static final int MOVIES_SEARCH_RESULTS = 0;
    public static final int MOVIES_HITS = 1;
    public static final int MOVIES_UPCOMING = 2;
   // public static final int MOVIES_REVIEWS = 3;
    public static final int TAB_COUNT = 3;

    //sorting
    private static final String TAG_SORT_NAME = "sortName";
    private static final String TAG_SORT_DATE = "sortDate";
    private static final String TAG_SORT_RATINGS = "sortRatings";


    //FLoating Button
    FloatingActionButton actionButton;
    FloatingActionMenu actionMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //for search
        search = (SearchBox) findViewById(R.id.searchbox);
        // search.enableVoiceRecognition(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.a_movie);
        toolbar.setTitle("");

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openSearch();
                return true;
            }
        });



        //toolbar setting
//        toolbar = (Toolbar) findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);
       // toolbar.setVisibility(toolbar.VISIBLE);
       // containerAppBar = (ViewGroup) findViewById(R.id.container_app_bar);
       // AnimationUtils.animateToolbar(containerAppBar);

//        etSearch = (EditText) findViewById(R.id.et_search);
//        btnSearch = (Button) findViewById(R.id.btn_search);


        //tab and viewPager setting
        tabHost = (TabLayout) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        //Notice how the Tab Layout links with the Pager Adapter
        tabHost.setTabsFromPagerAdapter(adapter);

        //Notice how The Tab Layout adn View Pager object are linked
        tabHost.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabHost));


//        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                tabHost.setSelectedNavigationItem(position);
//
//            }
//        });
//        for (int i = 0; i < adapter.getCount(); i++) {
//            tabHost.addTab(
//                    tabHost.newTab()
//                            .setText(adapter.getPageTitle(i))
//                            .setTabListener(this));
//        }


      //  setting_floating_action_button_and_menu();



//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String keyword = etSearch.getText().toString().trim();
//
//                if (keyword != null && keyword.length() != 0 && keyword != "") {
//
//                    keyword = keyword.replaceAll("\\s", "");
//
//                    String key = urlPre + multiPost + keyword;
//
//                    Intent intent = new Intent(MainActivity.this, Multi_Search_Activity.class);
//                    intent.putExtra("tv", key);
//                    startActivity(intent);
//
//                } else {
//                    Toast.makeText(MainActivity.this, "Not Proper Keyword", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//            }
//        });



    }


    private void setting_floating_action_button_and_menu() {

        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_action_refresh);
        actionButton = new FloatingActionButton
                .Builder(this)
                .setContentView(icon)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();

        SubActionButton.Builder item = new SubActionButton.Builder(this);
        item.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_sub_button_gray));

        ImageView icon_sort_name = new ImageView(this);
        icon_sort_name.setImageResource(R.drawable.ic_action_alphabets);
        ImageView icon_sort_date = new ImageView(this);
        icon_sort_date.setImageResource(R.drawable.ic_action_calendar);
        ImageView icon_sort_rating = new ImageView(this);
        icon_sort_rating.setImageResource(R.drawable.ic_action_important);

        SubActionButton button_sort_name = item.setContentView(icon_sort_name).build();
        SubActionButton button_sort_date = item.setContentView(icon_sort_date).build();
        SubActionButton button_sort_rating = item.setContentView(icon_sort_rating).build();


        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button_sort_name)
                .addSubActionView(button_sort_date)
                .addSubActionView(button_sort_rating)
                .attachTo(actionButton)
                .build();


        button_sort_name.setTag(TAG_SORT_NAME);
        button_sort_date.setTag(TAG_SORT_DATE);
        button_sort_rating.setTag(TAG_SORT_RATINGS);

        button_sort_name.setOnClickListener(this);
        button_sort_date.setOnClickListener(this);
        button_sort_rating.setOnClickListener(this);

        actionButton.setTranslationX(-200.0f);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition());
    }


    @Override
    public void onTabReselected(MaterialTab materialTab) {
    }


    @Override
    public void onTabUnselected(MaterialTab materialTab) {
    }


    @Override
    public void onClick(View v) {


        Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        if (fragment instanceof Sort) {

            if (v.getTag().equals(TAG_SORT_NAME)) {
                ((Sort) fragment).sortByName();
            }
            if (v.getTag().equals(TAG_SORT_DATE)) {
                ((Sort) fragment).sortByDate();
            }
            if (v.getTag().equals(TAG_SORT_RATINGS)) {
                ((Sort) fragment).sortByRatings();
            }
        }


    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

       // String[] tabs = getResources().getStringArray(R.array.tabs);
        int[] icon = {R.drawable.clearhistory,R.drawable.clearhistory,R.drawable.clearhistory};

        FragmentManager fragmentManager;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        public Fragment getItem(int num) {

            Fragment fragment = null;

//            L.m("getItem called for " + num);
            switch (num) {

                case MOVIES_SEARCH_RESULTS:

                    fragment = FragmentSearch.newInstance("", "");
                    break;
                case MOVIES_HITS:
                    fragment = FragmentBoxOffice.newInstance("", "");
                    break;
                case MOVIES_UPCOMING:
                    fragment = FragmentUpcoming.newInstance("", "");
                    break;
//                case MOVIES_REVIEWS:
//                    fragment = Reviews.newInstance("","");
            }
            return fragment;

        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }

        private Drawable getIcon(int position) {
            return getResources().getDrawable(icon[position]);
        }
    }


    public void openSearch() {
        toolbar.setTitle("");
        search.revealFromMenuItem(R.id.action_search, this);
//        for (int x = 0; x < 10; x++) {
//            SearchResult option = new SearchResult("Result "
//                    + Integer.toString(x), getResources().getDrawable(
//                    R.drawable.ic_history));
//            //  search.addSearchable(option);
//        }
//        search.setMenuListener(new SearchBox.MenuListener() {
//
//            @Override
//            public void onMenuClick() {
//                // Hamburger has been clicked
//                Toast.makeText(MainActivity.this, "Menu click",
//                        Toast.LENGTH_LONG).show();
//            }
//
//        });
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                // Use this to tint the screen

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                closeSearch();
            }

            @Override
            public void onSearchTermChanged() {
                // React to the search term changing
                // Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
//                Toast.makeText(MainActivity.this, searchTerm + " Searched",
//                        Toast.LENGTH_LONG).show();
               // toolbar.setTitle(searchTerm);

                if (searchTerm != null && searchTerm.length() != 0 && searchTerm != "") {

                    searchTerm = searchTerm.replaceAll("\\s", "");

                    String key = urlPre + multiPost + searchTerm;

                    Intent intent = new Intent(MainActivity.this, Multi_Search_Activity.class);
                    intent.putExtra("tv", key);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "Not Proper Keyword", Toast.LENGTH_LONG).show();
                    return;
                }

            }

            @Override
            public void onSearchCleared() {

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void closeSearch() {
        search.hideCircularly(this);
        if(search.getSearchText().isEmpty())toolbar.setTitle("");
    }



}
