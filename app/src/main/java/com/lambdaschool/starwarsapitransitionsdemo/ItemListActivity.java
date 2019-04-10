package com.lambdaschool.starwarsapitransitionsdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
// S04M03-6 replace all references to dummycontent with out model object
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private ArrayList<SwApiObject> swApiObjects;
    private SimpleItemRecyclerViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        swApiObjects = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    // S04M03-8 pull out fields from recyclerview construction and call our method
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        viewAdapter = new SimpleItemRecyclerViewAdapter(this, swApiObjects, mTwoPane);
        recyclerView.setAdapter(viewAdapter);
        getData();
    }

    // S04M03-7 write a method to retreive all the data
    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SwApiObject person = null;
                int counter = 1;
                int failCount = 0;
                do {
                    person = SwApiDao.getPerson(counter++);
                    if(person != null) {
                        swApiObjects.add(person);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewAdapter.notifyItemChanged(swApiObjects.size() - 1);
                            }
                        });
                        failCount = 0;
                    } else {
                        ++failCount;
                    }
                } while(person != null || failCount < 2);
            }
        }).start();
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity             mParentActivity;
        private final List<SwApiObject> mValues;
        private final boolean                      mTwoPane;
        private final View.OnClickListener         mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwApiObject item = (SwApiObject) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, String.valueOf(item.getId()));
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.item_detail_container, fragment)
                                   .commit();
                } else {
                    Context context = view.getContext();
                    Intent  intent  = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<SwApiObject> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // S04M03-10 convert id to string to display
            holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
            holder.mContentView.setText(mValues.get(position).getName());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
