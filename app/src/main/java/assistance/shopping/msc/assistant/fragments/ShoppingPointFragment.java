package assistance.shopping.msc.assistant.fragments;


import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.main.ShoppingPointsViewHolder;
import assistance.shopping.msc.assistant.model.ShoppingPoints;
import assistance.shopping.msc.assistant.support.FragmentSupport;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingPointFragment extends Fragment {

    private static final String TAG = "ShoppingPointsFragment";
    private static final int TAG_SIMPLE_NOTIFICATION = 1;
    public ProgressBar Shopping;
    public DatabaseReference mShoppingPoint;
    public FloatingActionButton fab;
    FragmentSupport fragmentSupport = new FragmentSupport();
    final String userId = fragmentSupport.getUid();
    // [END define_database_reference]
    // [START define_database_reference]
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<ShoppingPoints, ShoppingPointsViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private Location TODO = null;


    public ShoppingPointFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shopping_point, container, false);


        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = (RecyclerView) rootView.findViewById(R.id.points_list);
        mRecycler.setHasFixedSize(true);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_new_post);

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);
        // Query postsQuery = getQuery(mDatabase).orderByChild("starCount").equalTo("In Process");

        mAdapter = new FirebaseRecyclerAdapter<ShoppingPoints, ShoppingPointsViewHolder>(ShoppingPoints.class, R.layout.item_points,
                ShoppingPointsViewHolder.class, postsQuery) {
            /**
             * Each time the data at the given Firebase location changes, this method will be called for each item that needs
             * to be displayed. The first two arguments correspond to the mLayout and mModelClass given to the constructor of
             * this class. The third argument is the item's position in the list.
             * <p>
             * Your implementation should populate the view using the data contained in the model.
             *
             * @param viewHolder The view to populate
             * @param model      The object containing the data used to populate the view
             * @param position   The position in the list of the view being populated
             */
            @SuppressLint("SetTextI18n")
            @Override
            protected void populateViewHolder(ShoppingPointsViewHolder viewHolder, ShoppingPoints model, int position) {


                viewHolder.sPshoppingAssistantName.setText(model.shoppingAssistantName);
                viewHolder.sTransactionCompletedAt.setText((model.transactionCompletedAt));
                viewHolder.sPtotalShoppingPoints.setText(model.totalShoppingPoints.toString());
                viewHolder.sPearnedShoppingPoints.setText(model.earnedShoppingPoints.toString());
                viewHolder.sEmail.setText(model.sAEmail);

                if (model.sAPhotoUrl == null) {
                    viewHolder.shoppingAssistantPhoto
                            .setImageDrawable(ContextCompat
                                    .getDrawable(getActivity(),
                                            R.drawable.profile));
                } else {
                    Glide.with(getActivity())
                            .load(model.sAPhotoUrl)
                            .into(viewHolder.shoppingAssistantPhoto);
                }


            }


        };
        mRecycler.setAdapter(mAdapter);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }


    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public Query getQuery(DatabaseReference databaseReference) {

        Query recentShoppingQuery = databaseReference.child("user-shopping-points").
                child(userId);

        // [END recent_posts_query]

        return recentShoppingQuery;

    }


}
