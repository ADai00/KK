package edu.hnie.kk.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.hnie.kk.R;
import edu.hnie.kk.SearchFriendsOrTeamActivity;
import edu.hnie.kk.SelectMembersActivity;
import edu.hnie.kk.adapter.MyFragmentPagerAdapter;


public class AddressBookFragment extends Fragment {
    private TextView commonTitle;
    private TextView commonAdd;
    private CircleImageView commonAvatar;
    private ViewPager addressBookViewPager;
    private TextView addressBookFriends;
    private TextView addressBookTeam;
    private RelativeLayout createTeamLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_address_book, container, false);

        //初始化title信息
        commonTitle = view.findViewById(R.id.common_title);
        commonAdd = view.findViewById(R.id.common_add);
        commonAvatar = view.findViewById(R.id.common_avatar);
        commonTitle.setText("联系人");
        commonAdd.setText("添加");
        commonAvatar.setImageResource(R.drawable.common_avatar);
        commonAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout myDrawer = getActivity().findViewById(R.id.my_drawer);
                myDrawer.openDrawer(GravityCompat.START);
            }
        });
        commonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SearchFriendsOrTeamActivity.class);
                startActivity(intent);
            }
        });


        //初始化组件
        addressBookViewPager = view.findViewById(R.id.address_book_view_pager);
        addressBookFriends = view.findViewById(R.id.address_book_friends);
        addressBookTeam = view.findViewById(R.id.address_book_team);
        createTeamLayout = view.findViewById(R.id.create_team_layout);

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
        addressBookViewPager.setAdapter(myFragmentPagerAdapter);

        addressBookFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressBookViewPager.setCurrentItem(0);
                addressBookFriends.setTextColor(getResources().getColor(R.color.titleblue));
                addressBookTeam.setTextColor(getResources().getColor(R.color.gray));
            }
        });

        addressBookTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressBookViewPager.setCurrentItem(1);
                addressBookFriends.setTextColor(getResources().getColor(R.color.gray));
                addressBookTeam.setTextColor(getResources().getColor(R.color.titleblue));
            }
        });

        createTeamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SelectMembersActivity.class);
                startActivity(intent);
            }
        });

        addressBookViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    switch (addressBookViewPager.getCurrentItem()) {
                        case 0:
                            addressBookFriends.setTextColor(getResources().getColor(R.color.titleblue));
                            addressBookTeam.setTextColor(getResources().getColor(R.color.gray));
                            break;
                        case 1:
                            addressBookFriends.setTextColor(getResources().getColor(R.color.gray));
                            addressBookTeam.setTextColor(getResources().getColor(R.color.titleblue));
                            break;
                    }
                }
            }
        });
        return view;
    }

}
