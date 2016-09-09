package assistance.shopping.msc.assistant.support;

import android.app.Activity;
import android.app.ProgressDialog;

import com.google.firebase.auth.FirebaseAuth;

import assistance.shopping.msc.assistant.R;

/**
 * Created by admin on 27/07/2016.
 */

public class FragmentSupport extends Activity {

    private ProgressDialog mProgressDialog;

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(FragmentSupport.this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
