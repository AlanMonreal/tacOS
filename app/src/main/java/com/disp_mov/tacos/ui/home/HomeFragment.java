package com.disp_mov.tacos.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.disp_mov.tacos.OrderDetailActivity;
import com.disp_mov.tacos.OrderDetailFragment;
import com.disp_mov.tacos.R;
import com.disp_mov.tacos.data.model.Orders;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private boolean mTwoPane = false;
    private RecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final View recyclerView = root.findViewById(R.id.order_list);
        final FloatingActionButton fab = root.findViewById(R.id.fab);
        assert recyclerView != null;
        Log.d("dummy", "size" + Orders.ITEMS.size());
        if(Orders.ITEMS.size()!= 0) {
            adapter = new RecyclerViewAdapter(HomeFragment.this, Orders.ITEMS, mTwoPane);
            setupRecyclerView((RecyclerView) recyclerView, adapter);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Orders.ITEMS.size()== 0) {
                    Orders.addItem(Orders.createDummyItem());
                    Log.d("dummy", "size" + Orders.ITEMS.size());
                    adapter = new RecyclerViewAdapter(HomeFragment.this, Orders.ITEMS, mTwoPane);
                    setupRecyclerView((RecyclerView) recyclerView, adapter);
                } else {
                    Orders.addItem(Orders.createDummyItem());
                    adapter.notifyItemInserted(Orders.ITEMS.size());
                }

            }
        });

        homeViewModel.getList().observe(this, new Observer<ArrayList<Orders.Order>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Orders.Order> s) {
                //adapter.notifyDataSetChanged();
            }
        });
        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, @NonNull RecyclerViewAdapter adapter) {
        //recyclerView.setAdapter(new HomeFragment.RecyclerViewAdapter(HomeFragment.this, Orders.ITEMS, mTwoPane));
        recyclerView.setAdapter(adapter);
    }

    public static class RecyclerViewAdapter extends RecyclerView.Adapter<HomeFragment.RecyclerViewAdapter.ViewHolder> {

        private final HomeFragment mParentActivity;
        private final ArrayList<Orders.Order> mValues;
        private final boolean mTwoPane;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Orders.Order item = (Orders.Order) view.getTag();
                Context context = view.getContext();
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra(OrderDetailFragment.ARG_ITEM_ID, item.id);
                context.startActivity(intent);
            }
        };

        RecyclerViewAdapter(HomeFragment parent, ArrayList<Orders.Order> items, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }


        @Override
        public HomeFragment.RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_content, parent, false);

            return new HomeFragment.RecyclerViewAdapter.ViewHolder(view);
        }

        public void updateData(ArrayList<Orders.Order> orders) {
            Log.d("dummy", "updating data");
            //mValues.clear();
            //mValues.addAll(orders);
            notifyDataSetChanged();
        }

        public void addItem(int position, Orders.Order order) {
            mValues.add(position, order);
            notifyItemInserted(position);
        }

        public void removeItem(int position) {
            mValues.remove(position);
            notifyItemRemoved(position);
        }


        @Override
        public void onBindViewHolder(final HomeFragment.RecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

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