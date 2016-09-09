package assistance.shopping.msc.assistant.fragments;


import android.support.v4.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableBlankFragment extends ShoppingListFragment {


    public AvailableBlankFragment() {
        // Required empty public constructor
    }


    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        String myUserId = getUid();
        Query myCompletedShoppingQuery = databaseReference.child("user-shopping-broadcast")
                .orderByChild("shoppingStatus").equalTo("Processing");
        // [END my_top_posts_query]

        return myCompletedShoppingQuery;

    }
}
