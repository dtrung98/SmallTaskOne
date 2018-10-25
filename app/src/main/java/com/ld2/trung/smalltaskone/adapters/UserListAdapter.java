package com.ld2.trung.smalltaskone.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ld2.trung.smalltaskone.R;
import com.ld2.trung.smalltaskone.ui.UserListActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private Context mContext;
    private List<UserListActivity.UserData> mData;

    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public UserListAdapter(Context context, List<UserListActivity.UserData> data) {
        mContext = context;

        this.mData = data;
        if(data==null) mData = new ArrayList<>();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final UserListActivity.UserData user = mData.get(position);
        holder.bind(user);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    public static Bitmap getBitmapFromURL(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         @BindView(R.id.user_name_text_view) TextView userName;
         @BindView(R.id.id_text_view) TextView id;
         @BindView(R.id.user_image_view) ImageView iconAvatar;
         @BindView(R.id.over_view) View overView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            overView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                int pos = getAdapterPosition();
                mClickListener.onItemClick(view,
                        pos,
                        ((BitmapDrawable) iconAvatar.getDrawable()).getBitmap(),
                        mData.get(pos).getUsername(),
                        mData.get(pos).getId());
            }
        }
        void bind(UserListActivity.UserData userData) {
            userName.setText(userData.getUsername());
            id.setText("ID "+String.valueOf(userData.getId()));
            // new LoadBitmap().execute(holder,user.getAvatarPath());
            Glide.with(mContext)
                    .load(userData.getAvatarPath())
                    .into(iconAvatar);

        }
    }

    // convenience method for getting data at click position
    UserListActivity.UserData getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position,Bitmap avatar, String username, int id);
    }
    private static class LoadBitmap extends AsyncTask<Object,Void,Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bitmap1) {
            holder.iconAvatar.setImageBitmap(bitmap1);
        }

       ViewHolder holder;

        @Override
        protected Bitmap doInBackground(Object... url) {
            holder = (ViewHolder) url[0];
            return getBitmapFromURL(((String)url[1]));
        }
    }
}