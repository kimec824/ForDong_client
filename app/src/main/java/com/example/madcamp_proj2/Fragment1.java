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
    ArrayList<ContactItem> contactItems = new ArrayList<ContactItem>();

    //static ArrayList<ContentProviderOperation> newcontact = new ArrayList<>();
    static ListView listview;
    public ListViewAdapter adapter = new ListViewAdapter();

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


        String url = "http://192.249.18.232:8080/api/contacts";
        //String json = getJsonString();
        //System.out.println(json);
        //AsyncTask를 통해 HTTPURLConnection 수행.

        String method = "POST";
        NetworkTask networkPostTask = new NetworkTask(url, null, method);
        networkPostTask.execute();


         method = "GET";
        NetworkTask networkTask = new NetworkTask(url, null, method);
        networkTask.execute();


        adapter.clearItem();
        listview = (ListView) view.findViewById(R.id.listview1);



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

    ///////////////////////////////////////////////////////////////////////////////////
    /* Class for Network */
    ///////////////////////////////////////////////////////////////////////////////////
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;
        private String method;

        public NetworkTask(String url, ContentValues values, String method) {

            this.url = url;
            this.values = values;
            this.method = method;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            if(method == "GET"){
                result = requestHttpURLConnection.request_get(url, values); // 해당 URL로 부터 결과물을 얻어온다.
            }
            else{
                result = requestHttpURLConnection.request_post(url, values); // 해당 URL로 POST 보내기.
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //System.out.println(s);
            if (method == "GET"){
                try{
                    //Json parsing
                    JSONObject jsonObject = new JSONObject(s);

                    JSONArray contactsArray = jsonObject.getJSONArray("Contacts");


                    for(int i=0; i<contactsArray.length(); i++)
                    {
                        JSONObject movieObject = contactsArray.getJSONObject(i);
                        ContactItem contact = new ContactItem();

                        contact.setUser_name(movieObject.getString("name"));
                        contact.setUser_phNumber(movieObject.getString("phoneNumber"));
                        contact.setMail(movieObject.getString("email"));
                        //System.out.println(movieObject.getString("name")+movieObject.getString("phoneNumber")+movieObject.getString("email"));

                        contactItems.add(contact);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("CHECK : " + contactItems.size());
                for(int i = 0; i< contactItems.size() ; i++){
                    Bitmap sampleBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.person);
                    ContactItem ci = contactItems.get(i);
                    adapter.addItem(sampleBitmap, ci.getUser_name(), ci.getUser_phNumber(),
                            ci.getMail(), "sample address");
                }
                listview.setAdapter(adapter);
            }
            else if(method == "POST"){
                if(s == "fail"){
                    //Log.e("fail","fail....");
                }
                else{
                    //Log.e("success",s);
                }
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////
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