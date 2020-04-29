package com.createdinam.vidyo.global;

import java.util.List;

public class SliderModel {

    /**
     * success : true
     * data : {"memes":[{"id":"181913649","name":"Drake Hotline Bling","url":"https://i.imgflip.com/30b1gx.jpg","width":1200,"height":1200,"box_count":2},{"id":"112126428","name":"Distracted Boyfriend","url":"https://i.imgflip.com/1ur9b0.jpg","width":1200,"height":800,"box_count":3},{"id":"87743020","name":"Two Buttons","url":"https://i.imgflip.com/1g8my4.jpg","width":600,"height":908,"box_count":2},{"id":"195389","name":"Sparta Leonidas","url":"https://i.imgflip.com/46rh.jpg","width":500,"height":264,"box_count":2},{"id":"1367068","name":"I Should Buy A Boat Cat","url":"https://i.imgflip.com/tau4.jpg","width":500,"height":368,"box_count":2}]}
     */

    private boolean success;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<MemesBean> memes;

        public List<MemesBean> getMemes() {
            return memes;
        }

        public void setMemes(List<MemesBean> memes) {
            this.memes = memes;
        }

        public static class MemesBean {
            /**
             * id : 181913649
             * name : Drake Hotline Bling
             * url : https://i.imgflip.com/30b1gx.jpg
             * width : 1200
             * height : 1200
             * box_count : 2
             */

            private String id;
            private String name;
            private String url;
            private int width;
            private int height;
            private int box_count;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getBox_count() {
                return box_count;
            }

            public void setBox_count(int box_count) {
                this.box_count = box_count;
            }
        }
    }
}
