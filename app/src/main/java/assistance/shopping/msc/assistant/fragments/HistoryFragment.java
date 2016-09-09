package assistance.shopping.msc.assistant.fragments;


import android.support.v4.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends ShoppingListFragment {


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public Query getQuery(DatabaseReference databaseReference) {


        // All my posts

        String myUserId = getUid();
        return databaseReference.child("user-shopping-broadcast").child(myUserId)
                .orderByChild("shoppingStatus").equalTo("Completed");
    }


}