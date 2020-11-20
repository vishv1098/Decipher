package com.example.decipher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static android.Manifest.permission.CAMERA;

public class TranslateActivity extends AppCompatActivity {
    private TextView textView;
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextRecognizer textRecognizer;
    private TextToSpeech textToSpeech;
    private String stringResult = null;
    private EditText inputToTranslate;
    private TextView translatedTv;
    private String originalText;
    private String translatedText;
    private boolean connected;
    Translate translate;
    Spinner spinPic, spinText;
    HashMap<String, String> Languages = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Languages.put("Hindi", "hi");
        Languages.put("Kannada", "kn");
        Languages.put("Malayalam", "ml");
        Languages.put("Tamil", "ta");
        Languages.put("Telugu", "te");
        Languages.put("French", "fr");
        Languages.put("Spanish", "es");
        Languages.put("Japanese", "ja");
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                ProfileFragment()).commit();
        ActivityCompat.requestPermissions(this, new String[]{CAMERA},
                PackageManager.PERMISSION_GRANTED);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
                case R.id.nav_stats:
                    selectedFragment = new StatsFragment();
                    break;
                case R.id.nav_textToText:
                    selectedFragment = new TextFragment();
                    break;
                case R.id.nav_photoToText:
                    selectedFragment = new PictureFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.release();
    }

    private void textRecognizer() {
        textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        cameraSource = new CameraSource.Builder(getApplicationContext(),
                textRecognizer)
                .setRequestedPreviewSize(1280, 1024)
                .build();
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
        try {
            cameraSource.start(surfaceView.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {
            }
            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections)
            {
                SparseArray<TextBlock> sparseArray = detections.getDetectedItems();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i<sparseArray.size(); ++i){
                    TextBlock textBlock = sparseArray.valueAt(i);
                    if (textBlock != null && textBlock.getValue() !=null){
                        stringBuilder.append(textBlock.getValue() + " ");
                    }
                }
                final String stringText = stringBuilder.toString();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        stringResult = stringText;
                    }
                });
            }
        });
    }
    private void resultObtained(){
        textView = findViewById(R.id.textView);
        spinPic = (Spinner)findViewById(R.id.pictureTextChoose);
        originalText =stringResult;
        Translation translation = translate.translate(originalText,
                Translate.TranslateOption.targetLanguage(Languages.get(spinPic.getSelectedItem().toString())), Translate.TranslateOption.model("base"));
        translatedText = translation.getTranslatedText();
        textView.setText(translatedText);
    }
    public void buttonStart(View view){
        textRecognizer();
        if (checkInternetConnection() && stringResult!=null) {
            //If there is internet connection, get translate service and start
            translation:
            getTranslateService();
            resultObtained();
        } else {
            if(stringResult!=null){
                textView = findViewById(R.id.textView);
                textView.setText("no connection");
            }
        }
    }
    public void getTranslateService() {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try (InputStream is = getResources().openRawResource(R.raw.credentials)) {
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            TranslateOptions translateOptions =
                    TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public void translate() {
        spinText = (Spinner)findViewById(R.id.regtextChoose);
        inputToTranslate = findViewById(R.id.inputToTranslate);
        translatedTv = findViewById(R.id.translatedTv);
        originalText = inputToTranslate.getText().toString();
        Translation translation = translate.translate(originalText,
                Translate.TranslateOption.targetLanguage(Languages.get(spinText.getSelectedItem().toString())), Translate.TranslateOption.model("base"));
        translatedText = translation.getTranslatedText();
        translatedTv.setText(translatedText);
    }
    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        connected =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() ==
                        NetworkInfo.State.CONNECTED ||

                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() ==
                                NetworkInfo.State.CONNECTED;
        return connected;
    }
    public void translateButton(View view){
        if (checkInternetConnection()) {

            getTranslateService();
            translate();
        } else {

            translatedTv.setText("no connection");
        }
    }
    public void pictureSpeaker(View view){
        textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, null);
    }
    public void textSpeaker(View view){
        textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
