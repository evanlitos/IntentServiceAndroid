package goit.com.intentserviceandroid;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Carlos on 19/07/2016.
 */
public class MiServicio extends IntentService {

    public static final String ACTION_PROGRESS = "goit.com.intentserviceandroid.intent.action.PROGRESO";
    public static final String ACTION_FINALIZE = "goit.com.intentserviceandroid.intent.action.FIN";

    public MiServicio() {
        super("MI_SERVICIO");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int iteraciones = intent.getIntExtra("iteraciones", 0);
        for (int i = 0; i < iteraciones; i++) {
            //COMUNICAMOS EL PROGRESO A LA ACTIVIDAD QUE LO MANDÖ A LLAMAR
            dormirApp();
            Intent bgIntent = new Intent();
            bgIntent.setAction(ACTION_PROGRESS);
            bgIntent.putExtra("progreso", i);
            sendBroadcast(bgIntent);
        }

        Intent bgIntent = new Intent();
        bgIntent.setAction(ACTION_FINALIZE);
        sendBroadcast(bgIntent);


    }

    void dormirApp() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
