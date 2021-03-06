package com.x_c0re.a0rganize;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;


public class FailedTasksAdapter extends BaseAdapter
{
    CircleImageView mAvatar;

    Context context;
    LayoutInflater inflater;
    ArrayList<FailedTask> failedTasks;

    GetJSONBodyOfUser jsonBodyOfUser;

    FailedTasksAdapter(Context context, ArrayList<FailedTask> failedTasks)
    {
        this.context = context;
        this.failedTasks = failedTasks;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return failedTasks.size();
    }

    @Override
    public Object getItem(int position)
    {
        return failedTasks.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null)
        {
            view = inflater.inflate(R.layout.failed_task_element, parent, false);
        }

        FailedTask f = getFailedTask(position);

        ((TextView)view.findViewById(R.id.textAuthorLogin_failed)).setText(f.author_login);
        ((TextView)view.findViewById(R.id.text_element_failed)).setText(f.text);
        ((TextView)view.findViewById(R.id.postRatingTextView)).setText(f.post_rating);

        mAvatar = view.findViewById(R.id.avatarIconofFailedElement);

        try
        {
            jsonBodyOfUser = new GetJSONBodyOfUser();
            jsonBodyOfUser.execute(f.author_login);
            String body = jsonBodyOfUser.get();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            ContactJSON contactJSON = gson.fromJson(body, ContactJSON.class);
            String full_adress_to_avatar = "http://overcome-api.herokuapp.com" + contactJSON.avatar;
            Uri uri = Uri.parse(full_adress_to_avatar);
            Glide.with(context).load(uri).into(mAvatar);

        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }

        return view;
    }

    FailedTask getFailedTask(int position)
    {
        return (FailedTask)getItem(position);
    }

    // ?????????? ?????????? ?????????? ???? ???????????? ????????????????????????, "??????????????????" URL ?????????????? ?? ???????????????? ?????? ?????????? Glide ?? CircleImageView

    private static class GetJSONBodyOfUser extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            HttpRequest request = HttpRequest.get("http://overcome-api.herokuapp.com/contacts/find_by_login/" + strings[0]);
            return request.body();
        }
    }
}
