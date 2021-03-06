package com.samfieldhawb.instaapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {
    public static final int IMAGE_REQUEST_CODE = 1;
    private StorageReference mStorageReference;
    Uri mUri = null;
    ImageButton mImageButton;
    private EditText mNameField,mDescriptionField;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImageButton = findViewById(R.id.image_button);

        mNameField = findViewById(R.id.edit_name);
        mDescriptionField = findViewById(R.id.edit_description);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference().child("InstaApp");
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

    public void submitButtonClicked(View view){
        final String name = mNameField.getText().toString().trim();
        final String desc = mDescriptionField.getText().toString().trim();

        if(name.isEmpty()){
            mNameField.setError("Name cannot e empty");
            return;
        }
        if (desc.isEmpty()){
            mDescriptionField.setError("Description cannot be empty");
            return;
        }
        final StorageReference filePath = mStorageReference.child("PostImages").child(mUri.getLastPathSegment());
        filePath.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference newPost = mReference.push();
                        newPost.child("title").setValue(name);
                        newPost.child("desc").setValue(desc);
                        newPost.child("image").setValue(uri.toString());
                        Toast.makeText(PostActivity.this,"Upload Completed",Toast.LENGTH_LONG).show();

                    }
                });
                   }
        });
    }
}
