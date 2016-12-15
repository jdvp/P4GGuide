package com.valentech.p4gguide.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.valentech.p4gguide.R;

import static com.valentech.p4gguide.util.ResourceUtility.getPixelsFromDP;

/**
 * Created by JD on 12/11/2016.
 */

public class LastLinkFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.last_link_layout, container, false);
        myView.findViewById(R.id.link_tv).setLayoutParams(new RelativeLayout.LayoutParams(getSize(), getSize()));
        return myView;
    }

    private int getSize() {
        int sideMargins = getPixelsFromDP(16) * 2;
        return (Resources.getSystem().getDisplayMetrics().widthPixels - (sideMargins));
    }

}


