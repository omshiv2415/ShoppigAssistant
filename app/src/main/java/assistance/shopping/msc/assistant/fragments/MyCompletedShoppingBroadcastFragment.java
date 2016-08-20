package assistance.shopping.msc.assistant.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyCompletedShoppingBroadcastFragment extends ShoppingListFragment {

    public MyCompletedShoppingBroadcastFragment() {
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START my_top_posts_query]
        // My top posts by number of stars
        String myUserId = getUid();
        Query myCompletedShoppingQuery = databaseReference.child("user-shopping-broadcast").child(myUserId)
                .orderByChild("starCount").equalTo("Completed");
        // [END my_top_posts_query]

        return myCompletedShoppingQuery;
    }
}
