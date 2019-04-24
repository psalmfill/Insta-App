package com.samfieldhawb.instaapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PostActivity extends AppCompatActivity {
    public static final int IMAGE_REQUEST_CODE = 1;
    Uri mUri = null;
    ImageButton mImageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImageButton = findViewById(R.id.image_button);
    }

    public void imageButtonClicked(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            mUri = data.getData();
            mImageButton.setImageURI(mUri);

        }
    }
}
