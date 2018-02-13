package kidnaping;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * Created by ENG RANIA on 24-Feb-17.
 */

public class ReceiveMessage extends BroadcastReceiver{
    final SmsManager mysms=SmsManager.getDefault();
    static int id=1;
    public  void onReceive(Context context, Intent intent) {
        Bundle mybundel = intent.getExtras();

        try {

            if(mybundel !=null)
            {
                final Object[] messagecontent= ( Object[])mybundel.get("pdus");
                 for(int i=0;i<messagecontent.length;i++){
                     SmsMessage mynewsms=SmsMessage.createFromPdu((byte[]) messagecontent[i]);
                     NewMessageNotification nome= new NewMessageNotification();
                     nome.notify(context,mynewsms.getDisplayOriginatingAddress(),mynewsms.getMessageBody(),id);
                     id++;
                 }
            }
        }
        catch (Exception ex) {

        }
    }

}
