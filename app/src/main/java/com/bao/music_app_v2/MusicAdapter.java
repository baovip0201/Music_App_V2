package com.bao.music_app_v2;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MusicFiles> musicFiles;

    MusicAdapter(Context context, ArrayList<MusicFiles> musicFiles){
        this.context=context;
        this.musicFiles=musicFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.music_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.file_name.setText(musicFiles.get(position).getTitle());
            byte[] img=getAlbumArt(musicFiles.get(position).getPath());
            if(img!=null){
                Glide.with(context).asBitmap().load(img).into(holder.album_art);
            }else{
                Glide.with(context).load(R.drawable.compact_disc).into(holder.album_art);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, PlayerActivity.class);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
            holder.menu_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu=new PopupMenu(context,view);
                    popupMenu.getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener((menuItem -> {
                        switch (menuItem.getItemId()){
                            case R.id.delete:
                                Toast.makeText(context, "Delete click", Toast.LENGTH_SHORT).show();
                                deleteFile(position,view);
                                break;
                        }
                        return true;
                    }));
                }
            });
    }

    private void deleteFile(int position, View view) {
        Uri contentUri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(musicFiles.get(position).getId()));

        File file=new File(musicFiles.get(position).getPath());
        //xoa file
        boolean deleted=file.delete();
        if (deleted){
            context.getContentResolver().delete(contentUri, null, null);
            musicFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,musicFiles.size());
            Snackbar.make(view,"File deleted", Snackbar.LENGTH_SHORT)
                    .show();
        }
        //SD card
        else{
            Snackbar.make(view,"Can't be deleted", Snackbar.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView file_name;
        ImageView album_art, menu_more;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name=itemView.findViewById(R.id.music_file_name);
            album_art=itemView.findViewById(R.id.music_img);
            menu_more=itemView.findViewById(R.id.menuMore);
        }
    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art=retriever.getEmbeddedPicture();
         retriever.release();
         return art;
    }
}
