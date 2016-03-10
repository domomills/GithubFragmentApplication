package edu.lclark.githubfragmentapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import edu.lclark.githubfragmentapplication.fragments.TabUserFragment;
import edu.lclark.githubfragmentapplication.fragments.UserFragment;
import edu.lclark.githubfragmentapplication.models.GithubUser;

/**
 * Created by Dominic on 3/10/2016.
 */
public class FollowerFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<GithubUser> mFollowers;

    public FollowerFragmentStatePagerAdapter(FragmentManager fm, ArrayList<GithubUser> followers) {
        super(fm);
        mFollowers = followers;
    }

    @Override
    public Fragment getItem(int position) {
        return TabUserFragment.newInstance(mFollowers.get(position));
    }

    @Override
    public int getCount() {
//        Log.d("TAG", "getCount called");
        return mFollowers == null ? 0 : mFollowers.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFollowers.get(position).getLogin();
    }


    public void setFollowers(ArrayList<GithubUser> followers) {
        Log.d("TAG", "setFollowers() called");
        mFollowers = followers;
        notifyDataSetChanged();
    }
}
