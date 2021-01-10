package com.example.madcamp_proj2;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Choose_whattoadd extends ListActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void DialogSelectOption() {
        final String items[] = { "공지", "투표", "item3" };
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("어떤 글을 작성하시겠습니까?");
        ab.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // 각 리스트를 선택했을때
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                if(whichButton==1)Toast.makeText(getApplicationContext(), "1번", Toast.LENGTH_SHORT).show();
                if(whichButton==2)Toast.makeText(getApplicationContext(), "2번", Toast.LENGTH_SHORT).show();
                if(whichButton==3)Toast.makeText(getApplicationContext(), "3번", Toast.LENGTH_SHORT).show();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancel 버튼 클릭시
            }
        });
        ab.show();
    }
}
