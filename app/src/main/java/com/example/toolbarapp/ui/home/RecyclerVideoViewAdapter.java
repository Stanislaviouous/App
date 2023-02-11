package com.example.toolbarapp.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolbarapp.R;

import java.util.ArrayList;

public class RecyclerVideoViewAdapter extends RecyclerView.Adapter<RecyclerVideoViewAdapter.ViewHolder> {


    private ArrayList<String> VideoUrls = new ArrayList<>();
    private Context mContext;
    NewsAdapter.ViewHolder viewHolderPoint;
    AllOneAdapter.ViewHolder viewHolderPointAll;

    public RecyclerVideoViewAdapter(Context context, ArrayList<String> imageUrls, NewsAdapter.ViewHolder viewHolder) {

        VideoUrls = new ArrayList<>(imageUrls);
        mContext = context;
        viewHolderPoint = viewHolder;
    }
    public RecyclerVideoViewAdapter(Context context, ArrayList<String> imageUrls, AllOneAdapter.ViewHolder viewHolder) {
        VideoUrls = imageUrls;
        mContext = context;
        viewHolderPointAll = viewHolder;
    }


    @NonNull
    @Override
    public RecyclerVideoViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_video_view, parent, false);
        return new ViewHolder(view, viewHolderPoint);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        WebSettings webSettings = holder.video.getSettings();
        webSettings.setJavaScriptEnabled(true);

        System.out.println(VideoUrls.get(position) + "width=\"" + 320 + "\" height=\""  + 180 + "\" frameborder=\"0\" allowfullscreen></iframe>");
        holder.video.loadData(VideoUrls.get(position) + "width=\"" + 320 + "\" height=\""  + 180 + "\" frameborder=\"0\" allowfullscreen></iframe>",
                "text/html", "UTF-8");

    }

    @Override
    public int getItemCount() {
        return VideoUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        WebView video;

        public ViewHolder(View itemView, NewsAdapter.ViewHolder viewHolder) {
            super(itemView);
            video = itemView.findViewById(R.id.recy_video);
        }
    }
}
