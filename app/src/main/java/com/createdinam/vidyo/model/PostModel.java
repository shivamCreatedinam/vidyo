package com.createdinam.vidyo.model;

public class PostModel {

    /**
     * id : 45
     * title : sample video 2
     * content :
     * upload_videos : 46
     * slug : sample-video-2
     * featured_image : {"thumbnail":"https://createdinam.com/wp-content/uploads/2020/06/bnnr-150x150.jpg","medium":"https://createdinam.com/wp-content/uploads/2020/06/bnnr-300x205.jpg","large":"https://createdinam.com/wp-content/uploads/2020/06/bnnr.jpg"}
     */

    private int id;
    private String title;
    private String content;
    private String upload_videos;
    private String slug;
    private FeaturedImageBean featured_image;

    public PostModel(int id, String title, String content, String upload_videos, String slug, FeaturedImageBean featured_image) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.upload_videos = upload_videos;
        this.slug = slug;
        this.featured_image = featured_image;
    }

    public PostModel(String title, String content, String upload_videos, String slug, FeaturedImageBean featured_image) {
        this.title = title;
        this.content = content;
        this.upload_videos = upload_videos;
        this.slug = slug;
        this.featured_image = featured_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpload_videos() {
        return upload_videos;
    }

    public void setUpload_videos(String upload_videos) {
        this.upload_videos = upload_videos;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public FeaturedImageBean getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(FeaturedImageBean featured_image) {
        this.featured_image = featured_image;
    }

    public static class FeaturedImageBean {
        /**
         * thumbnail : https://createdinam.com/wp-content/uploads/2020/06/bnnr-150x150.jpg
         * medium : https://createdinam.com/wp-content/uploads/2020/06/bnnr-300x205.jpg
         * large : https://createdinam.com/wp-content/uploads/2020/06/bnnr.jpg
         */

        private String thumbnail;
        private String medium;
        private String large;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }
    }
}
