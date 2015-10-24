package eu.inloop.knight.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.inloop.knight.sample.R;

/**
 * Class {@link OrderViewHolder}.
 *
 * @author f3rog
 * @version 2015-07-09
 */
public class OrderViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.tv_order_name)
    TextView name;
    @Bind(R.id.tv_order_count)
    TextView count;

    public OrderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
