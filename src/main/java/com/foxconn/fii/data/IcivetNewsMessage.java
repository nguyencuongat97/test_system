package com.foxconn.fii.data;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class IcivetNewsMessage {

    private Date createTime;

    private List<IcivetArticle> articles;

    public static IcivetNewsMessage of (List<IcivetArticle> articles, Date createTime) {
        IcivetNewsMessage instance = new IcivetNewsMessage();
        instance.setArticles(articles);
        instance.setCreateTime(createTime);
        return instance;
    }

    public static IcivetNewsMessage of (List<IcivetArticle> articles) {
        return  of(articles, new Date());
    }

    @Data
    public static class IcivetArticle {

        private String author;

        private String title;

        private String description;

        private String url;

        private String mediaId;

        private String mediaUrl;

        private String mediaRaw;

        public static IcivetArticle of(String author, String title, String description, String url, String mediaRaw) {
            IcivetArticle instance = new IcivetArticle();
            instance.setAuthor(author);
            instance.setTitle(title);
            instance.setDescription(description);
            instance.setUrl(url);
            instance.setMediaRaw(mediaRaw);
            return instance;
        }

        public static IcivetArticle of(String author, String title, String description, String url, String mediaId, String mediaUrl) {
            IcivetArticle instance = of(author, title, description, url, null);
            instance.setMediaId(mediaId);
            instance.setMediaUrl(mediaUrl);
            return instance;
        }
    }
}
