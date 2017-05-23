package com.example.hemantj.mediaplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AllSongs extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<SongInfo> songsList = new ArrayList<>();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        checkUserPermissions();
    }

    private void loadSongs(){
        SongsAdapter adapter = new SongsAdapter(this,getAllSongs());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public ArrayList<SongInfo> getAllSongs(){
        Uri allSongs = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = getContentResolver().query(allSongs,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
               do{
                   String songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                   String songPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                   String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                   String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                   songsList.add(new SongInfo(songPath,songName,albumName,artistName));

               }while (cursor.moveToNext());
            }
            cursor.close();
        }

        return songsList;
    }

    private class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

        private ArrayList<SongInfo> songsList;
        private Context mContext;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView songName;
            private TextView artistName;

            public ViewHolder(View itemView) {
                super(itemView);

                songName = (TextView) itemView.findViewById(R.id.song_name);
                artistName = (TextView) itemView.findViewById(R.id.artist_name);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();

                Intent intent = new Intent(AllSongs.this,MainActivity.class);
                intent.putParcelableArrayListExtra("songsList",songsList);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        }

        public SongsAdapter(Context context,ArrayList<SongInfo> songsList){
            mContext = context;
            SongsAdapter.this.songsList = songsList;
        }

        private Context getmContext(){
            return mContext;
        }

        @Override
        public SongsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = getLayoutInflater().inflate(R.layout.list_item,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(SongsAdapter.ViewHolder holder, int position) {
            SongInfo songsInfo = SongsAdapter.this.songsList.get(position);

            TextView songName = holder.songName;
            songName.setText(songsInfo.getSongName());

            TextView artistName = holder.artistName;
            artistName.setText(songsInfo.getArtistName());
        }

        @Override
        public int getItemCount() {
            return SongsAdapter.this.songsList.size();
        }
    }

    private void checkUserPermissions(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }

        //if SDK is lesser than 23 then execute some function
        loadSongs();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // if permission is granted then execute the same function as above
                    loadSongs();
                } else {
                    // Permission Denied
                    Toast.makeText( this,"Media Permissions necessary" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
