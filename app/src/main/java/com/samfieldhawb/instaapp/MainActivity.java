package com.samfieldhawb.instaapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mInstaRV;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInstaRV = findViewById(R.id.recycler_view);
        mInstaRV.setHasFixedSize(true);
        mInstaRV.setLayoutManager(new LinearLayoutManager(this));
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("InstaApp");
    }

    @Override
    protected void onStart() {
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
                holder.mImageView.setImageURI( Uri.parse(model.getImage()));
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
