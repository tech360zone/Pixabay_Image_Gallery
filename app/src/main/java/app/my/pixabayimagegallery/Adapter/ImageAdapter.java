package app.my.pixabayimagegallery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.my.pixabayimagegallery.Interface.AdapterClick;
import app.my.pixabayimagegallery.Model.Hits;
import app.my.pixabayimagegallery.R;

public class ImageAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<Hits> imgList;
    private final AdapterClick adapterClick;
    private final int adapterType;

    public ImageAdapter(Context context, List<Hits> imgList, AdapterClick adapterClick, int adapterType) {
        this.context = context;
        this.imgList = imgList;
        this.adapterClick = adapterClick;
        this.adapterType = adapterType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (adapterType == 0) {
            View view = layoutInflater.inflate(R.layout.image_row_layout, parent, false);
            return new ImageThumbnailViewHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.image_full_screen_row_layout, parent, false);
            return new ImageFullScreenViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (adapterType == 0) {
            ((ImageThumbnailViewHolder) holder).bidData(imgList.get(position));
        } else {
            ((ImageFullScreenViewHolder) holder).bidData(imgList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public class ImageThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivThumbnail;

        public ImageThumbnailViewHolder(@NonNull View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            itemView.setOnClickListener(this);
        }

        public void bidData(Hits hits) {
            Glide.with(context).load(hits.getLargeImageURL()).into(ivThumbnail);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            adapterClick.itemClick(imgList.get(position), position);
        }
    }

    public class ImageFullScreenViewHolder extends RecyclerView.ViewHolder {

        ImageView ivFullScreenImage;

        public ImageFullScreenViewHolder(@NonNull View itemView) {
            super(itemView);

            ivFullScreenImage = itemView.findViewById(R.id.ivFullScreenImg);
        }

        public void bidData(Hits hits) {
            Glide.with(context).load(hits.getLargeImageURL()).into(ivFullScreenImage);
        }
    }
}
