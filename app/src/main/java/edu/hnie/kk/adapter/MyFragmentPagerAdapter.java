package edu.hnie.kk.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import edu.hnie.kk.fragment.FriendsFragment;
import edu.hnie.kk.fragment.TeamFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 2;
    private FriendsFragment friendsFragment = null;
    private TeamFragment groupChatFragment = null;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.friendsFragment = new FriendsFragment();
        this.groupChatFragment = new TeamFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = friendsFragment;
                break;
            case 1:
                fragment = groupChatFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
