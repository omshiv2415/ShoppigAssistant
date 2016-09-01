package assistance.shopping.msc.assistant.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trncic.library.DottedProgressBar;

import assistance.shopping.msc.assistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    DottedProgressBar progressBar;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        progressBar = (DottedProgressBar) rootView.findViewById(R.id.chat_dot_progress);
        progressBar.startProgress();


        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressBar.stopProgress();
    }

    @Override
    public void onStop() {
        super.onDestroy();
        progressBar.stopProgress();
    }
}
