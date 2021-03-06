package eu.inloop.knight.sample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import eu.inloop.knight.sample.R;
import eu.inloop.knight.sample.model.Contact;
import eu.inloop.knight.sample.model.api.IApi;

/**
 * Class {@link ContactRecyclerAdapter}.
 *
 * @author f3rog
 * @version 2015-07-09
 */
public class ContactRecyclerAdapter extends BaseRecyclerAdapter<Contact, ContactViewHolder> {

    private Picasso mPicasso;

    public ContactRecyclerAdapter(@NonNull Context context, @NonNull List<Contact> items) {
        super(context, items);
        mPicasso = Picasso.with(context);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, Contact contact) {
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getPhone());
        if (contact.getPictureUrl() != null) {
            mPicasso.load(IApi.IMG_ENDPOINT + contact.getPictureUrl())
                    .placeholder(R.drawable.ic_contact_placeholder)
                    .into(holder.foto);
        } else {
            holder.foto.setImageDrawable(
                    ContextCompat.getDrawable(getContext(), R.drawable.ic_contact_placeholder)
            );
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ContactViewHolder(getInflater().inflate(R.layout.item_contact, viewGroup, false));
    }

}
