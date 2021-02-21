package prj1.stu_1505005.ViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import prj1.stu_1505005.R;
import prj1.stu_1505005.Utils.Post;

import static prj1.stu_1505005.PostListActivity.postArrayList;

public class PostsListViewAdapter extends BaseAdapter {
    public static final String ADAPTER_LOG_TAG = "PostListViewAdapter";
    Context context;
    LayoutInflater layoutInflater;

    public PostsListViewAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return postArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.post_item, null);
        } else {
            Log.e(ADAPTER_LOG_TAG, "convertView is not null");
        }

        Post post = postArrayList.get(position);
        TextView tvTitle = convertView.findViewById(R.id.post_item_title_tv);
        TextView tvText = convertView.findViewById(R.id.post_item_text_tv);

        tvTitle.setText(post.getTitle());
        tvText.setText(post.getTxt());

        return convertView;
    }

}
