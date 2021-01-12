package com.example.madcamp_proj2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.madcamp_proj2.Fragment1.contactItems;
import static com.example.madcamp_proj2.Fragment2.recyclerView;
import static com.example.madcamp_proj2.MainActivity.context_main;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    public ArrayList<FeedItem> feedItems = new ArrayList<FeedItem>();


    public RecyclerViewAdapter(){}

    FeedItem feed;


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false) ;
        RecyclerViewHolder vh = new RecyclerViewAdapter.RecyclerViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        feed= feedItems.get(position);

        holder.onBind(feed);
    }


    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return feedItems.size() ;
    }

    public void clearItem() {
        feedItems = new ArrayList<FeedItem>();
    }
    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String userid, String context, String image_path, String photo_group) {
        FeedItem item = new FeedItem();

        item.setId(userid);
        item.setPhotoContext(context);
        item.setImagePath(image_path);
        item.setPhoto_group(photo_group);
        //item.setStr(str);


        feedItems.add(item);
    }


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView tv_writer_name;
        TextView tv_name;
        TextView tv_path;
        TextView tv_context;
        public ImageView writerImage = null;
        public ImageView iconImageView = null;


        public RecyclerViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            writerImage = (ImageView) itemView.findViewById(R.id.writer_image);
            tv_writer_name =(TextView) itemView.findViewById(R.id.writer_name);
            iconImageView = (ImageView) itemView.findViewById(R.id.imageview);
            tv_name = (TextView) itemView.findViewById(R.id.writer_name2);
            tv_context = (TextView) itemView.findViewById(R.id.context);

            writerImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    FeedItem fd = feedItems.get(pos);
                    Intent tt = new Intent(context_main ,ProfileActivity.class);
                    tt.putExtra("userID", fd.getId());
                    context_main.startActivity(tt);
                }
            });

            tv_writer_name.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    FeedItem fd = feedItems.get(pos);
                    Intent tt = new Intent(context_main ,ProfileActivity.class);
                    tt.putExtra("userID", fd.getId());
                    context_main.startActivity(tt);
                }
            });

        }

        public void onBind(FeedItem feed) {
            // 아이템 내 각 위젯에 데이터 반영

            String url = "http://"+context_main.getString(R.string.ip)+":8080/photos/uploads/";
            //iconImageView.setImageBitmap(gridViewItem.getIcon());

            for(int i= 0 ; i<contactItems.size() ; i++){
                Log.d(feed.getId(), contactItems.get(i).getID());
                if(feed.getId().equals(contactItems.get(i).getID()) ){
                    String profile_url = url+contactItems.get(i).getPhoto();
                    Glide.with(tv_context.getContext()).load(profile_url).into(this.writerImage);
                    break;
                }
            }
            Glide.with(tv_context.getContext()).load(url+feed.getImagePath()).into(this.iconImageView);
            tv_writer_name.setText(feed.getId());
            tv_name.setText(feed.getId());

            tv_context.setText(feed.getPhotoConText());
        }
    }


}