package com.example.toolbarapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolbarapp.R;
import com.example.toolbarapp.ui.home.news.NewsStencil;
import com.example.toolbarapp.ui.profile.ProfileNewsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.toolbarapp.ui.home.HomeFragment.flup;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{


    private ArrayList<RecyclerNewsItem> listItems;

    public static FragmentTransaction flip;
    static public FirebaseAuth mAuthNewsAdapter;
    private DatabaseReference rootRefNewsAdapter;
    private DatabaseReference referenceDo;

    NewsFormat newsFormat = new NewsFormat();
    RecyclerImageViewAdapter listOfImage;
    RecyclerVideoViewAdapter listOfVideo;

    ArrayList<String> stringArrayList;
    ArrayList<String> stringArrayListVideo;

    private Context mContext;
    static int port = 0;


    public NewsAdapter(ArrayList<RecyclerNewsItem> listItems, Context mContext, int i) {
        this.listItems = listItems;
        this.mContext = mContext;
        port = i;
    }

    @NotNull
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

        if (port == 0){
            Picasso.get().load(item.getGroupPhotoUrl()).into(holder.circleImageView);
            holder.title.setText(item.getGroupName());
            holder.date.setText(newsFormat.dateFormat(item.getDate()));
            String s = newsFormat.textFormat(item.getText());
            System.out.println(s);
            System.out.println(item.getText());
            holder.text.setText(newsFormat.textFormat(s));

            if (stringArrayList.size() >= 1){
                holder.recyclerViewImage.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                holder.recyclerViewImage.setHasFixedSize(true);

                listOfImage = new RecyclerImageViewAdapter(mContext, stringArrayList, holder, 0);
                holder.recyclerViewImage.setAdapter(listOfImage);

            }
            if(stringArrayListVideo.size() >= 1){
                holder.getRecyclerViewVideo.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                holder.getRecyclerViewVideo.setHasFixedSize(true);

                listOfVideo = new RecyclerVideoViewAdapter(mContext, stringArrayListVideo, holder);
                holder.getRecyclerViewVideo.setAdapter(listOfVideo);
            }
            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllOneNewsActivity.upItem(listItems.get(position));
                    Intent intent = new Intent(mContext, AllOneNewsActivity.class);
                    mContext.startActivity(intent);
                }
            });

            // Записываем в базу
            mAuthNewsAdapter = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = mAuthNewsAdapter.getCurrentUser();
            assert firebaseUser != null;
            String currentUserIDNewsAdapter = firebaseUser.getUid();
            rootRefNewsAdapter = FirebaseDatabase.getInstance().getReference("Users").child(currentUserIDNewsAdapter);

            holder.txtOptionDigit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final HashMap<String, String> toDoList = new HashMap<>();
                        PopupMenu popupMenu = new PopupMenu(mContext, holder.txtOptionDigit);
                        popupMenu.inflate(R.menu.news_menu);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.menu_news_save:
                                        referenceDo = rootRefNewsAdapter.child("TODO");
                                        Toast.makeText(mContext, "Сохранено в \" В планах \" ", Toast.LENGTH_LONG).show();
                                        break;
                                    case R.id.menu_news_add:
                                        referenceDo = rootRefNewsAdapter.child("PROGRESS");
                                        Toast.makeText(mContext, "Добавлено в \" В прогрессе \" ", Toast.LENGTH_LONG).show();
                                        break;
                                    case R.id.menu_news_call:
                                        Toast.makeText(mContext, "Связываемся", Toast.LENGTH_LONG).show();
                                        break;
                                }

                                referenceDo.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                            toDoList.put(childDataSnapshot.getKey(), String.valueOf(childDataSnapshot.getValue()));

                                            NewsStencil newsStencil = new NewsStencil(
                                                    listItems.get(position).getId(),
                                                    listItems.get(position).getOwnerId(),
                                                    listItems.get(position).getGroupPhotoUrl(),
                                                    listItems.get(position).getGroupName(),
                                                    String.valueOf(listItems.get(position).getDate()),
                                                    listItems.get(position).getText(),
                                                    listItems.get(position).getMediaUrl(),
                                                    listItems.get(position).getVideos(),
                                                    listItems.get(position).getLink());

                                            Gson gson = new Gson();
                                            String json = gson.toJson(newsStencil);

                                            toDoList.put(listItems.get(position).getOwnerId() + " " + listItems.get(position).getId(), json);
                                            referenceDo.setValue(toDoList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NotNull DatabaseError databaseError) {

                                    }
                                });

                                return false;
                            }
                        });
                        popupMenu.show();
                    } catch (Exception e) {
                        flup.replace(R.id.nav_host_fragment, new HomeFragment());
                        flup.commit();
                    }
                }
            });
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
