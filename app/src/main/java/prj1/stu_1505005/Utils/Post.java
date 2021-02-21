package prj1.stu_1505005.Utils;

public class Post {
    String id;
    String post_date;
    String account_id;
    String title;
    String txt;
    String student_hash;

    public Post(String id, String post_date, String account_id, String title, String txt, String student_hash) {
        this.id = id;
        this.post_date = post_date;
        this.account_id = account_id;
        this.title = title;
        this.txt = txt;
        this.student_hash = student_hash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getStudent_hash() {
        return student_hash;
    }

    public void setStudent_hash(String student_hash) {
        this.student_hash = student_hash;
    }


}
