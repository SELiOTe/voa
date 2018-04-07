package club.seliote.voa.ArticleActivityUtils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import club.seliote.voa.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<String> englishList;
    private List<String> chineseList;

    public RecyclerViewAdapter(ArticleHolder articleHolder) {
        englishList = articleHolder.getEnglishList();
        chineseList = articleHolder.getChineseList();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_article_item, parent, false);
        final RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(itemView);
        // set listener to R.id.english_text_view not to itemView, otherwise it will no response
        recyclerViewHolder.mEnglishTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentStatus = recyclerViewHolder.mChineseTextView.getVisibility();
                int status = (currentStatus == View.GONE ? View.VISIBLE : View.GONE);
                recyclerViewHolder.mChineseTextView.setVisibility(status);
            }
        });
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.mEnglishTextView.setText(englishList.get(position));
        holder.mChineseTextView.setText(chineseList.get(position));
    }

    @Override
    public int getItemCount() {
        return englishList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView mEnglishTextView;
        TextView mChineseTextView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mEnglishTextView = (TextView) itemView.findViewById(R.id.english_text_view);
            mChineseTextView = (TextView) itemView.findViewById(R.id.chinese_text_view);
            mChineseTextView.setVisibility(View.GONE);
        }
    }

}
