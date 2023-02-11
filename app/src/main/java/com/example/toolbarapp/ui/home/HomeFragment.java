package com.example.toolbarapp.ui.home;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.example.toolbarapp.MainActivity;
import com.example.toolbarapp.R;
import com.example.toolbarapp.ui.home.news.MyWorker;
import com.example.toolbarapp.ui.home.news.NewsStencil;
import com.example.toolbarapp.ui.search.SearchFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;


public class HomeFragment extends Fragment {

    static public FirebaseAuth mAuthHome;
    public static DatabaseReference ui;
    public static DatabaseReference ty;
    EditText editText;
    public static FragmentTransaction flup;

    public static String newsUrl = "=site:   11234423423   1241345   435345   vk:   27770435   144626470   120099959   96626248   13050764";

    public final static String todoFile = "TODO.txt";
    public final static String progressFile = "PROGRESS.txt";
    public final static String completeFile = "COMPLETE.txt";
    public static FileOutputStream fosTODO = null;
    public static FileOutputStream fosPROGRESS = null;
    public static FileOutputStream fosCOMPLETE = null;

    public static String news = "";
    ArrayList<NewsStencil> listOfStensil = new ArrayList<>();

    ArrayList<NewsStencil> list = new ArrayList<>();

    ArrayList<RecyclerNewsItem> recyclerNewsItemList;
    ArrayList<RecyclerNewsItem> nowNews;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    TextView textView;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerViewNews);
        textView = (TextView) root.findViewById(R.id.textView);
        editText = (EditText) root.findViewById(R.id.editText);

        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerNewsItemList = new ArrayList<>();

        mAuthHome = FirebaseAuth.getInstance();
        ui = FirebaseDatabase.getInstance().getReference().child("News");
        ty = FirebaseDatabase.getInstance().getReference().child("News");
        String currentUserIDHome = Objects.requireNonNull(mAuthHome.getCurrentUser()).getUid();
        DatabaseReference rootRefHome = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserIDHome);

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) || (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT)) {
                    assert getFragmentManager() != null;
                    flup = getFragmentManager().beginTransaction();
                    SearchFragment.str = editText.getText().toString();
                    flup.replace(R.id.nav_host_fragment, new SearchFragment());
                    flup.commit();
                    return true;
                }
                return false;
            }
        }
        );


        try {
            updateNews();

            ui.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    NewsStencil newsStencil;
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        newsStencil = gson.fromJson(Objects.requireNonNull(childDataSnapshot.getValue()).toString(), NewsStencil.class);
                        listOfStensil.add(newsStencil);
                    }
                    for (int i = 0; i < listOfStensil.size(); i++) {
                        recyclerNewsItemList.add(
                                new RecyclerNewsItem(
                                        listOfStensil.get(i).getId(),
                                        listOfStensil.get(i).getOwnerId(),
                                        listOfStensil.get(i).groupPhotoUrl,
                                        listOfStensil.get(i).getGroupName(),
                                        Long.parseLong(listOfStensil.get(i).getDate()),
                                        listOfStensil.get(i).getText(),
                                        listOfStensil.get(i).getMediaUrl(),
                                        listOfStensil.get(i).getVideos(),
                                        listOfStensil.get(i).getLink()));
                    }
                    nowNews = new ArrayList<>();
                    Collections.sort(recyclerNewsItemList);
                    for (int i = recyclerNewsItemList.size() - 1; i >= 0; i--) {
                        nowNews.add(recyclerNewsItemList.get(i));
                    }
                    adapter = new NewsAdapter(nowNews, root.getContext(), 0);
                    recyclerView.setAdapter(adapter);
                    listOfStensil = new ArrayList<>();
                    nowNews = new ArrayList<>();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ui.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class).build();
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            NewsStencil newsStencil;
                            try {
                                JSONArray jsonObject = new JSONArray(HomeFragment.news);
                                System.out.println(jsonObject.toString(2));
                                for (int i = 0; i < jsonObject.length(); i++) {
                                    newsStencil = gson.fromJson(jsonObject.get(i).toString(), NewsStencil.class);
                                    list.add(newsStencil);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            WorkManager.getInstance().enqueue(work);

                            HashMap<String, String> map = new HashMap<>();

                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                GsonBuilder builde1r = new GsonBuilder();
                                Gson gso1n = builder.create();
                                map.put(childDataSnapshot.getKey(), childDataSnapshot.getValue().toString());
                            }
                            System.out.println(map);
                            for (int i = 0; i < list.size(); i++) {
                                if (!map.containsKey(list.get(i).getOwnerId() + " " + list.get(i).getId())) {

                                    String jso1n = gson.toJson(list.get(i));
                                    map.put(list.get(i).getOwnerId() + " " + list.get(i).getId(), jso1n);
                                }
                            }
                            System.out.println(map);
                            ty.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
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
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //  Функция обновления новостей, которые мы получаем с сервера
    public void updateNews(){

        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        NewsStencil newsStencil;
        try {
            JSONArray jsonObject = new JSONArray(HomeFragment.news);
            for (int i = 0; i < jsonObject.length(); i++) {
                newsStencil = gson.fromJson(jsonObject.get(i).toString(), NewsStencil.class);
                listOfStensil.add(newsStencil);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WorkManager.getInstance().enqueue(work);
    }
}
