package assistance.shopping.msc.assistant.support;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by omshiv on 05/02/2015.
 */

public class AlarmService extends Service {

    public static String TAG = AlarmService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
}