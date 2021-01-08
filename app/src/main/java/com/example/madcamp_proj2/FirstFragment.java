package com.example.madcamp_proj2;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FirstFragment extends Fragment {
    TextView text;
    public static FirstFragment newInstance() {

        Bundle args = new Bundle();

        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //로그인한 유저의 동아리 정보 서버에서 가져오기
    //해당 동아리 구성원들 정보 가져오기
    //서버에서 가져온 정보 파싱해서 띄우기
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_first, container, false);
        String url = "http://192.249.18.235:8080/contacts";
        //AsyncTask를 통해 HTTPURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute();

        text=(TextView) view.findViewById(R.id.textView);
        return view;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    /* Class for Network */
    ///////////////////////////////////////////////////////////////////////////////////
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Json parsing
            JsonParser jsonParser = new JsonParser();
            JsonObject collections = (JsonObject) jsonParser.parse(s);
            JsonArray collection = (JsonArray) collections.get("myCollection");
            JsonObject document = (JsonObject) collection.get(1);

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            text.setText(document.toString());
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////


}