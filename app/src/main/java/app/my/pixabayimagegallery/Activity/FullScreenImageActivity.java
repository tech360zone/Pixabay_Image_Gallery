package app.my.pixabayimagegallery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import java.util.ArrayList;
import java.util.List;

import app.my.pixabayimagegallery.Adapter.ImageAdapter;
import app.my.pixabayimagegallery.ApiClient.RetrofitClient;
import app.my.pixabayimagegallery.Model.Hits;
import app.my.pixabayimagegallery.Model.Response;
import app.my.pixabayimagegallery.databinding.ActivityFullScreenImageBinding;
import retrofit2.Call;
import retrofit2.Callback;

public class FullScreenImageActivity extends AppCompatActivity {

    private ActivityFullScreenImageBinding binding;
    private List<Hits> imageList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullScreenImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            position = intent.getIntExtra("position", 0);
        }

        binding.rvImageFullScreen.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvImageFullScreen.setLayoutManager(linearLayoutManager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.rvImageFullScreen);

        getThumbnail();
    }

    private void getThumbnail() {
        Call<Response> responseCall = RetrofitClient.getInstance().getMyApi().getImages(1, 50);
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {
                    Response myResponse = response.body();
                    if (myResponse != null) {
                        imageList = myResponse.getHits();
                        if (imageList != null && imageList.size() > 0) {
                            ImageAdapter adapter = new ImageAdapter(FullScreenImageActivity.this, imageList, null, 1);
                            binding.rvImageFullScreen.setAdapter(adapter);
                            binding.rvImageFullScreen.scrollToPosition(position);
                        }
                        binding.pbImageFullScreen.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(FullScreenImageActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                binding.pbImageFullScreen.setVisibility(View.GONE);
            }
        });
    }
}