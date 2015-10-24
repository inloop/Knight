package eu.inloop.knight.sample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

/**
 * Class {@link BaseRecyclerAdapter}.
 *
 * @author f3rog
 * @version 2015-07-06
 */
public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public interface OnItemClickListener {

        void onItemClicked(int position);
    }

    private OnItemClickListener mItemClickListener;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final List<T> mItems;

    public BaseRecyclerAdapter(@NonNull Context context, @NonNull List<T> items) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItems = items;
    }

    public Context getContext() {
        return mContext;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public final void onBindViewHolder(VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) mItemClickListener.onItemClicked(position);
            }
        });
        T item = getItem(position);
        if (item != null) onBindViewHolder(holder, item);
    }

    public abstract void onBindViewHolder(VH holder, T item);

}
