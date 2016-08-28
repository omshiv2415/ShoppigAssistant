package assistance.shopping.msc.assistant.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.model.ShoppingBroadcast;


public class ShoppingBroadcastViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public TextView timeView;
    public ImageView shoppingAssistantPhoto;
    public RelativeLayout rel;
    public TextView paymentTypeText;
    public TextView saAddressfirstline;
    public TextView sacity;
    public TextView saPostcode;
    public TextView SaName;
    public LinearLayout hide;
    public RelativeLayout topline;
    public TextView srfirstline;
    public TextView srcity;
    public TextView srpostcode;
    public TextView spdeliveredtime;
    public TextView transactioncompletedat;
    public ShoppingBroadcastViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
        timeView = (TextView) itemView.findViewById(R.id.timeStamp);
        shoppingAssistantPhoto = (ImageView) itemView.findViewById(R.id.post_author_photo);
        rel = (RelativeLayout) itemView.findViewById(R.id.item_View);
        paymentTypeText = (TextView) itemView.findViewById(R.id.paymentType);
        topline = (RelativeLayout) itemView.findViewById(R.id.bottomLine);
        hide = (LinearLayout) itemView.findViewById(R.id.hideShoppingRequester);
        saAddressfirstline = (TextView)itemView.findViewById(R.id.firstLine);
        sacity = (TextView)itemView.findViewById(R.id.city);
        saPostcode = (TextView)itemView.findViewById(R.id.postCode);
        SaName = (TextView)itemView.findViewById(R.id.shoppingAssistantName);
        srfirstline = (TextView)itemView.findViewById(R.id.srFirstLineOfAddress);
        srcity = (TextView)itemView.findViewById(R.id.shoppingRequesterCity);
        srpostcode = (TextView)itemView.findViewById(R.id.shoppingRequesterPostcode);
        spdeliveredtime = (TextView)itemView.findViewById(R.id.shoppingDeliveredTime);
        transactioncompletedat = (TextView)itemView.findViewById(R.id.transactionCompletedAt);
    }

    public void bindToPost(ShoppingBroadcast shoppingBroadcast, View.OnClickListener starClickListener) {
        titleView.setText(shoppingBroadcast.title);
        authorView.setText(shoppingBroadcast.shoppingAssistant);
        numStarsView.setText(String.valueOf(shoppingBroadcast.starCount));
        bodyView.setText(shoppingBroadcast.body);
        timeView.setText(shoppingBroadcast.createdAt);
        paymentTypeText.setText(shoppingBroadcast.paymentType);
        starView.setOnClickListener(starClickListener);
        saAddressfirstline.setText(shoppingBroadcast.saFirstLineAddress);
        sacity.setText(shoppingBroadcast.saCity);
        saPostcode.setText(shoppingBroadcast.saPostCode);
        SaName.setText(shoppingBroadcast.shoppingAssistantName);
        srfirstline.setText(shoppingBroadcast.srFirstLineAddress);
        srcity.setText(shoppingBroadcast.srCity);
        srpostcode.setText(shoppingBroadcast.srPostCode);
        spdeliveredtime.setText(shoppingBroadcast.paymentCompletedAt);
        transactioncompletedat.setText(shoppingBroadcast.paymentCompletedAt);


    }
}
