package ru.rsue.android.droidquest;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.PrivateKey;

public class MainActivity extends AppCompatActivity {
    private Button mDaButton;
    private Button mNetButton;
    private ImageButton mNextImageButton;
    private ImageButton mBackImageButton;
    private Button mDeceitButton;
    private TextView mQuestionTextView;
    private static final String TAG = "MainActivity";
    private  static  final  String KEY_INDEX = "index";

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() вызван");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() вызван");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() вызван");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() вызван");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() вызван");
    }

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_android, true),
            new Question(R.string.question_linear, false),
            new Question(R.string.question_service, false),
            new Question(R.string.question_res, true),
            new Question( R.string.question_manifest, true),
            new Question( R.string.question_1, true),
            new Question( R.string.question_2, true),
            new Question( R.string.question_3, true),
            new Question( R.string.question_4, true),
            new Question( R.string.question_5, true),
    };
    private int mCurrentIndex = 0;
    private boolean mIsDeceiter;

    private  void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnsverTrue();
        int massageResId = 0;
        if (mIsDeceiter) {
            massageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                massageResId = R.string.corr;
            } else {
                massageResId = R.string.incorr;
            }
        }
        Toast.makeText(this, massageResId, Toast.LENGTH_SHORT).show();
    }

    private static final int REQUEST_CODE_DECEIT = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle)вызван");
        setContentView(R.layout.activity_main);
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mDaButton = (Button)findViewById(R.id.Who);
        mDaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mNetButton = (Button)findViewById(R.id.Net);
        mNetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        mNextImageButton = (ImageButton)findViewById(R.id.Next);
        mNextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsDeceiter = false;
                updateQuestion();
            }
        });
        mBackImageButton = (ImageButton)findViewById(R.id.Back);
        mBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                if (mCurrentIndex == -1) mCurrentIndex = mQuestionBank.length - 1;
                updateQuestion();
            }
        });

        mDeceitButton = (Button)findViewById(R.id.deceit_button);
        mDeceitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnsverTrue();
                Intent i = DeceitActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_DECEIT);
            }
        });
        updateQuestion();

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {return;}
        if (requestCode == REQUEST_CODE_DECEIT) {
            if (data == null) {return;}
            mIsDeceiter = DeceitActivity.wasAnswerShown(data);
        }
    }

    @Override
    public  void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSavedInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }
}
