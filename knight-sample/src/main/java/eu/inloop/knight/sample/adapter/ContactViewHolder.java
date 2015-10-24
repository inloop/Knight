package eu.inloop.knight.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.inloop.knight.sample.R;

/**
 * Class {@link ContactViewHolder}.
 *
 * @author f3rog
 * @version 2015-07-09
 */
public class ContactViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.img_contact_foto)
    ImageView foto;
    @Bind(R.id.tv_contact_name)
    TextView name;
    @Bind(R.id.tv_contact_phone)
    TextView phone;

    public ContactViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
