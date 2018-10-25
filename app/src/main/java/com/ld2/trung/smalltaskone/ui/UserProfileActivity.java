package com.ld2.trung.smalltaskone.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ld2.trung.smalltaskone.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileActivity extends AppCompatActivity {
    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.title) TextView username;
    @BindView(R.id.description) TextView description;
    Bitmap bmp;
    String str_username;
    int int_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        // FullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        ButterKnife.bind(this);

        byte[] byteArray = getIntent().getByteArrayExtra("avatar");
        str_username = getIntent().getStringExtra("username");
        int_id = getIntent().getIntExtra("id",0);
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        avatar.setImageBitmap(bmp);

        username.setText(str_username);
        description.setText("ID "+String.valueOf(int_id));
    }
}
