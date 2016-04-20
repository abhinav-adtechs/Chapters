package im.rohan.chapters;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentClubs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentClubs extends Fragment implements SearchView.OnQueryTextListener,SearchView.OnFocusChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static List<ParseObject> allObjects = new ArrayList<ParseObject>();
    private RecyclerView listClubs;
    private AdapterClubs adapterClubs;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private Toolbar toolbar;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentClubs.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentClubs newInstance(String param1, String param2) {
        FragmentClubs fragment = new FragmentClubs();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentClubs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);

    }



    int skip=0;
    FindCallback<ParseObject> getAllObjects(){

        progressBar.setVisibility(View.VISIBLE);
        listClubs.setVisibility(View.GONE);
        return new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    allObjects.addAll(objects);
                    int limit = 1000;
                    if (objects.size() == limit) {
                        skip = skip + limit;
                        ParseQuery query = new ParseQuery("Clubs");
                        query.setSkip(skip);
                        query.setLimit(limit);
                        query.findInBackground(getAllObjects());
                    }
                    else {
                        adapterClubs.setListClubChapter(objects);
                        listClubs.setAdapter(adapterClubs);
                        progressBar.setVisibility(View.GONE);
                        listClubs.setVisibility(View.VISIBLE);
                        swipeRefresh.setRefreshing(false);
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);


                }
            }
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clubs,container,false);
        toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        listClubs = (RecyclerView) view.findViewById(R.id.listClubs);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);

        swipeRefresh.getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                swipeRefresh
                                        .getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                                swipeRefresh.setRefreshing(true);
                            }
                        });


        refreshItems();


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                // Refresh items
                refreshItems();
            }
        });


        listClubs.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterClubs = new AdapterClubs(getActivity());

        listClubs.setAdapter(adapterClubs);
        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });
        listClubs.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {


            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(allObjects.get(recyclerView.getChildPosition(child)).getString("facebook")));
                    startActivity(intent);
                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        //final ParseQuery parseQuery = new ParseQuery("Clubs");
        //parseQuery.setLimit(1000);
        //parseQuery.findInBackground(getAllObjects());
        return view;
    }
    void refreshItems() {
        final ParseQuery parseQuery = new ParseQuery("Clubs");
        parseQuery.setLimit(1000);
        parseQuery.findInBackground(getAllObjects());


        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {



    }
    Menu menu;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_search, menu);
        this.menu = menu;
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextFocusChangeListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapterClubs.getFilter().filter(query);
        return true;
    }



    @Override
    public boolean onQueryTextChange(String newText) {
        adapterClubs.getFilter().filter(newText);
        return true;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        MenuItem menuItem = menu.findItem(R.id.search);
        if(!b){
            MenuItemCompat.collapseActionView(menuItem);
            swipeRefresh.setRefreshing(true);
            refreshItems();
        }
    }
}
