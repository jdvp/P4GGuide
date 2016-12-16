package com.valentech.p4gguide.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valentech.p4gguide.R;
import com.valentech.p4gguide.util.ResourceUtility;

/**
 * Fragment that displays the default main screen to the user.
 *
 * Contains sub-fragments depending on whether or not the user has any recently visited pages
 * Created by JD on 12/11/2016.
 */
public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.home_layout, container, false);

        String storedLinkPreference = ResourceUtility.getPreference(getActivity(), ResourceUtility.PREFERENCE_LAST_SOCIAL_LINK);
        if(storedLinkPreference != null && !storedLinkPreference.equals("")) {
            Bundle args = new Bundle();
            args.putSerializable("link", storedLinkPreference);

            FragmentManager fragmentManager = getFragmentManager();
            LastLinkFragment fragment = new LastLinkFragment();
            fragment.setArguments(args);
            fragmentManager.beginTransaction().add(R.id.home_container, fragment).commit();
        }
        return homeView;
    }
}


