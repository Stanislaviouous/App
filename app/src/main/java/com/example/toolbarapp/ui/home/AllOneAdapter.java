package com.example.toolbarapp.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolbarapp.R;
import com.example.toolbarapp.ui.profile.ProfileNewsAdapter;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AllOneAdapter extends RecyclerView.Adapter<AllOneAdapter.ViewHolder>{


    private ArrayList<RecyclerNewsItem> listItems;

    NewsFormat newsFormat = new NewsFormat();
    RecyclerImageViewAdapter listOfImage;
    RecyclerVideoViewAdapter listOfVideo;

    ArrayList<String> stringArrayList;
    ArrayList<String> stringArrayListVideo;

    private Context mContext;
    static int port = 0;


    public AllOneAdapter(ArrayList<RecyclerNewsItem> listItems, Context mContext, int i) {
        this.listItems = listItems;
        this.mContext = mContext;
        port = i;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_news_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {
        final RecyclerNewsItem item = listItems.get(position);
        stringArrayList = new ArrayList<>();
        stringArrayList = new ArrayList<>(item.getMediaUrl());
        stringArrayListVideo = new ArrayList<>(item.getVideos());

        if (port == 1){
            Picasso.get().load(item.getGroupPhotoUrl()).into(holder.circleImageView);
            holder.title.setText(newsFormat.titleFormat(item.getGroupName()));
            holder.date.setText(String.valueOf(item.getDate()));
            holder.text.setText(newsFormat.textFormat(item.getText()));

            if (stringArrayList.size() >= 1){
                holder.recyclerViewImage.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                holder.recyclerViewImage.setHasFixedSize(true);

                listOfImage = new RecyclerImageViewAdapter(mContext, stringArrayList, holder, 1);
                holder.recyclerViewImage.setAdapter(listOfImage);
            }

            stringArrayListVideo.size();
            if(stringArrayListVideo.size() >= 1){

                holder.getRecyclerViewVideo.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                holder.getRecyclerViewVideo.setHasFixedSize(true);

                listOfVideo = new RecyclerVideoViewAdapter(mContext, stringArrayListVideo, holder);
                holder.getRecyclerViewVideo.setAdapter(listOfVideo);
            }
        }
        stringArrayList = new ArrayList<>();
        stringArrayListVideo = new ArrayList<>();
    }
    public static void classicNews(RecyclerNewsItem item, ProfileNewsAdapter.ViewHolder holder, NewsFormat newsFormat){
        Picasso.get().load(item.getGroupPhotoUrl()).into(holder.circleImageView);
        holder.title.setText(item.getGroupName());
        holder.date.setText(String.valueOf(item.getDate()));
        holder.text.setText(newsFormat.textFormat(item.getText()));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtOptionDigit;
        public CircleImageView circleImageView;
        public TextView title;
        public TextView date;
        public TextView text;
        public RecyclerView recyclerViewImage;
        public RecyclerView getRecyclerViewVideo;
        public ImageView imageAll;
        public ImageView image;

        public CardView cardView;

        public static int HEIGHT,
                WIDTH;



        public ViewHolder(View itemView) {
            super(itemView);


            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageView1);
            title = (TextView) itemView.findViewById(R.id.txtTitle1);
            date = (TextView) itemView.findViewById(R.id.txtdate1);
            text = (TextView) itemView.findViewById(R.id.txtDescription1);
            txtOptionDigit = (TextView) itemView.findViewById(R.id.txtOptionDigit1);
            image = itemView.findViewById(R.id.recy_image);
            recyclerViewImage = itemView.findViewById(R.id.recyclerViewImages);
            getRecyclerViewVideo = itemView.findViewById(R.id.recyclerViewVideo);
            cardView = itemView.findViewById(R.id.CardY);

            HEIGHT = itemView.getHeight();
            WIDTH = itemView.getWidth();

        }
    }
}
