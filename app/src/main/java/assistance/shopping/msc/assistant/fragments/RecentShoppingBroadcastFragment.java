package assistance.shopping.msc.assistant.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RecentShoppingBroadcastFragment extends ShoppingListFragment {

    public RecentShoppingBroadcastFragment() {
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        //Query recentShoppingQuery = databaseReference.child("shopping-broadcast").limitToFirst(100);
        Query recentShoppingQuery = databaseReference.child("shopping-broadcast")
                .orderByChild("starCount").equalTo("Processing");

        // [END recent_posts_query]

        return recentShoppingQuery;
    }
}
