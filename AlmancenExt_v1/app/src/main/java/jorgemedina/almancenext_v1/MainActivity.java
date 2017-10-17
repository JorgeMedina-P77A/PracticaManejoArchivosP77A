package jorgemedina.almancenext_v1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private Button saveButton;
    private Button readButton;
    private Button listButton;

    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    private final String fileName = "note.txt";


    //EJECUCION DE LA APLICACION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);

        saveButton = (Button) findViewById(R.id.button_save);
        readButton = (Button) findViewById(R.id.button_read);
        listButton = (Button) findViewById(R.id.button_list);


        //GUARDAR ARCHIVO;
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                askPermissionAndWriteFile();

            }

        });

        //CARGAR ARCHIVO;
        readButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                askPermissionAndReadFile();

            }

        });

        //MOSTRAR DIRECTORIOS;
        listButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                listExternalStorages();

            }

        });

    }


    //SOLICITA PERMISO DE ESCRITURA;
    private void askPermissionAndWriteFile() {

        boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //

        if (canWrite) {

            this.writeFile();

        }

    }


    //SOLICITA PERMISO DE LECTURA;
    private void askPermissionAndReadFile() {

        boolean canRead = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        //

        if (canRead) {

            this.readFile();

        }

    }


    //EVALUA VERSION DEL S.O. DEL DISPOSITIVO
    private boolean askPermission(int requestId, String permissionName) {

        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(this, permissionName);


            if (permission != PackageManager.PERMISSION_GRANTED) {

                // If don't have permission so prompt the user.
                this.requestPermissions(

                        new String[]{permissionName},
                        requestId

                );

                return false;

            }

        }

        return true;

    }


    //SOLICITA PERMISO EN TIEMPO DE EJECUCION;
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {

            switch (requestCode) {

                case REQUEST_ID_READ_PERMISSION: {

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        readFile();

                    }

                }

                case REQUEST_ID_WRITE_PERMISSION: {

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        writeFile();

                    }

                }

            }

        } else {

            Toast.makeText(getApplicationContext(), "Permission Cancelled!", Toast.LENGTH_SHORT).show();

        }

    }


    //ESCRITURA DE ARCHIVO;
    private void writeFile() {

        File extStore = Environment.getExternalStorageDirectory();
        String path = extStore.getAbsolutePath() + "/" + fileName;
        Log.i("ExternalStorageDemo", "Save to: " + path);

        String data = editText.getText().toString();

        try {

            File myFile = new File(path);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();

            Toast.makeText(getApplicationContext(), fileName + " saved", Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    //LECTURA DE ARCHIVO;
    private void readFile() {

        File extStore = Environment.getExternalStorageDirectory();
        String path = extStore.getAbsolutePath() + "/" + fileName;
        Log.i("ExternalStorageDemo", "Read file: " + path);

        String s = "";
        String fileContent = "";

        try {

            File myFile = new File(path);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));

            while ((s = myReader.readLine()) != null) {

                fileContent += s + "\n";

            }

            myReader.close();

            this.textView.setText(fileContent);

        } catch (IOException e) {

            e.printStackTrace();

        }

        Toast.makeText(getApplicationContext(), fileContent, Toast.LENGTH_LONG).show();

    }


    //MOSTRAR DIRECTORIO;
    private void listExternalStorages() {

        StringBuilder sb = new StringBuilder();

        sb.append("Data Directory: ").append("\n - ")
                .append(Environment.getDataDirectory().toString()).append("\n");

        sb.append("Download Cache Directory: ").append("\n - ")
                .append(Environment.getDownloadCacheDirectory().toString()).append("\n");

        sb.append("External Storage State: ").append("\n - ")
                .append(Environment.getExternalStorageState().toString()).append("\n");

        sb.append("External Storage Directory: ").append("\n - ")
                .append(Environment.getExternalStorageDirectory().toString()).append("\n");

        sb.append("Is External Storage Emulated?: ").append("\n - ")
                .append(Environment.isExternalStorageEmulated()).append("\n");

        sb.append("Is External Storage Removable?: ").append("\n - ")
                .append(Environment.isExternalStorageRemovable()).append("\n");

        sb.append("External Storage Public Directory (Music): ").append("\n - ")
                .append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()).append("\n");

        sb.append("Download Cache Directory: ").append("\n - ")
                .append(Environment.getDownloadCacheDirectory().toString()).append("\n");

        sb.append("Root Directory: ").append("\n - ")
                .append(Environment.getRootDirectory().toString()).append("\n");

        Log.i("ExternalStorageDemo", sb.toString());
        this.textView.setText(sb.toString());

    }

}