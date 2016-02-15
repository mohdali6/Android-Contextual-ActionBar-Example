package com.example.mali.contextualactionbarexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private final String SELECTION_COUNT = "selection_count";
    private final String SELECTED_ITEMS_HASHSET = "selected_items_hashset";
    private final String DATA_ARRAY_LIST = "data_array_list";
    private ListAdapter mAdapter;
    private ArrayList<String> data;
    private int selectionCount;
    private TreeSet<Integer> selectedItemsPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            selectedItemsPosition = (TreeSet<Integer>) savedInstanceState
                    .getSerializable(SELECTED_ITEMS_HASHSET);
            selectionCount = savedInstanceState.getInt(SELECTION_COUNT);
            data = savedInstanceState.getStringArrayList(DATA_ARRAY_LIST);
        } else {
            selectionCount = 0;
            selectedItemsPosition = new TreeSet<>();
            data = new ArrayList<>(
                    Arrays.asList(
                            "One",
                            "Two",
                            "Three",
                            "Four",
                            "Five",
                            "Six",
                            "Seven",
                            "Eight",
                            "Nine",
                            "Ten"
                    )
            );
        }

        mAdapter = new ListAdapter(this,
                R.layout.list_item,
                R.id.textView,
                data);
        mAdapter.setCheckedItems(selectedItemsPosition);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    selectionCount++;
                    selectedItemsPosition.add(position);
                    mAdapter.setCheckedItems(selectedItemsPosition);
                    mAdapter.notifyDataSetChanged();
                } else {
                    selectionCount--;
                    selectedItemsPosition.remove(position);
                    mAdapter.notifyDataSetChanged();
                }

                if (selectionCount != 0) {
                    mode.setTitle(Integer.toString(selectionCount));
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.contextual_action_bar_menu, menu);

                if (selectionCount != 0) {
                    mode.setTitle(Integer.toString(selectionCount));
                }

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        deleteSelectedItems();

                        String text = Integer.toString(selectionCount).concat(" items deleted!");
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                        toast.show();

                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selectionCount = 0;
                selectedItemsPosition.clear();
            }
        });
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTION_COUNT, selectionCount);
        outState.putSerializable(SELECTED_ITEMS_HASHSET, selectedItemsPosition);
        outState.putStringArrayList(DATA_ARRAY_LIST, data);
        super.onSaveInstanceState(outState);
    }

    private void deleteSelectedItems() {
        Iterator iterator = selectedItemsPosition.iterator();

        int i = 0;
        while (iterator.hasNext()) {
            int value = (Integer) iterator.next();
            data.remove(value - i);
            i++;
        }
        mAdapter.notifyDataSetChanged();
    }
}