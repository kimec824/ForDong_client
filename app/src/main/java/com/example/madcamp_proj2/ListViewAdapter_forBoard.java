package com.example.madcamp_proj2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import static com.example.madcamp_proj2.Fragment1.contactItems;

public class ListViewAdapter_forBoard extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<ListViewItem_Board> listViewItemList = new ArrayList<ListViewItem_Board>();

    // ListViewAdapter의 생성자
    public ListViewAdapter_forBoard() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.boardlistview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1_board) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.title_board) ;
        TextView writerTextView = (TextView) convertView.findViewById(R.id.writername);
        TextView posttype=(TextView) convertView.findViewById(R.id.post_type);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득

        ListViewItem_Board listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        //여기서 glide 로 사진 가져와야함.
        // 그 해당 유저의 photo 의 값을 와야함.
        // contactItem writer==id

        for( int i = 0 ; i< contactItems.size() ; i++){
            if(listViewItem.getWriter().equals(contactItems.get(i).getID())){
                String path_url = "http://"+ iconImageView.getContext().getString(R.string.ip)+":8080/photos/uploads/" + contactItems.get(i).getPhoto();
                Glide.with(iconImageView.getContext()).load(path_url).into(iconImageView);
            }
        }
        //iconImageView.setImageBitmap(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getTitle());
        writerTextView.setText(listViewItem.getWriter());
        posttype.setText(listViewItem.getType());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Bitmap icon, String title, String writer, int type) {
        ListViewItem_Board item = new ListViewItem_Board();

        item.setIcon(icon);
        item.setTitle(title);
        //item.setDesc(desc);
        item.setWriter(writer);
        if(type==1)//공지
            item.setType("공지");
        else if(type==2)//투표
            item.setType("투표");
        listViewItemList.add(item);
    }

    public void clearItem() {
        listViewItemList = new ArrayList<ListViewItem_Board>();
    }

}
