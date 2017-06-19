package com.josemanueldsds.curriculum.api.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josemanueldsds.curriculum.api.holders.BaseHolder;
import com.josemanueldsds.curriculum.api.interfaces.AdapterChangeListener;
import com.josemanueldsds.curriculum.api.interfaces.AdapterScrollListener;
import com.josemanueldsds.curriculum.api.interfaces.OnItemClickListener;
import com.josemanueldsds.curriculum.api.interfaces.OnLongItemClickListener;

import java.util.ArrayList;

/**
 * Base Adapter
 *
 * @author Jose Manuel De Sousa
 * @version 0.1.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseAdapter<M, VH extends BaseHolder> extends RecyclerView.Adapter<VH> {

    private static final String TAG = "BaseAdapter";
    /**
     * Protected fields
     */
    protected Activity context;
    protected ArrayList<M> dataSet;
    protected AdapterChangeListener<M> dataSetChangeListener;
    protected OnItemClickListener<M> onItemClick;
    protected OnLongItemClickListener<M> onLongItemClick;
    protected AdapterScrollListener<M> adapterScrollListener;

    /**
     * Constructor
     *
     * @param context Activity context
     * @param dataSet ArrayList of Object Model
     */
    protected BaseAdapter(Activity context, ArrayList<M> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    /**
     * Create view holder
     *
     * @param itemView ViewGroup parent resource
     * @param type     Integer row type
     * @return called for the creation wieven object instance
     */
    @Override
    public VH onCreateViewHolder(ViewGroup itemView, int type) {
        View cardView = LayoutInflater.from(context).inflate(getRowViewResourceId(), itemView, false);
        return createHolder(cardView, type);
    }

    /**
     * Sets the view holder
     *
     * @param baseHolder WievenHolder instance object
     * @param position   Integer position shipmentTo render
     */
    @Override
    public void onBindViewHolder(final VH baseHolder, final int position) {
        if (dataSet != null) {
            bindBaseHolder(baseHolder, getItem(position), position);
            if (adapterScrollListener != null && position == dataSet.size() - 1)
                adapterScrollListener.onScrollEnded(getItem(position), position);
            if (onItemClick != null) {
                baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClick.onItemClick(baseHolder.itemView, getItem(baseHolder.getAdapterPosition()), baseHolder.getAdapterPosition());
                    }
                });
            }
            if (onLongItemClick != null) {
                baseHolder.itemView.setLongClickable(true);
                baseHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onLongItemClick.onLongItemClick(baseHolder.itemView, getItem(baseHolder.getAdapterPosition()), baseHolder.getAdapterPosition());
                        return true;
                    }
                });
            } else {
                baseHolder.itemView.setLongClickable(false);
            }
        }
    }

    /**
     * add an item at last index available position
     *
     * @param item M
     */
    public void addLastItem(M item) {
        if (dataSet == null)
            dataSet = new ArrayList<>();
        dataSet.add(item);
        notifyItemInserted(dataSet.size() - 1);
    }

    /**
     * add an item at first index position
     *
     * @param item M
     */
    public void addFirstItem(M item) {
        if (dataSet == null)
            dataSet = new ArrayList<>();
        dataSet.add(0, item);
        notifyItemInserted(0);
    }

    /**
     * and an item at specific position
     *
     * @param item     M
     * @param position Integer
     */
    public void addItem(M item, int position) {
        if (dataSet == null)
            dataSet = new ArrayList<>();
        dataSet.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * remove specific item position
     *
     * @param position Integer
     */
    public void removeItem(final int position) {
        if (position >= dataSet.size())
            return;
        dataSet.remove(position);
        notifyItemRemoved(position);
        if (dataSet != null)
            notifyItemRangeChanged(position, dataSet.size());
        if (dataSetChangeListener != null)
            dataSetChangeListener.onDataSetChange(dataSet);

    }

    /**
     * remove first item position
     */
    public void removeFirstItem() {
        if (!dataSet.isEmpty()) {
            dataSet.remove(0);
            notifyItemRemoved(0);
            if (dataSet != null)
                notifyItemRangeChanged(0, dataSet.size());
            if (dataSetChangeListener != null)
                dataSetChangeListener.onDataSetChange(dataSet);

        }
    }

    /**
     * remove last item position
     */
    public void removeLastItem() {
        if (!dataSet.isEmpty()) {
            dataSet.remove(dataSet.size() - 1);
            notifyItemRemoved(dataSet.size() - 1);
            if (dataSet != null)
                notifyItemRangeChanged(dataSet.size() - 1, dataSet.size());
            if (dataSetChangeListener != null)
                dataSetChangeListener.onDataSetChange(dataSet);

        }
    }

    /**
     * Returns the number of items of dataSet
     *
     * @return dataSet
     */
    @Override
    public int getItemCount() {
        return dataSet != null ? dataSet.size() : 0;
    }

    /**
     * Get row view shipmentTo inflate
     *
     * @return int layout resource shipmentTo inflate
     */
    public abstract int getRowViewResourceId();

    /**
     * Create a Wieven object instance
     *
     * @param itemView row item view
     * @param type     int row type
     * @return Wieven object instance
     */
    protected abstract VH createHolder(View itemView, int type);

    /**
     * Bind Base Holder
     *
     * @param baseHolder BaseHolder instance
     * @param itemData   data in the current position
     * @param position   int position shipmentTo render
     */
    protected abstract void bindBaseHolder(VH baseHolder, M itemData, int position);

    /**
     * Get item data in a specific position
     *
     * @param position int position shipmentTo find
     * @return Item Data
     */
    public M getItem(int position) {
        M item = null;
        if (dataSet != null &&
                dataSet.size() > position) {
            item = dataSet.get(position);
        }
        return item;
    }

    /**
     * Get item data in a first position
     *
     * @return Item Data
     */
    public M getFirstItem() {
        M item = null;
        if (dataSet != null && !dataSet.isEmpty()) {
            item = dataSet.get(0);
        }
        return item;
    }

    /**
     * Get item data in a last position
     *
     * @return Item Data
     */
    public M getLastItem() {
        M item = null;
        if (dataSet != null && !dataSet.isEmpty()) {
            item = dataSet.get(dataSet.size() - 1);
        }
        return item;
    }

    /**
     * Change DataSet object and notify shipmentTo this adapter that data has been changed.
     */
    public void changeDataSet(ArrayList<M> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
        if (dataSetChangeListener != null)
            dataSetChangeListener.onDataSetChange(this.dataSet);
    }

    /**
     * Send local broadcast
     *
     * @param broadcast Intent broadcast with channel and extras
     */
    public void sendLocalBroadcast(Intent broadcast) {
        LocalBroadcastManager.getInstance(context)
                .sendBroadcast(broadcast);
    }

    /**
     * Set adapter scroll listener
     *
     * @param adapterScrollListener Adapter Scroll Listener of M
     */
    public void setAdapterScrollListener(AdapterScrollListener<M> adapterScrollListener) {
        this.adapterScrollListener = adapterScrollListener;
    }

    /**
     * Set Adapter Change Listener CallBack
     *
     * @param dataSetChangeListener AdapterChangeListener
     */
    public void setDataSetChangeListener(AdapterChangeListener<M> dataSetChangeListener) {
        this.dataSetChangeListener = dataSetChangeListener;
    }

    /**
     * Set on Item Click Listener Callback
     *
     * @param onItemClick OnItemClickListener<M> where M is the generic type of the object model.
     */
    public void setOnItemCLickListener(OnItemClickListener<M> onItemClick) {
        this.onItemClick = onItemClick;
    }

    /**
     * Set on Item Click Listener Callback
     *
     * @param onLongItemClick OnLongItemClickListener<M> where M is the generic type of the object model.
     */
    public void setOnLongItemClick(OnLongItemClickListener<M> onLongItemClick) {
        this.onLongItemClick = onLongItemClick;
    }
}
