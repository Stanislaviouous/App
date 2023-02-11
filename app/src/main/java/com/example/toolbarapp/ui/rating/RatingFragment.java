package com.example.toolbarapp.ui.rating;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.toolbarapp.MainActivity;
import com.example.toolbarapp.R;
import com.example.toolbarapp.ui.home.HomeFragment;
import com.google.firebase.database.*;

import java.util.*;

public class RatingFragment extends Fragment {

    HashMap<String, Integer> map = new HashMap<String, Integer>();
    private ArrayList<Rating> ratingList;
    private ArrayList<Rating> rating;
    private DatabaseReference rootRot;
    public static FragmentTransaction flop;
    ListView listView;
    String t;

    String loginRat, countRat, imageUriRat;
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rating, container, false);

        rootRot = FirebaseDatabase.getInstance().getReference();
        listView = root.findViewById(R.id.list_rating);
        ratingUpdate(root);
        
        return root;
    }

    void ratingUpdate(final View root){
        try {
            final DatabaseReference rootRou = rootRot.child("Users");
            ratingList = new ArrayList<>();
            rating = new ArrayList<>();
            rootRot.child("Rating").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String currentUserID = "";

                    for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                        map.put(childDataSnapshot.getKey(), childDataSnapshot.getValue(Integer.class));
                        currentUserID = childDataSnapshot.getKey();
                    }

                    List<Integer> mapValues = new ArrayList<>(map.values());
                    Collections.sort(mapValues);
                    rootRou.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                int r = 1;
                                for (DataSnapshot childDataSnapshotHost : dataSnapshot.getChildren()) {
                                    if (childDataSnapshotHost.child("email").exists() && childDataSnapshotHost.child("image").exists()) {
                                        imageUriRat = (String) childDataSnapshotHost.child("image").getValue();
                                        countRat = (String) childDataSnapshotHost.child("count").getValue();
                                        loginRat = (String) childDataSnapshotHost.child("login").getValue();
                                        t = String.valueOf(r);
                                        ratingList.add(new Rating(t, loginRat, countRat, imageUriRat));
                                        r++;
                                    }
                                }
                                Collections.sort(ratingList);
                                int j = 1;
                                for (int i = ratingList.size() - 1; i >= 0; i--) {
                                    ratingList.get(i).setNumber(String.valueOf(j));
                                    rating.add(ratingList.get(i));
                                    j++;
                                }
                                @NonNull
                                RatingAdapter ratingAdapter = new RatingAdapter(Objects.requireNonNull(getContext()), R.layout.list_item, rating);
                                listView.setAdapter(ratingAdapter);
                            } catch (Exception e) {
                                Intent mStartActivity = new Intent(root.getContext(), MainActivity.class);
                                int IntentId = 123456;
                                PendingIntent Intent = PendingIntent.getActivity(root.getContext(), IntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                                AlarmManager mgrt = (AlarmManager) root.getContext().getSystemService(Context.ALARM_SERVICE);
                                mgrt.set(AlarmManager.RTC, System.currentTimeMillis() + 100, Intent);
                                System.exit(0);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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
    }
}