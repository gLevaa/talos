package data.parsed;

import org.json.JSONObject;

public class Submission {
    public String id;
    public String subreddit;
    public String text;
    public int numberOfUpvotes;
    public boolean isScoreHidden;
    public String link;

    public String authorId;
    public boolean isAuthorPremium;

    public int numberOfAwards;
    public int numberOfChildren;

    public int publishedAt;

    public boolean isLocked;

    public Submission(Object id, Object subreddit, Object text, Object numberOfUpvotes, Object isScoreHidden, Object link, Object authorId, Object isAuthorPremium, Object numberOfAwards, Object numberOfChildren, Object publishedAt, Object isLocked) {
        this.id =               (!JSONObject.NULL.equals(id))               ? (String)id : null;
        this.subreddit =        (!JSONObject.NULL.equals(subreddit))        ? (String)subreddit : null;
        this.text =             (!JSONObject.NULL.equals(text))             ? (String)text : null;
        this.numberOfUpvotes =  (!JSONObject.NULL.equals(numberOfUpvotes))  ? (int)numberOfUpvotes : -1;
        this.isScoreHidden =    (!JSONObject.NULL.equals(isScoreHidden))    ? (boolean)isScoreHidden : false;
        this.link =             (!JSONObject.NULL.equals(link))             ? (String)link : null;
        this.authorId =         (!JSONObject.NULL.equals(authorId))         ? (String)authorId : null;
        this.isAuthorPremium =  (!JSONObject.NULL.equals(isAuthorPremium))  ? (boolean)isAuthorPremium : false;
        this.numberOfAwards =   (!JSONObject.NULL.equals(numberOfAwards))   ? (int)numberOfAwards : -1;
        this.numberOfChildren = (!JSONObject.NULL.equals(numberOfChildren)) ? (int)numberOfChildren : -1;
        this.publishedAt =      (!JSONObject.NULL.equals(publishedAt))      ? (int)publishedAt : -1;
        this.isLocked =         (!JSONObject.NULL.equals(isLocked))         ? (boolean)isLocked : false;
    }
}
