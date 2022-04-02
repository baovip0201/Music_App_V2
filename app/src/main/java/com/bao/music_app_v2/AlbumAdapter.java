package com.bao.music_app_v2;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder1>{
    private Context context;
    private ArrayList<MusicFiles> musicFiles;

    public AlbumAdapter(Context context, ArrayList<MusicFiles> musicFiles) {
        this.context = context;
        this.musicFiles = musicFiles;
    }

    @NonNull
    @Override
    public AlbumAdapter.MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.album_items,parent,false);
        return new MyViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.MyViewHolder1 holder, int position) {
        holder.textAlbum.setText(musicFiles.get(position).getAlbum());
        byte[] img=getAlbumArt(musicFiles.get(position).getPath());
        if(img!=null){
            Glide.with(context).asBitmap().load(img).into(holder.imgAlbum);
        }else{
            Glide.with(context).load(R.drawable.compact_disc).into(holder.imgAlbum);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PlayerActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }
    public static class MyViewHolder1 extends RecyclerView.ViewHolder{
        ImageView imgAlbum;
        TextView textAlbum;

        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            imgAlbum=itemView.findViewById(R.id.album_img);
            textAlbum=itemView.findViewById(R.id.text_album);
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
