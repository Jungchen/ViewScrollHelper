package com.hrules.viewscrollhelper.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hrules.viewscrollhelper.OnViewScrollVisibilityChanged;
import com.hrules.viewscrollhelper.ViewScrollHelper;
import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity implements OnViewScrollVisibilityChanged {
    private static final String TAG = "RecyclerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerAdapter(createList()));

        new ViewScrollHelper(recyclerView, toolbar, this);
    }

    private List<String> createList() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            list.add("Item " + i);
        }
        return list;
    }

    @Override
    public void onShow() {
        Log.e(TAG, "onShow");
    }

    @Override
    public void onHide() {
        Log.e(TAG, "onHide");
    }

    @Override
    public void onMove(int distance) {

    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final List<String> list;

        public RecyclerAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
            return new RecyclerItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
            holder.textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
            public final TextView textView;

            public RecyclerItemViewHolder(final View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textView);
            }
        }
    }
}