package assistance.shopping.msc.assistant.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import assistance.shopping.msc.assistant.R;

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
                .orderByChild("starCount").equalTo("Processing");
        // [END my_top_posts_query]

        return myCompletedShoppingQuery;

    }
}
