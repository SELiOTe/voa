package club.seliote.voa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import club.seliote.voa.ArticleActivityUtils.ArticleHolder;
import club.seliote.voa.ArticleActivityUtils.GetArticleByDate;
import club.seliote.voa.ArticleActivityUtils.GetMedia;
import club.seliote.voa.ArticleActivityUtils.RecyclerViewAdapter;
import club.seliote.voa.GlobalUtils.ConstantValue;
import club.seliote.voa.GlobalUtils.GlobalContextApplication;
import club.seliote.voa.GlobalUtils.LogToFile;
import club.seliote.voa.GlobalUtils.NetworkStatus;

public class ArticleActivity extends AppCompatActivity {

    private ImageView mBackImageView;
    private RecyclerView mRecyclerView;
    private SeekBar mSeekBar;
    private TextView mCurrentTimeTextView;
    private TextView mMaxTimeTextView;
    private ImageButton mMediaStatusImageButton;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private TextView mTitleTextView;

    private MediaPlayer mMediaPlayer = new MediaPlayer();;

    private boolean isFirstClickPlay = true;
    private Date mDate;
    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_article);

        mBackImageView = (ImageView) this.findViewById(R.id.article_activity_back);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.article_recycler_view);
        mSeekBar = (SeekBar) this.findViewById(R.id.media_player_seek_bar);
        mMediaStatusImageButton = (ImageButton) this.findViewById(R.id.media_status_iamge_view);
        mCurrentTimeTextView = (TextView) this.findViewById(R.id.media_current_progress_text_view);
        mMaxTimeTextView = (TextView) this.findViewById(R.id.media_max_progress_text_view);
        mTitleTextView = (TextView) this.findViewById(R.id.title_article_activity_text_view);

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleActivity.this.finish();
            }
        });

        mDate = (Date) this.getIntent().getSerializableExtra(ConstantValue.VOA_LIST_ACTIVITY_USER_SELECTED_DATE);
        ArticleHolder articleHolder = GetArticleByDate.get(mDate);
        if (NetworkStatus.get() == ConstantValue.NETWORK_CLOSE && articleHolder.getEnglishList().size() == 0) {
            Toast.makeText(this, "本地无缓存且网络未连接，请联网后再戳我～", Toast.LENGTH_LONG).show();
            this.finish();
        }

        mTitleTextView.setText(new SimpleDateFormat("yyyy-MM-dd").format(mDate));

        this.mediaPlayPrepare();
        mMediaStatusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaStatusImageButton.setImageResource(R.drawable.ic_media_stop);
                    mMediaPlayer.pause();
                } else {
                    mMediaStatusImageButton.setImageResource(R.drawable.ic_media_start);
                    mMediaPlayer.start();
                }
            }
        });

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (mSeekBar.getProgress() < mSeekBar.getMax()) {
                        final int currentPosition = mMediaPlayer.getCurrentPosition();
                        ArticleActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArticleActivity.this.mSeekBar.setProgress(currentPosition);
                                ArticleActivity.this.mCurrentTimeTextView.setText(ArticleActivity.this.millisecondsToString(currentPosition));
                            }
                        });
                        Thread.sleep(1000);
                    }
                } catch (Exception exp) {
                }
            }
        });
        mThread.start();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do noting yet
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress());
                Toast.makeText(GlobalContextApplication.getContext(), "缓冲中～请稍等～", Toast.LENGTH_LONG).show();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerViewAdapter = new RecyclerViewAdapter(articleHolder);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ConstantValue.PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.mediaPlayPrepare();
                } else {
                    Toast.makeText(this, "哼！小气鬼！还说爱我？内存卡都不让我进去！生气ing...", Toast.LENGTH_LONG).show();
                    this.finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
        mThread.interrupt();
    }

    private void mediaPlayPrepare () {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ConstantValue.PERMISSION_WRITE_EXTERNAL_STORAGE);
        } else {
            GetMedia getMedia = new GetMedia(mDate);
            File file = getMedia.get();
            if (file != null) {
                // play in local
                try {
                    mMediaPlayer.setDataSource(file.getPath());
                    mMediaPlayer.prepare();
                    mSeekBar.setMax(mMediaPlayer.getDuration());
                    mMaxTimeTextView.setText(this.millisecondsToString(mMediaPlayer.getDuration()));
                } catch (IOException exp) {
                    LogToFile.e("play media in local error, date: " + mDate.toString());
                    Toast.makeText(this, "音频播放出错！", Toast.LENGTH_LONG).show();
                }
            } else {
                // must execute in ui thread!!!
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    mMediaPlayer.setDataSource(ConstantValue.GET_MEDIA_BASE_URL + simpleDateFormat.format(mDate) + ".mp3");
                    mMediaPlayer.prepare();
                    mSeekBar.setMax(mMediaPlayer.getDuration());
                    mMaxTimeTextView.setText(this.millisecondsToString(mMediaPlayer.getDuration()));
                } catch (IOException exp) {
                    LogToFile.e("play media in internet error, date: " + mDate.toString());
                    ArticleActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ArticleActivity.this, "音频播放出错！", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }

    private String millisecondsToString(int millisecondsDuration) {
        int totalSeconds = millisecondsDuration / 1000;
        int minutes = totalSeconds / 60;
        int remain_second = totalSeconds % 60;
        return minutes + ":" + (("" + remain_second).length() == 1 ? "0" + remain_second : remain_second);
    }
}
