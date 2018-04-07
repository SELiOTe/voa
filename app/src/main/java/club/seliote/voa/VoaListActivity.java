package club.seliote.voa;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import club.seliote.voa.GlobalUtils.ConstantValue;
import club.seliote.voa.GlobalUtils.NetworkStatus;
import club.seliote.voa.VoaListActivityUtils.GetListDate;
import club.seliote.voa.VoaListActivityUtils.ListDataLab;
import club.seliote.voa.VoaListActivityUtils.RecyclerViewAdapter;

public class VoaListActivity extends AppCompatActivity implements View.OnClickListener{


    private ImageView mEXitImageView;
    private TextView mTitleTextView;
    private ImageView mSettingImageVIew;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerViewAdapter mRecyclerViewAdapter;
    List<Date> mDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_voa_list);

        mEXitImageView = (ImageView) this.findViewById(R.id.exit_image_view);
        mTitleTextView = (TextView) this.findViewById(R.id.title_text_view);
        mSettingImageVIew = (ImageView) this.findViewById(R.id.setting_image_view);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.list_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.list_swipe_refresh);

        mEXitImageView.setOnClickListener(this);
        mTitleTextView.setOnClickListener(this);
        mSettingImageVIew.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStatus.get() != ConstantValue.NETWORK_CONNECT) {
                    Snackbar.make(mRecyclerView, R.string.network_close, Snackbar.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                } else {
                    mDates = ListDataLab.getDates(true);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        if (NetworkStatus.get() != ConstantValue.NETWORK_CONNECT) {
            Snackbar.make(mRecyclerView, R.string.network_close, Snackbar.LENGTH_LONG).show();
            mDates = ListDataLab.getDates(false);
        } else {
            mDates = ListDataLab.getDates(true);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerViewAdapter = new RecyclerViewAdapter(mDates);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit_image_view:
                this.finish();
                break;
            case R.id.title_text_view:
                mRecyclerView.scrollToPosition(0);
                break;
            case R.id.setting_image_view:
                this.startActivity(new Intent(this, SettingActivity.class));
                break;
            default:
                break;
        }
    }
}
