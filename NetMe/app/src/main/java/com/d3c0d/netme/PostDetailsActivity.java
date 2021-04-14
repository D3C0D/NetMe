package com.d3c0d.netme;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.api.Context;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class PostDetailsActivity extends AppCompatActivity {

    private TextView postDetailTitle, postDetailDesc;
    private ImageView postDetailImage;
    private Toolbar postDetailToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        postDetailToolbar = findViewById(R.id.post_detail_toolbar);
        setSupportActionBar(postDetailToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postDetailImage = findViewById(R.id.post_detail_image);
        postDetailTitle = findViewById(R.id.post_detail_title);
        postDetailDesc = findViewById(R.id.post_detail_desc);

        // Get Extras
        Intent postDetailIntent = getIntent();
        String title = postDetailIntent.getExtras().getString("Title");
        String desc = postDetailIntent.getExtras().getString("Desc");
        String imageUri = postDetailIntent.getExtras().getString("Image");

        getSupportActionBar().setTitle(title);

        // Set Extras
        postDetailTitle.setText(title);
        postDetailDesc.setText(desc);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image_placeholder);

        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(imageUri).into(postDetailImage);
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(PostDetailsActivity.this.getContentResolver(), inImage, UUID.randomUUID().toString() + ".png", postDetailTitle.toString());
        return Uri.parse(path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share_post:
                BitmapDrawable drawable = (BitmapDrawable) postDetailImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(bitmap));
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                break;
            default:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
