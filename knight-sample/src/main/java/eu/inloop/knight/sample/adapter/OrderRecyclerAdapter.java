package eu.inloop.knight.sample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import java.util.List;

import eu.inloop.knight.sample.R;
import eu.inloop.knight.sample.model.Order;

/**
 * Class {@link OrderRecyclerAdapter}.
 *
 * @author f3rog
 * @version 2015-07-09
 */
public class OrderRecyclerAdapter extends BaseRecyclerAdapter<Order, OrderViewHolder> {

    public OrderRecyclerAdapter(@NonNull Context context, @NonNull List<Order> items) {
        super(context, items);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, Order order) {
        holder.name.setText(order.getName());
        holder.count.setText(order.getCount() + "x");
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new OrderViewHolder(getInflater().inflate(R.layout.item_order, viewGroup, false));
    }

}
