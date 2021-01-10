package com.example.madcamp_proj2;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.madcamp_proj2.ContactItem;
import com.example.madcamp_proj2.ListViewAdapter;
import com.example.madcamp_proj2.MainActivity;
import com.example.madcamp_proj2.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//import static com.example.helloworld.MainActivity.contactList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */



public class Fragment1 extends Fragment {

    TextView text;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayList<ContactItem> contactItems = new ArrayList<ContactItem>();

    //static ArrayList<ContentProviderOperation> newcontact = new ArrayList<>();
    public static ListView listview;
    public static ListViewAdapter adapter = new ListViewAdapter();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment1() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, null) ;
        contactItems.clear();
        //연락처 가져오기.-> db로 연결
        //getContactList();

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;
        //ListView listview = (ListView) view.findViewById(R.id.listview1);
        //listview.setAdapter(adapter) ;



        String url = "http://192.249.18.250:8080/contacts";

        //String json = getJsonString();
        //System.out.println(json);
        //AsyncTask를 통해 HTTPURLConnection 수행.
/*
        String method = "POST";
        NetworkTask networkPostTask = new NetworkTask(url, null, method, 1);
        networkPostTask.execute();

*/
        String method = "GET";
        NetworkTask networkTask = new NetworkTask(url, null, method, 1);
        networkTask.execute();

        adapter.clearItem();
        listview = (ListView) view.findViewById(R.id.listview1);
        listview.setAdapter(adapter);


        return view ;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        text = view.findViewById(R.id.textview_first);
        */



    }
/**
    private String getJsonString()
    {
        String json = "";

        try {
            InputStream is = getContext().getAssets().open("contacts.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return json;
    }
    //이걸 db로 받으면 됨.
    public void getContactList(){
        //contacts item에 해당 파일들이 json parsing 해서들어오면되는거.
    }
 */
}