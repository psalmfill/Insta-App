package com.samfieldhawb.instaapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mInstaRV;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInstaRV = findViewById(R.id.recycler_view);
        mInstaRV.setHasFixedSize(true);
        mInstaRV.setLayoutManager(new LinearLayoutManager(this));
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("InstaApp");
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(getApplicationContext(),RegisterActivity.class);
                    //prvent user from returning here if backbutton is press
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        //attach listener
        mAuth.addAuthStateListener(mAuthStateListener);
        super.onStart();
        FirebaseRecyclerAdapter<Post,InstaViewHolder> FBRA = new FirebaseRecyclerAdapter<Post, InstaViewHolder>(
                new FirebaseRecyclerOptions.Builder<Post>().setQuery(mDatabaseReference, new SnapshotParser<Post>() {
                    @NonNull
                    @Override
                    public Post parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Post(snapshot.child("title").getValue().toString(),
                                snapshot.child("desc").getValue().toString(),
                                snapshot.child("image").getValue().toString());
                    }
                }).build()
        ) {
            @Override
            protected void onBindViewHolder(@NonNull InstaViewHolder holder, int position, @NonNull Post model) {
                Picasso.with(getApplicationContext()).load(model.getImage()).into(holder.mImageView);
                holder.mTitle.setText(model.getTitle());
                holder.mDesc.setText(model.getDesc());
            }

            @NonNull
            @Override
            public InstaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new InstaViewHolder(
                        LayoutInflater.from(MainActivity.this).inflate(R.layout.insta_row,viewGroup,false)
                );
            }
        };
        mInstaRV.setAdapter(FBRA);FBRA.startListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add){
            Intent intent = new Intent(this,PostActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_logout){
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    class InstaViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTitle,mDesc;
        public InstaViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.post_image);
            mTitle = itemView.findViewById(R.id.post_title);
            mDesc = itemView.findViewById(R.id.post_desc);
        }
    }
}
