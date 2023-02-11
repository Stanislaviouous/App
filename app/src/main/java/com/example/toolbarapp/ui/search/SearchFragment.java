package com.example.toolbarapp.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolbarapp.R;
import com.example.toolbarapp.ui.home.NewsAdapter;
import com.example.toolbarapp.ui.home.RecyclerNewsItem;
import com.example.toolbarapp.ui.home.news.NewsStencil;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {

    View root;
    public EditText editText;
    ArrayList<NewsStencil> listOfStensil = new ArrayList<>();
    ArrayList<RecyclerNewsItem> recyclerNewsItemList;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    public DatabaseReference low;
    static public String str = "";

    /*public SearchFragment(String stroke) {
        str = stroke;
    }*/

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);

        low = FirebaseDatabase.getInstance().getReference().child("News");
        editText = (EditText) root.findViewById(R.id.editText);
        if (str.length() != 0){
            editText.setText(str);
            final String st = str;
            str = "";
            if (st.length() > 0) {
                listOfStensil = new ArrayList<>();
                recyclerView = (RecyclerView) root.findViewById(R.id.recyclerViewNews);

                recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                recyclerView.setHasFixedSize(true);
                recyclerNewsItemList = new ArrayList<>();
                low.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        NewsStencil newsStencil;
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            newsStencil = gson.fromJson(Objects.requireNonNull(childDataSnapshot.getValue()).toString(), NewsStencil.class);
                            if (inStroke(st, newsStencil.getText())) {
                                listOfStensil.add(newsStencil);
                            }
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
                        adapter = new NewsAdapter(recyclerNewsItemList, root.getContext(), 0);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        else {
            editText.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(final CharSequence s, int start, int before, int count) {

                    if (s.length() > 0) {
                        listOfStensil = new ArrayList<>();
                        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerViewNews);

                        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        recyclerView.setHasFixedSize(true);
                        recyclerNewsItemList = new ArrayList<>();
                        low.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                NewsStencil newsStencil;
                                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    newsStencil = gson.fromJson(Objects.requireNonNull(childDataSnapshot.getValue()).toString(), NewsStencil.class);
                                    if (inStroke(String.valueOf(s), newsStencil.getText())) {
                                        listOfStensil.add(newsStencil);
                                    }
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
                                adapter = new NewsAdapter(recyclerNewsItemList, root.getContext(), 0);
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            });
        }
        return root;
    }

    boolean inStroke(String n, String m) {
        return m.contains(n);
    }
}
