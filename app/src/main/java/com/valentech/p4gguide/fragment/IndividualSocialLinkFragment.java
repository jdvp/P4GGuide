package com.valentech.p4gguide.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.valentech.p4gguide.R;
import com.valentech.p4gguide.model.social_link.SocialLink;
import com.valentech.p4gguide.util.StringUtil;

import java.io.InputStream;
import java.lang.reflect.Type;

import static com.valentech.p4gguide.util.StringUtil.getSocialLinkImgId;

/**
 * Created by JD on 12/11/2016.
 */

public class IndividualSocialLinkFragment extends Fragment {

    View myView;
    String name = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.individual_social_link_layout, container, false);
        LinearLayout layout = (LinearLayout) myView.findViewById(R.id.individual_social_link_container);

        name = getArguments().getString("link", "");

        Gson gson = new Gson();
        Type type = new TypeToken<SocialLink>(){}.getType();
        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(StringUtil.getRawId(getActivity(), name));

            byte[] b = new byte[in_s.available()];
            int read = in_s.read(b);
            if(read == 0) {
                throw new Exception("No file contents");
            }

            SocialLink socialLink = gson.fromJson(new String(b), type);
            displayLinkInformation(socialLink, layout, inflater);
        } catch (Exception e) {
            //set layout to failed to load
        }
        return myView;
    }

    private void displayLinkInformation(SocialLink socialLink, LinearLayout layout, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.social_link_header_card, layout, false);

        ImageView image = (ImageView) view.findViewById(R.id.link_header_card_image);

        image.setImageDrawable(getResources().getDrawable(getSocialLinkImgId(getActivity(), name)));
        TextView itemName = (TextView) view.findViewById(R.id.link_header_card_name);
        itemName.setText(name);

        TextView v = (TextView) view.findViewById(R.id.link_header_card_calendar);
        v.setText(socialLink.getAvailability().getLocation().getWeekdays());

        layout.addView(view);
    }
}


