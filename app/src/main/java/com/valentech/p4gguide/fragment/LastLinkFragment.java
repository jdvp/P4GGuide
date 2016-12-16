package com.valentech.p4gguide.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.valentech.p4gguide.R;
import com.valentech.p4gguide.util.ResourceUtility;

import static com.valentech.p4gguide.util.ResourceUtility.getPixelsFromDP;

/**
 * Fragment that shows the user's most recently visited social link
 *
 * Created by JD on 12/11/2016.
 */

public class LastLinkFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.last_link_layout, container, false);
        View tv = myView.findViewById(R.id.link_tv);
        tv.setLayoutParams(new FrameLayout.LayoutParams(getSize(), getSize()));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putSerializable("link", getArguments().getString("link", ""));
                SocialLinkFragment newFragment = new SocialLinkFragment();
                newFragment.setArguments(args);

                ResourceUtility.savePreference(getActivity(),
                        ResourceUtility.PREFERENCE_LAST_SOCIAL_LINK, getArguments().getString("link", ""));
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, newFragment).
                        addToBackStack("social link individual").commit();
            }
        });

        return myView;
    }

    private int getSize() {
        int sideMargins = getPixelsFromDP(16) * 2;
        return (Resources.getSystem().getDisplayMetrics().widthPixels - (sideMargins));
    }

}


