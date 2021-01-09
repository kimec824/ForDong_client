package com.example.madcamp_proj2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static com.example.madcamp_proj2.Fragment1.adapter;
import static com.example.madcamp_proj2.Fragment1.contactItems;
import static com.example.madcamp_proj2.Fragment1.listview;
import static com.example.madcamp_proj2.MainActivity.context_main;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment implements AsyncTaskCallback{


    public Fragment2() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        //GridView adapter
        GridViewAdapter adapter = new GridViewAdapter();
        GridView gridview = (GridView) view.findViewById(R.id.gridView);
        gridview.setAdapter(adapter);
        adapter = add_item_to_gridviewadapter(adapter);

        Button button1 = (Button) view.findViewById(R.id.button);

        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),ImageActivity.class);
                startActivity(intent);
                // TODO : click event
            }
        });


        return view;
    }

    public GridViewAdapter add_item_to_gridviewadapter(GridViewAdapter myadapter) {

        //여기서 item 을 넣어줘야함
        //그말은 여기서 get api 를 이용해서 server의 이미지들을 가져와야한다는 뜻.
        //1.requestHTTpconnection을 이용해서 서버에 get /photos를 할 순 있음.
        //그럼 이거 부터해서 photo들의 path를 받아보자.


        String url = "http://192.249.18.250:8080/photos";
        //String json = getJsonString();
        //System.out.println(json);
        //AsyncTask를 통해 HTTPURLConnection 수행.

        NetworkTask networkTask = new NetworkTask(url, null, null, this);
        networkTask.execute();


        //jsonParsing(json); // arraylist<childfragmentitem> 에 들어가게 됨.
/**
        for (int i = 0; i < dep_icon.length; i++) {
            GridViewItem gk = kaist.get(i);
            myadapter.addItem(gk.getIcon(), gk.getStr()); //건설환경공학과
        }
*/
        //adapter.clearItem();
        //listview = (ListView) view.findViewById(R.id.listview1);
        return myadapter;
    }

    @Override
    public void method1(String s) {
        try{
            //Json parsing
            JSONObject jsonObject = new JSONObject(s);

            JSONArray contactsArray = jsonObject.getJSONArray("Photos");


            for(int i=0; i<contactsArray.length(); i++)
            {
                JSONObject movieObject = contactsArray.getJSONObject(i);
                ContactItem contact = new ContactItem();

                contact.setUser_name(movieObject.getString("name"));
                contact.setUser_phNumber(movieObject.getString("file_path"));
                contact.setMail(movieObject.getString("email"));
                //System.out.println(movieObject.getString("name")+movieObject.getString("phoneNumber")+movieObject.getString("email"));

                contactItems.add(contact);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("CHECK : " + contactItems.size());
        for(int i = 0; i< contactItems.size() ; i++){
            Bitmap sampleBitmap = BitmapFactory.decodeResource( context_main.getResources(), R.drawable.person);
            ContactItem ci = contactItems.get(i);
            adapter.addItem(sampleBitmap, ci.getUser_name(), ci.getUser_phNumber(),
                    ci.getMail(), "sample address");
        }
        listview.setAdapter(adapter);
    }
}