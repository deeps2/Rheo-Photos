package com.example.rheophotos.views.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rheophotos.R;
import com.example.rheophotos.model.SearchResponse;
import com.example.rheophotos.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SearchResponse.Results> resultsList;

    SearchAdapter(List<SearchResponse.Results> resultsList) {
        this.resultsList = resultsList;
    }

    void addMoreData(List<SearchResponse.Results> resultsListArg) {
        int startPosition;

        if (resultsList == null) {
            resultsList = new ArrayList<>();
        }

        startPosition = resultsList.size();
        int numberOfItemsAdded = resultsListArg.size();

        resultsList.addAll(resultsListArg);
        notifyItemRangeInserted(startPosition, numberOfItemsAdded);
    }

    void clearData() {
        if (resultsList != null) {
            resultsList.clear();
            notifyDataSetChanged();
        }
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_item_image)
        ImageView listItemImage;

        int allImagesWidth = 0;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        //during initial binding we won't know the image width. That can be found using ViewTreeObserver.OnGlobalLayoutListener()
        //once we get the width this function will not be called. as width remains same for all the photos in StaggeredGrid. that's why if (allImagesWidth == 0) check is written before function call
        //after getting the width we can set it using AppUtils.setImageViewHeight()function
        //we need width in order to set height according to aspect ratio of image. This will not crop the image
        private void getImageHeightUsingViewTreeObserver() {
            listItemImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    listItemImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    if (allImagesWidth == 0)
                        allImagesWidth = listItemImage.getWidth();

                    int position = getAdapterPosition();
                    if (position == RecyclerView.NO_POSITION)
                        return;

                    AppUtils.setImageViewHeight(listItemImage, resultsList.get(position), allImagesWidth);
                }
            });
        }

        void setData(SearchResponse.Results result) {
            if (allImagesWidth == 0)
                getImageHeightUsingViewTreeObserver();
            else
                AppUtils.setImageViewHeight(listItemImage, result, allImagesWidth);

            //in large projects this can be moved to another class like ImageRequestManager.java
            Glide.with(listItemImage.getContext()).load(result.getThumbnail()).placeholder(R.drawable.placeholder).into(listItemImage);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchViewHolder viewHolder = (SearchViewHolder) holder;
        viewHolder.setData(resultsList.get(position));
    }

    @Override
    public int getItemCount() {
        if (resultsList == null)
            return 0;
        else
            return resultsList.size();
    }
}
