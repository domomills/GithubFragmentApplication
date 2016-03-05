package edu.lclark.githubfragmentapplication.fragments;

import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.lclark.githubfragmentapplication.LoginAsyncTask;
import edu.lclark.githubfragmentapplication.NetworkAsyncTask;
import edu.lclark.githubfragmentapplication.R;
import edu.lclark.githubfragmentapplication.activities.MainActivity;
import edu.lclark.githubfragmentapplication.models.GithubUser;

/**
 * Created by Dominic on 3/4/2016.
 */
public class LoginFragment extends Fragment implements LoginAsyncTask.OnUserNotFoundListener {

    @Bind(R.id.fragment_login_button)
    Button mButton;
    @Bind(R.id.fragment_login_edit_text)
    EditText mEditText;
    @Bind(R.id.fragment_login_image_view)
    ImageView mImageView;
    @Bind(R.id.fragment_login_progress_bar)
    ProgressBar mProgressBar;

    private ArrayList<GithubUser> mFollowers;
    private NetworkAsyncTask mAsyncTask;
    private String mLoginName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);
        Picasso.with(getContext()).load("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png").fit().centerInside().into(mImageView);
        mProgressBar.setVisibility(View.INVISIBLE);
        return rootView;
    }

    @OnClick(R.id.fragment_login_button)
    public void onButtonClick() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        mButton.setEnabled(false);
        if(networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(getContext(), R.string.fragment_login_connection_error, Toast.LENGTH_SHORT).show();
            mButton.setEnabled(true);
        } else {
            mLoginName = mEditText.getText().toString();
            LoginAsyncTask mAsyncTask = new LoginAsyncTask(this, (LoginAsyncTask.OnUserFoundListener) getActivity(), mLoginName);
            mAsyncTask.execute(mLoginName);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
            mAsyncTask.cancel(true);
            mAsyncTask = null;
        }
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onUserNotFound() {
        Toast.makeText(getContext(), R.string.fragment_login_user_error, Toast.LENGTH_SHORT).show();
        mButton.setEnabled(true);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
