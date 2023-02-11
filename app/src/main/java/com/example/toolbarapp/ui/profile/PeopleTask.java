package com.example.toolbarapp.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.toolbarapp.MainActivity;
import com.example.toolbarapp.R;
import com.example.toolbarapp.ui.home.RecyclerNewsItem;
import com.example.toolbarapp.ui.home.news.NewsStencil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import static com.example.toolbarapp.ui.profile.ProfileFragment.currentUserID;

public class PeopleTask extends AppCompatActivity {

    ImageView imageView;
    ImageView loading;
    Button button;
    EditText editText;
    public  static RecyclerNewsItem recyclerNewsItem;
    private static int GalleryPick = 1;
    public static int poing = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_task);

        button = (Button) findViewById(R.id.buttont);
        loading = (ImageView) findViewById(R.id.imageView6);
        imageView = (ImageView) findViewById(R.id.doc);
        editText = (EditText) findViewById(R.id.editText2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText == null || editText.getText().toString().length() <= 1) {
                    Toast.makeText(PeopleTask.this, "Нельзя прикрепить фото без текста", Toast.LENGTH_LONG).show();
                }
                else {
                    GalleryPick = 1;
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, GalleryPick);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerNewsItem = new RecyclerNewsItem();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null && poing != 1)
        {
            final Uri ImageUri = data.getData();
            Picasso.get ().load(ImageUri).into(loading);
            FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final String login = (String) dataSnapshot.child("login").getValue();
                    final String image = (String) dataSnapshot.child("image").getValue();

                    FirebaseDatabase.getInstance().getReference("Task").child(currentUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final int n = (int) dataSnapshot.getChildrenCount();
                            FirebaseDatabase.getInstance().getReference("Task").child(currentUserID).child(String.valueOf(n)).child("login").setValue(login);
                            FirebaseDatabase.getInstance().getReference("Task").child(currentUserID).child(String.valueOf(n)).child("image").setValue(image);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

            FirebaseDatabase.getInstance().getReference("Task").child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final int n = (int) dataSnapshot.getChildrenCount();

                    if (editText != null){
                       FirebaseDatabase.getInstance().getReference("Task").child(currentUserID).child(String.valueOf(n)).child("text").setValue(editText.getText().toString());
                    }
                    NewsStencil newsStencil = new NewsStencil(
                            recyclerNewsItem.getId(),
                            recyclerNewsItem.getOwnerId(),
                            recyclerNewsItem.getGroupPhotoUrl(),
                            recyclerNewsItem.getGroupName(),
                            String.valueOf(recyclerNewsItem.getDate()),
                            recyclerNewsItem.getText(),
                            recyclerNewsItem.getMediaUrl(),
                            recyclerNewsItem.getVideos(),
                            recyclerNewsItem.getLink());

                    Gson gson = new Gson();
                    String json = gson.toJson(newsStencil);
                    FirebaseDatabase.getInstance().getReference("Task").child(currentUserID).child(String.valueOf(n-1)).child(recyclerNewsItem.getOwnerId() + " " + recyclerNewsItem.getId()).setValue(json);

                    final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Task Doc").child(currentUserID + "_" + n + ".jpg");

                    assert ImageUri != null;
                    filePath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();
                                    FirebaseDatabase.getInstance().getReference("Task").child(currentUserID).child(String.valueOf(n)).child("doc").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            GalleryPick = 0;
            poing = 1;
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }
    }
}
