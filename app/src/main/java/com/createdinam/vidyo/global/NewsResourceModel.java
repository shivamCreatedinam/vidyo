package com.createdinam.vidyo.global;

import java.util.List;

public class NewsResourceModel {

    /**
     * status : ok
     * totalResults : 70
     * articles : [{"source":{"id":"reuters","name":"Reuters"},"author":"William James","title":"UK government faces mounting criticism over protection clothing shortages - Reuters India","description":"Doctors and health workers criticised the British government on Saturday for suggesting that personal protective equipment (PPE) worn while treating patients infected with coronavirus could be re-used, as supplies run low across the country.","url":"https://in.reuters.com/article/health-coronavirus-britain-idINKBN2200DR","urlToImage":"https://s3.reutersmedia.net/resources/r/?m=02&d=20200418&t=2&i=1515588461&w=1200&r=LYNXMPEG3H0DL","publishedAt":"2020-04-18T20:34:06Z","content":"LONDON (Reuters) - Doctors and health workers criticised the British government on Saturday for suggesting that personal protective equipment (PPE) worn while treating patients infected with coronavirus could be re-used, as supplies run low across the country\u2026 [+3581 chars]"},{"source":{"id":null,"name":"Indiatoday.in"},"author":null,"title":"5,000-year-old ancient scriptures describe something similar to coronavirus - India Today","description":"A coronavirus-like virus has been described in ancient ayurvedic scriptures about 5,000 years ago. Dr Smita Naram, founder and CMD of Ayushakti revealed that she was surprised to find a chapter called 'Krimi' i.e. infections, in the 'Charak Samhita' that desc\u2026","url":"https://www.indiatoday.in/india/story/5000-year-old-ancient-scriptures-describe-something-similar-coronavirus-1668405-2020-04-18","urlToImage":"https://akm-img-a-in.tosshub.com/indiatoday/images/story/202004/chaiti_1-647x363.jpeg?mQ5kMoiRgPTI2FC3APN7QibAoVFDjs5q","publishedAt":"2020-04-18T12:01:05Z","content":"A coronavirus-like virus has been described in ancient ayurvedic scriptures about 5,000 years ago. Dr Smita Naram, founder and CMD of Ayushakti revealed that she was surprised to find a chapter called 'Krimi' i.e. infections, in the 'Charak Samhita' that desc\u2026 [+8619 chars]"}]
     */

    private String status;
    private int totalResults;
    private List<ArticlesBean> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<ArticlesBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticlesBean> articles) {
        this.articles = articles;
    }

    public static class ArticlesBean {
        /**
         * source : {"id":"reuters","name":"Reuters"}
         * author : William James
         * title : UK government faces mounting criticism over protection clothing shortages - Reuters India
         * description : Doctors and health workers criticised the British government on Saturday for suggesting that personal protective equipment (PPE) worn while treating patients infected with coronavirus could be re-used, as supplies run low across the country.
         * url : https://in.reuters.com/article/health-coronavirus-britain-idINKBN2200DR
         * urlToImage : https://s3.reutersmedia.net/resources/r/?m=02&d=20200418&t=2&i=1515588461&w=1200&r=LYNXMPEG3H0DL
         * publishedAt : 2020-04-18T20:34:06Z
         * content : LONDON (Reuters) - Doctors and health workers criticised the British government on Saturday for suggesting that personal protective equipment (PPE) worn while treating patients infected with coronavirus could be re-used, as supplies run low across the country… [+3581 chars]
         */

        private SourceBean source;
        private String author;
        private String title;
        private String description;
        private String url;
        private String urlToImage;
        private String publishedAt;
        private String content;

        public SourceBean getSource() {
            return source;
        }

        public void setSource(SourceBean source) {
            this.source = source;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlToImage() {
            return urlToImage;
        }

        public void setUrlToImage(String urlToImage) {
            this.urlToImage = urlToImage;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public static class SourceBean {
            /**
             * id : reuters
             * name : Reuters
             */

            private String id;
            private String name;

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
        }
    }
}
