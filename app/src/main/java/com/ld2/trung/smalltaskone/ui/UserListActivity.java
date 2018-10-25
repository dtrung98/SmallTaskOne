/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ld2.trung.smalltaskone.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ld2.trung.smalltaskone.R;
import com.ld2.trung.smalltaskone.adapters.UserListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends AppCompatActivity implements UserListAdapter.ItemClickListener{
    private static final String TAG="UserListActivity";

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    UserListAdapter adapter;
    
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);
        data = readFileFromAsset("data.json");
        arrayData = convertJSonToArray(data);

        adapter = new UserListAdapter(this,arrayData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        adapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position, Bitmap avatar, String username, int id) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();

        Intent anotherIntent = new Intent(this, UserProfileActivity.class);
        anotherIntent.putExtra("avatar", byteArray);
        anotherIntent.putExtra("username",username);
        anotherIntent.putExtra("id",id);
        startActivity(anotherIntent);

    }

    public class UserData {
        int id;
        String username;
        String avatarPath;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatarPath() {
            return avatarPath;
        }

        public void setAvatarPath(String avatarPath) {
            this.avatarPath = avatarPath;
        }

        public UserData(int id, String username, String avatarPath) {;
            this.id = id;
            this.username = username;
            this.avatarPath = avatarPath;
        };
    }
    private ArrayList<UserData> arrayData;
    private ArrayList<UserData> convertJSonToArray(String jsonString) {
        ArrayList<UserData> arrayList= new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i=0;i<jsonArray.length();i++) {
              JSONObject jo = jsonArray.getJSONObject(i);
              arrayList.add(new UserData(jo.getInt("userId"),jo.getString("displayName"),jo.getString("avatar")));

            }
         } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * Read String File From Assets
     * @param fileName the name of the file
     * @return the text content of the jsong
     */
    private String readFileFromAsset(String fileName){


        BufferedReader reader = null;
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try{
            inputStream = getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while((line = reader.readLine()) != null)
            {
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        } finally {

            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }

            if(reader != null)
            {
                try {
                    reader.close();
                } catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
}
