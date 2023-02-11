package com.example.toolbarapp.ui.workspace;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.example.toolbarapp.MainActivity;
import com.example.toolbarapp.R;
import com.example.toolbarapp.map.MapsActivity;

public class WorkspaceFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_workspace, container, false);

        ImageView imageView = (ImageView) root.findViewById(R.id.buttonui);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Не забудьте включить геолокацию!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });



        return root;

    }
}
