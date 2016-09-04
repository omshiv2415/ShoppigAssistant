package assistance.shopping.msc.assistant.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyShoppingBroadcastFragment extends ShoppingListFragment {

    public MyShoppingBroadcastFragment() {
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        String myUserId = getUid();
        Query myCompletedShoppingQuery = databaseReference.child("user-shopping-broadcast").child(myUserId)
                .orderByChild("starCount").equalTo("Processing");
        // [END my_top_posts_query]


        return myCompletedShoppingQuery;

    }

}
