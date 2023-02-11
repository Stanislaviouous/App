package com.example.toolbarapp.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolbarapp.R;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerImageViewAdapter extends RecyclerView.Adapter<RecyclerImageViewAdapter.ViewHolder> {


    private ArrayList<String> ImageUrls = new ArrayList<>();
    private Context mContext;
    NewsAdapter.ViewHolder holderFall;
    AllOneAdapter.ViewHolder holderFallAll;
    public int j;

    public RecyclerImageViewAdapter(Context context, ArrayList<String> imageUrls, NewsAdapter.ViewHolder holderFalling, int type) {
        ImageUrls = imageUrls;
        mContext = context;
        holderFall = holderFalling;
        j = type;
    }
    public RecyclerImageViewAdapter(Context context, ArrayList<String> imageUrls, AllOneAdapter.ViewHolder holderFalling, int type) {
        ImageUrls = imageUrls;
        mContext = context;
        holderFallAll = holderFalling;
        j = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_image_view, parent, false);
        if (j == 0){
            return new ViewHolder(view, holderFall);
        }
        if (j == 1){
            return new ViewHolder(view, holderFallAll);
        }
        return new ViewHolder(view, holderFall);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {

        if (j == 0){
            Picasso.get().load(ImageUrls.get(position)).into(holder.image);

        }
        if (j == 1){
            holder.image.setMinimumWidth(NewsAdapter.ViewHolder.WIDTH - 2);
            Picasso.get().load(ImageUrls.get(position)).into(holder.image);
        }
    }


    @Override
    public int getItemCount() {
        return ImageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ImageView imageAll;
        public ViewHolder(View itemView, NewsAdapter.ViewHolder viewHolder) {
            super(itemView);
            imageAll = viewHolder.imageAll;
            image = itemView.findViewById(R.id.recy_image);
        }

        public ViewHolder(View itemView, AllOneAdapter.ViewHolder viewHolder) {
            super(itemView);
            imageAll = viewHolder.imageAll;
            image = itemView.findViewById(R.id.recy_image);
        }
    }
}
