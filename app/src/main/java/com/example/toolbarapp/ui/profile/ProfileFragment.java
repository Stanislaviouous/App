package com.example.toolbarapp.ui.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolbarapp.R;
import com.example.toolbarapp.ui.home.NewsFormat;
import com.example.toolbarapp.ui.home.RecyclerNewsItem;
import com.example.toolbarapp.ui.home.news.NewsStencil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import dev.iamsabbir.accordionview.AccordionView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    public static String currentUserID;
    public static FirebaseAuth mAuth;
    public static DatabaseReference rootRef;
    public static DatabaseReference referenceDo;
    public static DatabaseReference referenceRoot;

    static TreeMap<String, String> point = new TreeMap<>();

    public static ArrayList<RecyclerNewsItem> TODO = new ArrayList<>();
    public static ArrayList<RecyclerNewsItem> PROGRESS = new ArrayList<>();
    public static ArrayList<RecyclerNewsItem> COMPLETE = new ArrayList<>();


    private static final int GalleryPick = 1;
    private StorageReference UserProfileImageRef;

    public CircleImageView circleImageView;
    TextView toName, tologinuc, toemailic, tocountic;

    static RecyclerView recyclerPreviewTODO;
    static RecyclerView recyclerPreviewPROGRESS;
    static RecyclerView recyclerPreviewCOMPLETE;

    @SuppressLint("StaticFieldLeak")
    static ProfileNewsAdapter adapterTODO;
    @SuppressLint("StaticFieldLeak")
    static ProfileNewsAdapter adapterPROGRESS;
    @SuppressLint("StaticFieldLeak")
    static ProfileNewsAdapter adapterCOMPLETE;

    static ArrayList<RecyclerNewsItem> profileItemListTODO;
    static ArrayList<RecyclerNewsItem> profileItemListPROGRESS;
    static ArrayList<RecyclerNewsItem> profileItemListCOMPLETE;



    static View root;

    AccordionView accordionView2, accordionView3, accordionView4;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        accordionView2 = (AccordionView) root.findViewById(R.id.accordionView2);
        accordionView3 = (AccordionView) root.findViewById(R.id.accordionView3);
        accordionView4 = (AccordionView) root.findViewById(R.id.accordionView4);

        toName = (TextView) root.findViewById(R.id.TwoName);
        tologinuc = (TextView) root.findViewById(R.id.loginic);
        toemailic = (TextView) root.findViewById(R.id.emailic);
        tocountic = (TextView) root.findViewById(R.id.countic);



        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        verifyUser();

        referenceRoot = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        referenceDo = rootRef.child("Users").child(currentUserID);

        recyclerPreviewTODO = (RecyclerView) root.findViewById(R.id.TODO);
        recyclerPreviewPROGRESS = (RecyclerView) root.findViewById(R.id.PROGRESS);
        recyclerPreviewCOMPLETE = (RecyclerView) root.findViewById(R.id.COMPLETE);

        referenceRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toName.setText(String.valueOf(dataSnapshot.child("username").getValue()));
                tologinuc.setText(String.valueOf(dataSnapshot.child("login").getValue()));
                toemailic.setText(String.valueOf(dataSnapshot.child("email").getValue()));
                tocountic.setText(String.valueOf(dataSnapshot.child("count").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referenceRoot.child("TODO").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                NewsStencil newsStencil = new NewsStencil();
                final Gson gson = new Gson();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if (!String.valueOf(childDataSnapshot.getKey()).equals("1")) {
                        newsStencil = gson.fromJson(String.valueOf(childDataSnapshot.getValue()), NewsStencil.class);
                        RecyclerNewsItem recyclerNewsItem = new RecyclerNewsItem(
                                newsStencil.getId(),
                                newsStencil.getOwnerId(),
                                newsStencil.getGroupPhotoUrl(),
                                newsStencil.getGroupName(),
                            Long.parseLong(newsStencil.getDate()),
                                newsStencil.getText(),
                                newsStencil.getMediaUrl(),
                                newsStencil.getVideos(),
                                newsStencil.getLink());

                        if(point.containsKey(recyclerNewsItem.getOwnerId() + " " + recyclerNewsItem.getId())){

                        }
                        else {
                            point.put(recyclerNewsItem.getOwnerId() + " " + recyclerNewsItem.getId(), "1");
                            TODO.add(recyclerNewsItem);
                        }
                    }
                }
                functionProfileTaskUpdate("TODO");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        referenceRoot.child("PROGRESS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                NewsStencil newsStencil = new NewsStencil();
                final Gson gson = new Gson();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if (!String.valueOf(childDataSnapshot.getKey()).equals("1")) {
                        newsStencil = gson.fromJson(String.valueOf(childDataSnapshot.getValue()), NewsStencil.class);
                        RecyclerNewsItem recyclerNewsItem = new RecyclerNewsItem(
                                newsStencil.getId(),
                                newsStencil.getOwnerId(),
                                newsStencil.getGroupPhotoUrl(),
                                newsStencil.getGroupName(),
                                Long.parseLong(newsStencil.getDate()),
                                newsStencil.getText(),
                                newsStencil.getMediaUrl(),
                                newsStencil.getVideos(),
                                newsStencil.getLink());

                        if(point.containsKey(recyclerNewsItem.getOwnerId() + " " + recyclerNewsItem.getId())){

                        }
                        else {
                            point.put(recyclerNewsItem.getOwnerId() + " " + recyclerNewsItem.getId(), "1");
                            PROGRESS.add(recyclerNewsItem);
                        }
                    }
                }
                functionProfileTaskUpdate("PROGRESS");
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) { }
        });
        referenceRoot.child("COMPLETE").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NewsStencil newsStencil = new NewsStencil();
                final Gson gson = new Gson();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if (!String.valueOf(childDataSnapshot.getKey()).equals("1")) {
                        newsStencil = gson.fromJson(String.valueOf(childDataSnapshot.getValue()), NewsStencil.class);
                        RecyclerNewsItem recyclerNewsItem = new RecyclerNewsItem(
                                newsStencil.getId(),
                                newsStencil.getOwnerId(),
                                newsStencil.getGroupPhotoUrl(),
                                newsStencil.getGroupName(),
                                Long.parseLong(newsStencil.getDate()),
                                newsStencil.getText(),
                                newsStencil.getMediaUrl(),
                                newsStencil.getVideos(),
                                newsStencil.getLink());

                        if(point.containsKey(recyclerNewsItem.getOwnerId() + " " + recyclerNewsItem.getId())){

                        }
                        else {
                            point.put(recyclerNewsItem.getOwnerId() + " " + recyclerNewsItem.getId(), "1");
                            COMPLETE.add(recyclerNewsItem);
                        }
                    }
                }
                functionProfileTaskUpdate("COMPLETE");
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) { }
        });

        verifyUser();

        circleImageView = (CircleImageView) root.findViewById(R.id.profile_image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1).start(Objects.requireNonNull(getContext()), this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                Uri resultUri = result.getUri();
                final StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                rootRef.child("Users").child(currentUserID).child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            verifyUser();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }

    private void verifyUser() {
        String currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("email").exists() && dataSnapshot.child("image").exists()){
                    String reteievImage = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                    Picasso.get().load(reteievImage).into(circleImageView);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    void functionProfileTaskUpdate(String nameStage){
        ArrayList<RecyclerNewsItem> faceLess = new ArrayList<>();
        int l;
        switch (nameStage){
            case "TODO":
                if (ProfileFragment.TODO != null){
                    faceLess = new ArrayList<RecyclerNewsItem>(ProfileFragment.TODO);
                    l = 1;
                    if (faceLess != null) {
                        recyclerPreviewTODO.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        recyclerPreviewTODO.setHasFixedSize(true);
                        profileItemListTODO = new ArrayList<>();

                        for (int i = faceLess.size() - 1; i >= 0; i--) {
                            profileItemListTODO.add(new RecyclerNewsItem(faceLess.get(i).getId(), faceLess.get(i).getOwnerId(), faceLess.get(i).getGroupPhotoUrl(), faceLess.get(i).getGroupName(), faceLess.get(i).getDate(), faceLess.get(i).getText(), faceLess.get(i).getMediaUrl(), faceLess.get(i).getVideos()));
                        }

                        adapterTODO = new ProfileNewsAdapter(profileItemListTODO, root.getContext(), 1, l);
                        recyclerPreviewTODO.setAdapter(adapterTODO);
                    }
                    break;
                }
            case "PROGRESS":
                if (ProfileFragment.PROGRESS != null){
                    faceLess = new ArrayList<RecyclerNewsItem>(ProfileFragment.PROGRESS);
                    l = 2;
                    if (faceLess != null) {
                        recyclerPreviewPROGRESS.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        recyclerPreviewPROGRESS.setHasFixedSize(true);
                        profileItemListPROGRESS = new ArrayList<>();

                        for (int i = faceLess.size() - 1; i >= 0; i--) {
                            profileItemListPROGRESS.add(new RecyclerNewsItem(faceLess.get(i).getId(), faceLess.get(i).getOwnerId(), faceLess.get(i).getGroupPhotoUrl(), faceLess.get(i).getGroupName(), faceLess.get(i).getDate(), faceLess.get(i).getText(), faceLess.get(i).getMediaUrl(), faceLess.get(i).getVideos()));
                        }

                        adapterPROGRESS = new ProfileNewsAdapter(profileItemListPROGRESS, root.getContext(), 1, l);
                        recyclerPreviewPROGRESS.setAdapter(adapterPROGRESS);
                    }
                    break;
                }
            case "COMPLETE":
                if (ProfileFragment.COMPLETE != null){
                    faceLess = new ArrayList<RecyclerNewsItem>(ProfileFragment.COMPLETE);
                    l = 3;
                    if (faceLess != null) {
                        recyclerPreviewCOMPLETE.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        recyclerPreviewCOMPLETE.setHasFixedSize(true);
                        profileItemListCOMPLETE = new ArrayList<>();
                        NewsFormat newsFormat = new NewsFormat();

                        for (int i = faceLess.size() - 1; i >= 0; i--) {
                            profileItemListCOMPLETE.add(new RecyclerNewsItem(faceLess.get(i).getId(), faceLess.get(i).getOwnerId(), faceLess.get(i).getGroupPhotoUrl(), faceLess.get(i).getGroupName(), faceLess.get(i).getDate(), faceLess.get(i).getText(), faceLess.get(i).getMediaUrl(), faceLess.get(i).getVideos()));
                        }

                        adapterCOMPLETE = new ProfileNewsAdapter(profileItemListCOMPLETE, root.getContext(), 1, l);
                        recyclerPreviewCOMPLETE.setAdapter(adapterCOMPLETE);
                    }
                    break;
                }
        }
    }
}
