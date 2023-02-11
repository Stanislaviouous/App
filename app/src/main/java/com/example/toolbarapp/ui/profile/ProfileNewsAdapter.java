package com.example.toolbarapp.ui.profile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolbarapp.MainActivity;
import com.example.toolbarapp.R;
import com.example.toolbarapp.ui.home.AllOneNewsActivity;
import com.example.toolbarapp.ui.home.NewsAdapter;
import com.example.toolbarapp.ui.home.NewsFormat;
import com.example.toolbarapp.ui.home.RecyclerNewsItem;
import com.example.toolbarapp.ui.home.news.NewsStencil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.toolbarapp.ui.profile.PeopleTask.recyclerNewsItem;
import static com.example.toolbarapp.ui.profile.ProfileFragment.*;

public class ProfileNewsAdapter extends RecyclerView.Adapter<ProfileNewsAdapter.ViewHolder>{


    private ArrayList<RecyclerNewsItem> listItems;
    private Context mContext;
    int typ;
    View v;
    int var;
    public static DatabaseReference myOPer1;
    public static DatabaseReference myOPerqw2;
    public static DatabaseReference myOPerte3;
    public static DatabaseReference myOPergh4;
    public static DatabaseReference myOPerqwd6;




    public ProfileNewsAdapter(ArrayList<RecyclerNewsItem> listItems, Context mContext, int type, int ew) {
        this.listItems = listItems;
        this.mContext = mContext;
        typ = type;
        var = ew;
    }

