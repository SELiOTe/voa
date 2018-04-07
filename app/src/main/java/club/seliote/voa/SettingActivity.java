package club.seliote.voa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    private ImageView mBackImageView;
    private TextView mTitleTextVIew;
    private TextView mSettingContentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_setting);

        mBackImageView  = (ImageView) this.findViewById(R.id.back_image_view);
        mTitleTextVIew = (TextView) this.findViewById(R.id.title_text_view);
        mSettingContentTextView = this.findViewById(R.id.setting_content_text_view);
        mSettingContentTextView.setTextIsSelectable(true);

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
    }
}
