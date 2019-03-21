package in.geekofia.hi_geekofia;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech gTTS;
    private SpeechRecognizer gSpeechRecognizer;
    private Intent intent_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_main = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent_main.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent_main.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                gSpeechRecognizer.startListening(intent_main);
            }
        });

        initTextToSpeech();
        initSpeechRecognizer();
    }

    private void initTextToSpeech() {
        gTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(gTTS.getEngines().size() == 0){
                    Toast.makeText(MainActivity.this, "There is no TTS engine on your device", Toast.LENGTH_LONG).show();
                }else{
                    gTTS.setLanguage(Locale.getDefault());
                    speak("Hi! How Can I Help You");
                }
            }
        });
    }

    private void initSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            gSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            gSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result = results.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    processresult(result.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processresult(String command) {
        command = command.toLowerCase();

        if(command.indexOf("what") == -1){
            if(command.indexOf("your name") == -1){
                speak("My Name Is Monika !");
            }if(command.indexOf("time") == -1){
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("Now the time is " + time);
            }
        }else if(command.indexOf("open") == -1){
            if(command.indexOf("brower") == -1){
                Intent intent_02 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://geekofia.in"));
                startActivity(intent_02);
            }
        }

    }

    private void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21) {
            gTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }else {
            gTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gTTS.shutdown();
    }
}
