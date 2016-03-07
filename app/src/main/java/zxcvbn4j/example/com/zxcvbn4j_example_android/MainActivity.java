package zxcvbn4j.example.com.zxcvbn4j_example_android;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private static final Zxcvbn ZXCVBN = new Zxcvbn();

    private EditText editText;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.edit_text);
        editText.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.addTextChangedListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_horizontal);
        progressBar.setMax(4);
        progressBar.setProgress(0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(final Editable s) {
        MeasureTask task = new MeasureTask(progressBar);
        task.execute(s.toString());
    }

    private static class MeasureTask extends AsyncTask<String, Void, Void> {

        private final ProgressBar progressBar;

        public MeasureTask(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        protected Void doInBackground(String... params) {
            final Strength strength = ZXCVBN.measure(params[0]);
            MainActivity.HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    int score = strength.getScore();
                    Log.d(MeasureTask.class.getName(), "score: " + score);
                    progressBar.setProgress(score);
                }
            });
            return null;
        }
    }
}
