package data.parsed;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Post extends Submission {
    public String type;
    public String title;
    public String flair;
    public float upvoteRatio;
    public int numberOfCrossposts;
    public boolean isNsfw;

    private JSONArray commentData;
    public ArrayList<Comment> comments;

    public Post(JSONObject parsedPost) {
        super(parsedPost.get("id"), parsedPost.get("subreddit"), parsedPost.get("text"), parsedPost.get("upvotes"), parsedPost.get("is_score_hidden"), parsedPost.get("link"),
                parsedPost.get("author_id"), parsedPost.get("is_author_premium"), parsedPost.get("num_awards"), parsedPost.get("num_children"),
                 parsedPost.get("published_at"), parsedPost.get("is_locked"));

        this.type = (!JSONObject.NULL.equals(type))                 ? (String) parsedPost.get("type") : null;
        this.title = (!JSONObject.NULL.equals(title))               ? (String) parsedPost.get("title") : null;
        this.flair = (!JSONObject.NULL.equals(flair))               ? (String) parsedPost.get("flair") : null;
        this.upvoteRatio = (!JSONObject.NULL.equals(upvoteRatio))   ? ((BigDecimal)parsedPost.get("upvote_ratio")).floatValue() : -1;
        this.isNsfw = (!JSONObject.NULL.equals(isNsfw))             ? (boolean) parsedPost.get("is_over_18") : false;
        this.numberOfCrossposts = (!JSONObject.NULL.equals(numberOfCrossposts)) ? (int) parsedPost.get("num_crossposts") : -1;

        this.commentData = (JSONArray) parsedPost.get("comments");
        this.comments = new ArrayList<>();

        readCommentsToObjects();
    }

    private void readCommentsToObjects() {
        if (commentData.isEmpty() || numberOfChildren == -1) {
            return;
        }

        for (Object comment : commentData) {
            try {
                System.out.println((JSONObject)comment);

                JSONObject parsedCommentRaw = (JSONObject) comment;
                Comment parsedComment = new Comment(parsedCommentRaw);

                comments.add(parsedComment);

                System.out.println(1);
            } catch (Exception e) {
                // TODO
                throw(e);
            }
        }
    }
}
