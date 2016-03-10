package edu.lclark.githubfragmentapplication.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.lclark.githubfragmentapplication.FollowerFragmentStatePagerAdapter;
import edu.lclark.githubfragmentapplication.NetworkAsyncTask;
import edu.lclark.githubfragmentapplication.R;
import edu.lclark.githubfragmentapplication.models.GithubUser;

/**
 * Created by Dominic on 3/10/2016.
 */
public class TabActivity extends AppCompatActivity implements NetworkAsyncTask.GithubListener {
    public static final String ARG_TAB_USER = "arg_tab_user";
    private GithubUser mUser;
    private ArrayList<GithubUser> mFollowers;
    private NetworkAsyncTask mAsyncTask;
    private FollowerFragmentStatePagerAdapter mFragmentStatePagerAdapter;

    @Bind(R.id.activity_tab_tab_layout)
    TabLayout mTabLayout;

    @Bind(R.id.activity_tab_viewpager)
    ViewPager mViewPager;

    @Bind(R.id.activity_tab_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        ButterKnife.bind(this);

        mUser = getIntent().getParcelableExtra(ARG_TAB_USER);
        mFragmentStatePagerAdapter = new FollowerFragmentStatePagerAdapter(getSupportFragmentManager(), mFollowers);
        mViewPager.setAdapter(mFragmentStatePagerAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mToolbar.setTitle(mUser.getLogin());
        setSupportActionBar(mToolbar);


    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAsyncTask == null && (mFollowers == null || mFollowers.isEmpty())) {
            mAsyncTask = new NetworkAsyncTask(this);
            mAsyncTask.execute(mUser.getLogin());
        }
    }

    @Override
    public void onGithubFollowersRetrieved(@Nullable ArrayList<GithubUser> followers) {
        Log.d("TAG", "onGithubFollowersRetrieved called");
        mFollowers = followers;
        mFragmentStatePagerAdapter.setFollowers(followers);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
