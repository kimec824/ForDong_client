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

import static com.example.madcamp_proj2.MainActivity.context_main;

//import static com.example.helloworld.MainActivity.contactList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */



public class Fragment1 extends Fragment implements AsyncTaskCallback{

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

        String url = "http://"+getString(R.string.ip)+":8080/contacts";
        String method = "GET";
        //String json = getJsonString();
        //System.out.println(json);

        //AsyncTask를 통해 HTTPURLConnection 수행.

        NetworkTask networkTask = new NetworkTask(url, null, method ,null, this);
        networkTask.execute();

        adapter.clearItem();
        listview = (ListView) view.findViewById(R.id.listview1);

        return view ;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void method1(String s) {
        try{
            //Json parsing
            JSONObject jsonObject = new JSONObject(s);

            JSONArray contactsArray = jsonObject.getJSONArray("Contacts");


            for(int i=0; i<contactsArray.length(); i++)
            {
                JSONObject movieObject = contactsArray.getJSONObject(i);
                ContactItem contact = new ContactItem();

                contact.setUser_name(movieObject.getString("name"));
                contact.setUser_phNumber(movieObject.getString("phone"));
                contact.setMail(movieObject.getString("email"));
                contact.setID(movieObject.getString("ID"));
                contact.setPhoto(movieObject.getString("photo"));
                //System.out.println(movieObject.getString("name")+movieObject.getString("phone")+movieObject.getString("email"));

                contactItems.add(contact);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("CHECK : " + contactItems.size());
        adapter.clearItem();
        for(int i = 0; i< contactItems.size() ; i++){
            Bitmap sampleBitmap = BitmapFactory.decodeResource( context_main.getResources(), R.drawable.person);
            ContactItem ci = contactItems.get(i);
            ci.setUser_profile(sampleBitmap);
            adapter.addItem(ci);
        }
        listview.setAdapter(adapter);
    }
}