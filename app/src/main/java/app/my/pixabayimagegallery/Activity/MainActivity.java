package app.my.pixabayimagegallery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.my.pixabayimagegallery.Adapter.ImageAdapter;
import app.my.pixabayimagegallery.ApiClient.RetrofitClient;
import app.my.pixabayimagegallery.Interface.AdapterClick;
import app.my.pixabayimagegallery.Model.Hits;
import app.my.pixabayimagegallery.Model.Response;
import app.my.pixabayimagegallery.R;
import app.my.pixabayimagegallery.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements AdapterClick {

    private ActivityMainBinding binding;
    private List<Hits> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageList = new ArrayList<>();

        binding.rvGallery.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.rvGallery.setLayoutManager(gridLayoutManager);

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
                            ImageAdapter adapter = new ImageAdapter(MainActivity.this, imageList, MainActivity.this, 0);
                            binding.rvGallery.setAdapter(adapter);
                        }
                    }
                    binding.pbGallery.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                binding.pbGallery.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void itemClick(Hits hits, int position) {
        Intent intent = new Intent(MainActivity.this, FullScreenImageActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}