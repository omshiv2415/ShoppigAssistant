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
public class HistoryFragment extends ShoppingListFragment {


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public Query getQuery(DatabaseReference databaseReference) {


        // All my posts

        String myUserId = getUid();
        return databaseReference.child("user-shopping-broadcast").child(myUserId)
                .orderByChild("starCount").equalTo("Completed");
    }


}