    @NotNull
    @Override
    public ProfileNewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_profile, parent, false);
        return new ProfileNewsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final RecyclerNewsItem item = listItems.get(position);
        NewsFormat newsFormat = new NewsFormat();

        NewsAdapter.classicNews(item, holder, newsFormat);
        try {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Gson gson = new Gson();
                    if (holder.d == 1) {
                        PROGRESS.add(0, TODO.get(position));

                        myOPer1 = referenceRoot.child("TODO");
                        final String er = profileItemListTODO.get(position).getOwnerId() + " " + profileItemListTODO.get(position).getId();
                        myOPer1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                HashMap<String, String> toDoList = new HashMap<>();
                                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                    toDoList.put(childDataSnapshot.getKey(), String.valueOf(childDataSnapshot.getValue()));
                                }
                                toDoList.remove(er);
                                myOPer1.setValue(toDoList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NotNull DatabaseError databaseError) {
                            }
                        });

                        profileItemListPROGRESS.add(0, profileItemListTODO.get(position));
                        adapterPROGRESS.notifyItemInserted(0);
                        adapterPROGRESS.notifyDataSetChanged();

                        myOPerqw2 = referenceRoot.child("PROGRESS");
                        final NewsStencil newsStencil = new NewsStencil(
                                profileItemListPROGRESS.get(0).getId(),
                                profileItemListPROGRESS.get(0).getOwnerId(),
                                profileItemListPROGRESS.get(0).getGroupPhotoUrl(),
                                profileItemListPROGRESS.get(0).getGroupName(),
                                String.valueOf(profileItemListPROGRESS.get(0).getDate()),
                                profileItemListPROGRESS.get(0).getText(),
                                profileItemListPROGRESS.get(0).getMediaUrl(),
                                profileItemListPROGRESS.get(0).getVideos(),
                                profileItemListPROGRESS.get(0).getLink());
                        myOPerqw2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                HashMap<String, String> toDoList = new HashMap<>();
                                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                    toDoList.put(childDataSnapshot.getKey(), String.valueOf(childDataSnapshot.getValue()));
                                }

                                String json = gson.toJson(newsStencil);
                                toDoList.put(er, String.valueOf(json));

                                myOPerqw2.setValue(toDoList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NotNull DatabaseError databaseError) {
                            }
                        });
                        TODO.remove(position);
                        profileItemListTODO.remove(position);
                        adapterTODO.notifyItemRemoved(position);
                        adapterTODO.notifyDataSetChanged();

                    }
                    if (holder.d == 2) {
                        ProfileFragment.COMPLETE.add(0, ProfileFragment.PROGRESS.get(position));

                        myOPerte3 = referenceRoot.child("PROGRESS");
                        final String er = profileItemListPROGRESS.get(position).getOwnerId() + " " + profileItemListPROGRESS.get(position).getId();
                        myOPerte3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                HashMap<String, String> toDoList = new HashMap<>();
                                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                    toDoList.put(childDataSnapshot.getKey(), String.valueOf(childDataSnapshot.getValue()));
                                }
                                toDoList.remove(er);
                                myOPerte3.setValue(toDoList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NotNull DatabaseError databaseError) {
                            }
                        });

                        profileItemListCOMPLETE.add(0, profileItemListPROGRESS.get(position));
                        adapterCOMPLETE.notifyItemInserted(0);
                        adapterCOMPLETE.notifyDataSetChanged();

                        myOPergh4 = referenceRoot.child("COMPLETE");
                        final NewsStencil newsStencil = new NewsStencil(
                                profileItemListCOMPLETE.get(0).getId(),
                                profileItemListCOMPLETE.get(0).getOwnerId(),
                                profileItemListCOMPLETE.get(0).getGroupPhotoUrl(),
                                profileItemListCOMPLETE.get(0).getGroupName(),
                                String.valueOf(profileItemListCOMPLETE.get(0).getDate()),
                                profileItemListCOMPLETE.get(0).getText(),
                                profileItemListCOMPLETE.get(0).getMediaUrl(),
                                profileItemListCOMPLETE.get(0).getVideos(),
                                profileItemListCOMPLETE.get(0).getLink());
                        myOPergh4.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                HashMap<String, String> toDoList = new HashMap<>();
                                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                    toDoList.put(childDataSnapshot.getKey(), String.valueOf(childDataSnapshot.getValue()));
                                }

                                String json = gson.toJson(newsStencil);
                                toDoList.put(er, String.valueOf(json));

                                myOPergh4.setValue(toDoList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NotNull DatabaseError databaseError) {
                            }
                        });
                        PROGRESS.remove(position);
                        ProfileFragment.profileItemListPROGRESS.remove(position);
                        adapterPROGRESS.notifyItemRemoved(position);
                        adapterPROGRESS.notifyDataSetChanged();

                    }
                    if (holder.d == 3) {

                        recyclerNewsItem = COMPLETE.get(position);
                        Intent intent = new Intent(mContext, PeopleTask.class);
                        mContext.startActivity(intent);

                        myOPerqwd6 = referenceRoot.child("COMPLETE");
                        final String er = profileItemListCOMPLETE.get(position).getOwnerId() + " " + profileItemListCOMPLETE.get(position).getId();
                        myOPerqwd6.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                HashMap<String, String> toDoList = new HashMap<>();
                                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                    toDoList.put(childDataSnapshot.getKey(), String.valueOf(childDataSnapshot.getValue()));
                                }
                                toDoList.remove(er);
                                myOPerqwd6.setValue(toDoList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NotNull DatabaseError databaseError) {
                            }
                        });

                        COMPLETE.remove(position);
                        profileItemListCOMPLETE.remove(position);
                        adapterCOMPLETE.notifyItemRemoved(position);
                        adapterCOMPLETE.notifyDataSetChanged();
                    }

                }
            });
        } catch (Exception e) {
            Intent mStartActivity = new Intent(root.getContext(), MainActivity.class);
            int IntentId = 123456;
            PendingIntent Intent = PendingIntent.getActivity(root.getContext(), IntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgrt = (AlarmManager)root.getContext().getSystemService(Context.ALARM_SERVICE);
            mgrt.set(AlarmManager.RTC, System.currentTimeMillis() + 100, Intent);
            System.exit(0);
        }
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionSource(position);
            }
        });
    }
    public void functionSource(int position){
            AllOneNewsActivity.upItem(listItems.get(position));
            Intent intent = new Intent(mContext, AllOneNewsActivity.class);
            mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView circleImageView;
        public TextView title;
        public TextView date;
        public TextView text;
        public ImageView imageView;
        int d = 0;


        public ViewHolder(View itemView) {
            super(itemView);

            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageView1);
            title = (TextView) itemView.findViewById(R.id.txtTitle1);
            date = (TextView) itemView.findViewById(R.id.txtdate1);
            text = (TextView) itemView.findViewById(R.id.txtDescription1);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            d = var;
        }
    }
}
