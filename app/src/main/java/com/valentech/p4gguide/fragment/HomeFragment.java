package com.valentech.p4gguide.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.valentech.p4gguide.R;
import com.valentech.p4gguide.util.ResourceUtility;

/**
 * Created by JD on 12/11/2016.
 */

public class HomeFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home_layout, container, false);

        if(ResourceUtility.getPreference(getActivity(), ResourceUtility.PREFERENCE_LAST_SOCIAL_LINK) != null) {
            FragmentManager fragmentManager = getFragmentManager();
            LastLinkFragment fragment = new LastLinkFragment();
            fragmentManager.beginTransaction().add(R.id.home_container, fragment).commit();
        }
        return myView;
    }
}


