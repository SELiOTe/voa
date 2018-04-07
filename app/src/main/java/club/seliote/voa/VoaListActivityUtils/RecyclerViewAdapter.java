package club.seliote.voa.VoaListActivityUtils;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import club.seliote.voa.ArticleActivity;
import club.seliote.voa.GlobalUtils.ConstantValue;
import club.seliote.voa.R;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<Date> mDates;

    public RecyclerViewAdapter(List<Date> dates) {
        mDates = dates;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_voa_list_item, parent, false);
        final RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ArticleActivity.class);
                Date clickedDate = mDates.get(recyclerViewHolder.getAdapterPosition());
                intent.putExtra(ConstantValue.VOA_LIST_ACTIVITY_USER_SELECTED_DATE, clickedDate);
                v.getContext().startActivity(intent);
            }
        });
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        holder.setTextView(simpleDateFormat.format(mDates.get(position)));
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            // 与Activity不在同一个包中，所以需要import club.seliote.voa.R;
            mTextView = (TextView) itemView.findViewById(R.id.list_item_text_view);
        }

        public void setTextView(String text) {
            mTextView.setText(text);
        }
    }

}
