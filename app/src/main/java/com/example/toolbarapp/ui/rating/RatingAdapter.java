package com.example.toolbarapp.ui.rating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.toolbarapp.R;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class RatingAdapter extends ArrayAdapter<Rating> {

    public RatingAdapter(@NonNull Context context, int resource, @NonNull List<Rating> objects) {
        super(context, resource, objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Rating rating = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        TextView tmPosition = convertView.findViewById(R.id.raring_position),
                tmModule = convertView.findViewById(R.id.rating_mod),
                tmName= convertView.findViewById(R.id.raring_name);

        assert rating != null;
        tmPosition.setText(rating.getNumber());
        tmModule.setText(rating.getCount());
        tmName.setText(rating.getLogin());

        if (!rating.getImageUri().equals("")) {
            CircleImageView imageView = convertView.findViewById(R.id.profile_image_use);
            Picasso.get().load(rating.getImageUri()).into(imageView);
        }

        return convertView;
    }
}
