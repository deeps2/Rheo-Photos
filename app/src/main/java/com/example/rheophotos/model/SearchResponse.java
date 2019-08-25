package com.example.rheophotos.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("value")
    private List<Results> resultsList;

    @SerializedName("nextOffset")
    private int nextOffset;

    @SerializedName("totalEstimatedMatches")
    private int totalEstimatedMatches;

    public List<Results> getResultsList() {
        return resultsList;
    }

    public int getNextOffset() {
        return nextOffset;
    }

    public int getTotalEstimatedMatches() {
        return totalEstimatedMatches;
    }

    public static class Results {
        @SerializedName("thumbnailUrl")
        private String thumbnail;

        @SerializedName("thumbnail")
        private ThumbnailMeta thumbnailMeta;

        public String getThumbnail() {
            return thumbnail;
        }

        public ThumbnailMeta getThumbnailMeta() {
            return thumbnailMeta;
        }

        public static class ThumbnailMeta {

            @SerializedName("width")
            private int width;

            @SerializedName("height")
            private int height;

            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }
        }
    }
}
