package eu.inloop.knight.sample.model.api;

import eu.inloop.knight.sample.model.Contact;
import eu.inloop.knight.sample.model.Items;
import eu.inloop.knight.sample.model.Order;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Interface {@link IApi}
 *
 * @author f3rog
 * @version 2015-07-09
 */
public interface IApi {

    String API_ENDPOINT = "https://inloop-contacts.appspot.com/_ah/api";
    String IMG_ENDPOINT = "http://inloop-contacts.appspot.com/";

    @GET("/contactendpoint/v1/contact")
    void getContacts(ApiCallback<Items<Contact>> callback);

    @POST("/contactendpoint/v1/contact")
    void postContact(@Body Contact contact, ApiCallback<Contact> callback);

    @GET("/orderendpoint/v1/order/{contactId}")
    void getOrders(@Path("contactId") String contactId, ApiCallback<Items<Order>> callback);


}
