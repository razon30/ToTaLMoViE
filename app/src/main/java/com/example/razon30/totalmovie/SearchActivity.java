package com.example.razon30.totalmovie;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.github.mrengineer13.snackbar.SnackBar;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    SearchBox search;
    ArrayList<String> searchList;
    String urlPre = "http://api.themoviedb.org/3/search/";
    String multiPost = "multi?api_key=f246d5e5105e9934d3cd4c4c181d618d&query=";
    DBMovies dbMovies;

    ImageView imageView;
    MyTextViewTwo textViewTwo;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = (SearchBox) findViewById(R.id.searchbox);
        imageView = (ImageView) findViewById(R.id.imageSearch);
        textViewTwo = (MyTextViewTwo) findViewById(R.id.textSearch);
        cardView = (CardView) findViewById(R.id.cardSearch);
        cardView.setVisibility(View.GONE);

        searchList = new ArrayList<>();

        dbMovies = new DBMovies(this);
        searchList = dbMovies.getAllSearchKey();

        if (searchList.size() != 0 && !searchList.isEmpty()) {
            Set<String> set = new HashSet<String>();
            set.addAll(searchList);
            searchList.clear();
            searchList.addAll(set);
        } else {
            textViewTwo.setText("No Search History Available");
            imageView.setImageResource(R.drawable.ic_action_warning);
        }

        textViewTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchList.size() != 0 && !searchList.isEmpty()) {
                    dbMovies.deleateAll_SearchKey();
                    new SnackBar.Builder(SearchActivity.this)
                            .withMessage("History Cleared") // OR
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
                    search.clearSearchable();
                    search.clearResults();
                    textViewTwo.setText("No Search History Available");
                    imageView.setImageResource(R.drawable.ic_action_warning);
                } else {
                    new SnackBar.Builder(SearchActivity.this)
                            .withMessage("No Search history Available to clear") // OR
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
                }
            }
        });


        openSearch();
    }

    private void openSearch() {
        //  toolbar.setTitle("");
        search.revealFromMenuItem(R.id.action_search, this);

        if (!searchList.isEmpty() && searchList.size() != 0) {
            for (int i = 0; i < searchList.size(); i++) {
                SearchResult options = new SearchResult(searchList.get(i), getResources().getDrawable(R
                        .drawable.abc_ic_go_search_api_mtrl_alpha));
                search.addSearchable(options);
            }
        }
        search.setLogoText("Click to Search");
        search.setSearchListener(new SearchBox.SearchListener() {


            @Override
            public void onSearchOpened() {
                // Use this to tint the screen
                cardView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                closeSearch();
                search.clearSearchable();

            }

            @Override
            public void onSearchTermChanged() {
                // React to the search term changing
                // Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {

                if (searchTerm != null && searchTerm.length() != 0 && searchTerm != "") {

                    String key;
                    dbMovies.insertSearchKey(searchTerm);
                    String[] search = searchTerm.split(" ");
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < search.length; i++) {
                        builder.append(search[i]);
                        if (i < search.length - 1) {
                            builder.append("+");
                        }
                    }

                    //searchTerm = searchTerm.replaceAll("\\s", "");

                    key = urlPre + multiPost + builder;

                    Intent intent = new Intent(SearchActivity.this, Multi_Search_Activity.class);
                    intent.putExtra("tv", key);
                    startActivity(intent);

                } else {
                    new SnackBar.Builder(SearchActivity.this)
                            .withMessage("Not Proper Keyword") // OR
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
                    return;
                }

            }

            @Override
            public void onSearchCleared() {

            }

        });
    }

    private void closeSearch() {

        search.hideCircularly(this);
        onBackPressed();
    }
}
