package assistance.shopping.msc.assistant.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyShoppingBroadcastFragment extends ShoppingListFragment {

    public MyShoppingBroadcastFragment() {
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("user-shopping-broadcast")
                .child(getUid());

    }

}
