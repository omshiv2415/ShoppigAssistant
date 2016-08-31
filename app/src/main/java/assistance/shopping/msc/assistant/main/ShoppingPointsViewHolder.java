package assistance.shopping.msc.assistant.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import assistance.shopping.msc.assistant.R;


/**
 * Created by admin on 31/08/2016.
 */

public class ShoppingPointsViewHolder extends RecyclerView.ViewHolder {


    public TextView sPshoppingAssistantName;
    public TextView sTransactionCompletedAt;
    public TextView sPtotalShoppingPoints;
    public TextView sPearnedShoppingPoints;
    public ImageView shoppingAssistantPhoto;
    public TextView sEmail;


    public ShoppingPointsViewHolder(View itemView) {
        super(itemView);


        sPshoppingAssistantName = (TextView) itemView.findViewById(R.id.pointSaName);
        sTransactionCompletedAt = (TextView) itemView.findViewById(R.id.pointCompletedAt);
        sPtotalShoppingPoints = (TextView) itemView.findViewById(R.id.pointSaTotal);
        sPearnedShoppingPoints = (TextView) itemView.findViewById(R.id.pointEarned);
        shoppingAssistantPhoto = (ImageView) itemView.findViewById(R.id.post_author_photo);
        sEmail = (TextView) itemView.findViewById(R.id.post_author);

    }


}