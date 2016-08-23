package assistance.shopping.msc.assistant.support;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;


/**
 * Created by omshiv on 07/03/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // setting up the parse push Notification

        // setting alarm service for notification in the phone if user restart the phone
        // still system will set the alarm by default so patient will get notification
        // 24 hours before the actual appointment date
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent mServiceIntent = new Intent();
            mServiceIntent.setAction("<assistance.shopping.msc.assistant.support>.BootService");
            ComponentName service = context.startService(mServiceIntent);
            if (null == service) {
                // something really wrong here
            }
        }

    }

}