package goit.com.intentserviceandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button startService;
    private ProgressBar barra;
    File photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barra = (ProgressBar) findViewById(R.id.progreso_Servicio);
        startService = (Button) findViewById(R.id.startService);

        barra.setMax(10);

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MiServicio.class);
                intent.putExtra("iteraciones", 10);
                startService(intent);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(MiServicio.ACTION_PROGRESS);
        filter.addAction(MiServicio.ACTION_FINALIZE);

        ReceiverData datos = new ReceiverData();

        registerReceiver(datos, filter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("REQUEST", requestCode + "");
        Log.i("RESULT", resultCode + "");
        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                ImageView imagen = (ImageView) findViewById(R.id.imagenCapturada);

                Bitmap bmp = data.getExtras().getParcelable("data");
                imagen.setImageBitmap(bmp);
                // imagen.setImageURI(Uri.fromFile(photo));
            }
        }

    }


    public class ReceiverData extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MiServicio.ACTION_PROGRESS)) {
                int progresso = intent.getIntExtra("progreso", 0);
                barra.setProgress(progresso);
                Log.i("PROGRESO", progresso + "%");
            } else if (intent.getAction().equals(MiServicio.ACTION_FINALIZE)) {
                Toast.makeText(MainActivity.this, "Ha finalizado el servicio", Toast.LENGTH_SHORT).show();

                String state = Environment.getExternalStorageDirectory() + "/DCIM/MiFoto_" + (new Date().getTime()) + ".jpg";

                Intent intentCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                Intent imageGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                photo = new File(state);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));

                startActivityForResult(intent.createChooser(intentCamera, "Capture Image"), 111);
            }
        }
    }
}
