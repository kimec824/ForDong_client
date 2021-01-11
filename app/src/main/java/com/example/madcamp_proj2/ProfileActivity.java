package com.example.madcamp_proj2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static com.example.madcamp_proj2.Fragment1.adapter;
import static com.example.madcamp_proj2.Fragment1.listview;
import static com.example.madcamp_proj2.MainActivity.context_main;
import static com.example.madcamp_proj2.MainActivity.userID;

public class ProfileActivity extends AppCompatActivity implements AsyncTaskCallback{

    TextView userID;
    TextView userName;
    TextView userPhone;
    TextView userEmail;
    ImageView userImage;
    ApiService apiService;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;

    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userID = findViewById(R.id.idProfile);
        userName = findViewById(R.id.nameProfile);
        userPhone = findViewById(R.id.phoneProfile);
        userEmail = findViewById(R.id.emailProfile);
        userImage = findViewById(R.id.imageProfile);
        Button editbtn = (Button) findViewById(R.id.editbtn);


        Intent intent = getIntent();
        userID.setText(intent.getStringExtra("userID"));

        String url = "http://"+getString(R.string.ip)+":8080/contacts/"+userID.getText().toString();

        NetworkTask networkTask = new NetworkTask(url, null, "GET",null, this);
        networkTask.execute();

        if(userID.getText().toString().equals(MainActivity.userID)) {
            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askPermissions();
                    initRetrofitClient();
                    startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                }
            });

            //edit button click
            editbtn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(MainActivity.userID);

                    final LinearLayout linear = (LinearLayout) View.inflate(ProfileActivity.this, R.layout.contactdialog, null);
                    AlertDialog.Builder adb = new AlertDialog.Builder(ProfileActivity.this, R.style.MyDialogTheme);

                    EditText edt = linear.findViewById(R.id.et1);
                    adb.setView(linear);
                    edt.setText(userName.getText().toString());

                    EditText edt2 = linear.findViewById(R.id.et2);
                    adb.setView(linear);
                    edt2.setText(userPhone.getText().toString());

                    EditText edt3 = linear.findViewById(R.id.et3);
                    adb.setView(linear);
                    edt3.setText(userEmail.getText().toString());

                    //ok는 수정했다는것.
                    adb.setTitle("Edit Contact")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String name = edt.getText().toString();
                                    String number = edt2.getText().toString();
                                    String mail = edt3.getText().toString();
                                    //
                                    //DATABASE에 넘겨주는 작업이 필요함.(PUT)
                                    //
                                    String url = "http://" + getString(R.string.ip) + ":8080/contacts";
                                    String method = "PUT";

                                    //build jsonObject
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.accumulate("ID", userID.getText().toString());
                                        jsonObject.accumulate("name", userName.getText().toString());
                                        jsonObject.accumulate("phone", userPhone.getText().toString());
                                        jsonObject.accumulate("email", userEmail.getText().toString());
                                        jsonObject.accumulate("changename", name);
                                        jsonObject.accumulate("changephone", number);
                                        jsonObject.accumulate("changeemail", mail);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    System.out.println("checkpoint");
                                    NetworkTask networkTask = new NetworkTask(url, null, method, jsonObject, ProfileActivity.this);
                                    networkTask.execute();
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                    AlertDialog finalDialog = adb.create();
                    finalDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            finalDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#6E6557"));
                            finalDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#6E6557"));
                        }
                    });
                    finalDialog.show();
                }
            });

        }
        else{
            editbtn.setVisibility(View.GONE);
        }
    }

    /**
     *  Permission의 이용.
     */
    private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    /**
     *  image 전송관련 함수의 시작
     */
    private void initRetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        apiService = new Retrofit.Builder().baseUrl("http://"+getString(R.string.ip)+":8080/").client(client).build().create(ApiService.class);
    }

    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            ImageView imageView = findViewById(R.id.imageProfile);

            if (requestCode == IMAGE_RESULT) {
                String filePath = getImageFilePath(data);
                File file = new File(filePath);
                if (filePath != null) {
                    //Log.v("SDK Version:","sdk_int " +Build.VERSION.SDK_INT);

                    if (Build.VERSION.SDK_INT >= 29){
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),Uri.fromFile(file));
                        try {
                            mBitmap = ImageDecoder.decodeBitmap(source);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{

                        mBitmap = BitmapFactory.decodeFile(filePath);
                        //mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));

                    }

                    imageView.setImageBitmap(mBitmap);

                    if (mBitmap != null)
                        multipartImageUpload();
                }
            }
        }
    }

    private void multipartImageUpload() {
        try {
            Log.d("multipart","ImageUpload");
            File filesDir = getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), userID.getText().toString());

            Call<ResponseBody> req = apiService.postImage2(body, name);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(),"Uploaded Successfully!", Toast.LENGTH_LONG);
                    }

                    Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void method1(String s) {
        try{
            //Json parsing
            JSONObject jsonObject = new JSONObject(s);

            JSONArray contactsArray = jsonObject.getJSONArray("Contact");
            JSONObject contact = contactsArray.getJSONObject(0);

            userName.setText(contact.getString("name"));
            userPhone.setText(contact.getString("phone"));
            userEmail.setText(contact.getString("email"));

            if(contact.getString("photo").equals("null"))
                userImage.setImageBitmap(BitmapFactory.decodeResource( context_main.getResources(), R.drawable.person));
            else {
                String path_url = "http://"+getString(R.string.ip)+":8080/photos/uploads/" + contact.getString("photo");
                Glide.with(this).load(path_url).into(userImage);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void method3(String s) {
        System.out.println(s);
    }
}