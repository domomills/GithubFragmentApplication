package edu.lclark.githubfragmentapplication;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.lclark.githubfragmentapplication.models.GithubUser;

/**
 * Created by parulsohal on 3/4/16.
 */
public class LoginAsyncTask extends AsyncTask<String, String, GithubUser>{

    OnUserFoundListener mFoundListener;
    OnUserNotFoundListener mNotFoundListener;


    public static final String TAG = LoginAsyncTask.class.getSimpleName();

    public LoginAsyncTask(OnUserNotFoundListener notFoundListener, OnUserFoundListener onFoundListener, String editText) {
        mFoundListener = onFoundListener;
        mNotFoundListener = notFoundListener;
    }


    @Override
    protected GithubUser doInBackground(String... params) {

        StringBuilder responseBuilder = new StringBuilder();
        GithubUser user = null;
        if (params.length == 0) {
            return null;
        }

        String userId = params[0];

        try {
            URL url = new URL("https://api.github.com/users/" + userId );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStream);
            String line;

            if (isCancelled()) {
                return null;
            }
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);

                if (isCancelled()) {
                    return null;
                }
            }

            user = new Gson().fromJson(responseBuilder.toString(), GithubUser.class);

            if (isCancelled()) {
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        return user;
    }


    public interface OnUserFoundListener{
        void onUserFound(GithubUser user);
    }

    public interface OnUserNotFoundListener{
        void onUserNotFound();
    }


    public void onPostExecute(GithubUser user){
        super.onPostExecute(user);
        if(user == null){
            mNotFoundListener.onUserNotFound();
        }else {
            mFoundListener.onUserFound(user);
        }
    }


}