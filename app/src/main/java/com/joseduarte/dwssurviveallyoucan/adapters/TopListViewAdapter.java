package com.joseduarte.dwssurviveallyoucan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.joseduarte.dwssurviveallyoucan.R;
import com.joseduarte.dwssurviveallyoucan.models.User;

import java.util.List;

public class TopListViewAdapter extends RecyclerView.Adapter<TopListViewAdapter.ViewHolder> {

    private List<User> mValues;

    public TopListViewAdapter(List<User> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tops_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.start(position);
    }

    public void update(List<User> items) {
        mValues = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mPosView;
        public final TextView mIdView;
        public final TextView mCoins;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPosView = view.findViewById(R.id.item_position_field);
            mIdView = view.findViewById(R.id.item_user_name);
            mCoins = view.findViewById(R.id.item_coins_field);
        }

        public void start(int position) {
            mPosView.setText(""+(position + 1));
            mIdView.setText( mItem.getCurrentName() );
            mCoins.setText( ""+mItem.getCoins() );
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "' has earn " + mCoins.getText() +" coins";
        }
    }
}