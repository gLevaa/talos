package data.parsed;

import org.json.JSONObject;

import java.lang.reflect.Field;

public class Comment extends Submission {
    public String parentId;
    public int depth;
    public boolean isControversial;


    public Comment(JSONObject parsedComment) {
        super(parsedComment.get("id"), parsedComment.get("subreddit"), parsedComment.get("text"), parsedComment.get("upvotes"), parsedComment.get("is_score_hidden"), parsedComment.get("link"),
                parsedComment.get("author_id"), parsedComment.get("is_author_premium"), parsedComment.get("num_awards"), parsedComment.get("num_children"),
                parsedComment.get("published_at"), parsedComment.get("is_locked"));

        this.parentId =         (!JSONObject.NULL.equals(parentId))         ? (String)parsedComment.get("parent_id") : null;
        this.depth =            (!JSONObject.NULL.equals(depth))            ? (int)parsedComment.get("depth") : -1;
        this.isControversial =  (!JSONObject.NULL.equals(isControversial))  ? (boolean) parsedComment.get("is_controversial") : false;
    }
}
