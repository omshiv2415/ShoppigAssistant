package assistance.shopping.msc.assistant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 30/06/2016.
 */

public class Support {

    public Support() {
        // Required empty public constructor
    }

    // validating email id
    protected boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    public boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 7 && pass.length() <= 21) {
            return true;
        }
        return false;
    }


}
