package com.example.chaojung.nytimessearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class SearchActivity extends AppCompatActivity implements FilterSearchDialogFragment.FilterSearchDialogListener {

    ArticlesAdapter articlesAdapter;
    RecyclerView rvArticles;

    List<Doc> article;
    //int page = 0;
    String apiKey = "b04b71a746fa4bc5aaf6f146e9b7cff0";
    String keyword;

    String filterDate = null;
    String filterSort = null;
    String filterNewsDesk = null;

    Filter filter;

    private EndlessRecyclerViewScrollListener scrollListener;

    // Trailing slash is needed
    public static final String BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    public void onFinishFilterDialog(Filter filterconfirmed) {
        filter = filterconfirmed;

        if(filter.getBeginDate() != null)
            filterDate = filter.getBeginDate();
        if(filter.getSortOrder() != null)
            filterSort = filter.getSortOrder();
        if(!filter.getNewsDesk().equals("")){
            filterNewsDesk = "news_desk:("+filter.getNewsDesk()+")";
        }
    }

    public interface MyApiEndpointInterface {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @GET("articlesearch.json")
        Call<Article> getArticle(
                @Query("api-key") String apiKey,
                @Query("page") int page,
                @Query("q") String query,
                @Query("begin_date") String beginDate,
                @Query("sort") String sortOrder,
                @Query("fq") String newsDesk
        );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();

    } //API Key = b04b71a746fa4bc5aaf6f146e9b7cff0

    public void setupViews(){

        filter =  new Filter();
        // Lookup the recyclerview in activity layout
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        // Initialize contacts
        article = new ArrayList<>();
        // Create adapter passing in the sample user data
        articlesAdapter = new ArticlesAdapter(this,article);
        // Attach the adapter to the recyclerview to populate items
        rvArticles.setAdapter(articlesAdapter);
        // Set layout manager to position the items
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        rvArticles.setLayoutManager(gridLayoutManager);
        rvArticles.setHasFixedSize(true);

        ItemClickSupport.addTo(rvArticles).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // do it
                Intent i = new Intent(getApplicationContext(),ArticleActivity.class);
                Doc doc = article.get(position);
                //i.putExtra("url",doc.getWebUrl());
                i.putExtra("article", Parcels.wrap(doc));
                startActivity(i);

                //Toast.makeText(SearchActivity.this,"Hello",Toast.LENGTH_SHORT).show();
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                page += 1;
                loadNextDataFromApi(page);
            }

        };
        // Adds the scroll listener to RecyclerView
        rvArticles.addOnScrollListener(scrollListener);

    }

    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MyApiEndpointInterface apiService =
                retrofit.create(MyApiEndpointInterface.class);

        Call<Article> call = apiService.getArticle(apiKey,offset,keyword,filterDate,filterSort,filterNewsDesk);

        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {

                Article articleResponse = response.body();
                article.addAll(articleResponse.getResponse().getDocs());
                articlesAdapter.notifyDataSetChanged();

                //setupViews();
                Log.d("DEBUG",articlesAdapter.toString());

            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterSearchDialogFragment filterSearchDialogFragment = FilterSearchDialogFragment.newInstance(filter);
        filterSearchDialogFragment.show(fm, "fragment_filter_search");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem filterItem = menu.findItem(R.id.action_filter);
        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showFilterDialog();
                return true;
            }
        });

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                keyword = query;
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                MyApiEndpointInterface apiService =
                        retrofit.create(MyApiEndpointInterface.class);

                Call<Article> call = apiService.getArticle(apiKey,0,keyword,filterDate,filterSort,filterNewsDesk);

                call.enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {

                        article.clear();
                        Article articleResponse = response.body();
                        if(articleResponse != null) {
                            article.addAll(articleResponse.getResponse().getDocs());
                            articlesAdapter.notifyDataSetChanged();
                        }
                        scrollListener.resetState();

                        //setupViews();
                        Log.d("DEBUG",articlesAdapter.toString());

                    }

                    @Override
                    public void onFailure(Call<Article> call, Throwable t) {

                    }
                });
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
