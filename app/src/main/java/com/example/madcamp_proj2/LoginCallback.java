package com.example.madcamp_proj2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginCallback implements FacebookCallback<LoginResult> {
    boolean success = false;
    String userID;
    String userName;
    String userEmail;
    Context context;

    // 로그인 성공 시 호출 됩니다. Access Token 발급 성공.
    @Override
    public void onSuccess(LoginResult loginResult) {

        AccessToken accessToken = loginResult.getAccessToken();

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {

            }
        };
        accessTokenTracker.startTracking();

        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {

            }
        };
        profileTracker.startTracking();

        Profile profile = Profile.getCurrentProfile();
        Profile.fetchProfileForCurrentAccessToken();
        //Log.e("Callback :: ", "onSuccess");
        //Log.d("Success", String.valueOf(loginResult.getAccessToken()));
        //Log.d("Success", loginResult.getAccessToken().getUserId());
        //Log.d("Success", String.valueOf(profile.getName()));
        //Log.d("Success", String.valueOf(profile.getProfilePictureUri(200, 200)));
        requestMe(loginResult.getAccessToken());
    }

    // 로그인 창을 닫을 경우, 호출됩니다.
    @Override
    public void onCancel() {
        Log.e("Callback :: ", "onCancel");
        success = false;
    }

    // 로그인 실패 시에 호출됩니다.
    @Override
    public void onError(FacebookException error) {
        Log.e("Callback :: ", "onError : " + error.getMessage());
        success = false;
    }

    // 사용자 정보 요청
    public void requestMe(AccessToken token) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("result",object.toString());
                        try {
                            userEmail = response.getJSONObject().getString("email");
                            userID = response.getJSONObject().getString("id");
                            userName = response.getJSONObject().getString("name");
                            String url_signup = "http://"+context.getString(R.string.ip)+":8080/login/facebook/signup";
                            NetworkTask networkTask = new NetworkTask(url_signup + "?ID=" + userID, null, "GET", null, new AsyncTaskCallback() {
                                @Override
                                public void method1(String s) {
                                    JsonParser jsonParser = new JsonParser();
                                    JsonObject collections = (JsonObject) jsonParser.parse(s);

                                    String verified = collections.get("Message").getAsString();
                                    if (verified.equals("Sign up")) {
                                        try {
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.accumulate("name", userName.toString());
                                            jsonObject.accumulate("ID", userID);
                                            jsonObject.accumulate("email", userEmail);
                                            jsonObject.accumulate("phone","");
                                            jsonObject.accumulate("photo","null");
                                            NetworkTask networkTask = new NetworkTask(url_signup, null, "POST", jsonObject, new AsyncTaskCallback() {
                                                @Override
                                                public void method2(String s) {
                                                    return;
                                                }
                                            });
                                            networkTask.execute();
                                        }catch (JSONException e){
                                            Toast.makeText(context,"error", Toast.LENGTH_SHORT);
                                        }
                                        Toast.makeText(context, "Sign up complete", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            networkTask.execute();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("userID", userID);
                            context.startActivity(intent);
                            Log.d("Result", userEmail);
                            Log.d("Result", userID);
                            Log.d("Result", userName);
                            success = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }
}
