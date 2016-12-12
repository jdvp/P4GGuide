package com.valentech.p4gguide.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valentech.p4gguide.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.valentech.p4gguide.util.ResourceUtility.getPixelsFromDP;
import static com.valentech.p4gguide.util.ResourceUtility.getSocialLinkImgId;

/**
 * Created by JD on 12/11/2016.
 */

public class SocialLinkFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.social_link_layout, container, false);

        GridView socialLinkGrid = (GridView) myView.findViewById(R.id.social_link_grid);
        List<String> items = Arrays.asList(getResources().getStringArray(R.array.social_link_name_list));
        ArrayList<String> itemsArrayList = new ArrayList<>();
        itemsArrayList.addAll(items);

        socialLinkGrid.setAdapter(new SocialLinkItemAdapter(getActivity(), R.layout.social_link_item, itemsArrayList));
        return myView;
    }

    private class SocialLinkItemAdapter extends ArrayAdapter<String> {

        private LayoutInflater inflater;

        SocialLinkItemAdapter(Context context, int resource, ArrayList<String> socialLinkItems) {
            super(context, resource, socialLinkItems);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.social_link_item, null);
            final String item = getItem(position);
            ImageView image = (ImageView) convertView.findViewById(R.id.social_link_item_image);

            image.setImageDrawable(getResources().getDrawable(getSocialLinkImgId(getActivity(), item)));
            TextView itemName = (TextView) convertView.findViewById(R.id.social_link_item_name);
            itemName.setText(item);

            if(position < 3) {
                View card = convertView.findViewById(R.id.social_link_card);
                if(card != null && card.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) card.getLayoutParams();
                    params.setMargins(0, getPixelsFromDP(16), 0, 0 );
                    card.setLayoutParams(params);
                }
            }

            convertView.setLayoutParams(new GridView.LayoutParams(getWidth(), GridView.AUTO_FIT));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putSerializable("link", getItem(position));
                    IndividualSocialLinkFragment newFragment = new IndividualSocialLinkFragment();
                    newFragment.setArguments(args);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, newFragment).
                            addToBackStack("social link individual").commit();
                }
            });
            return convertView;
        }

        private int getWidth() {
            int sideMargins = getPixelsFromDP(16) * 2;
            int gridSpacing = getPixelsFromDP(8) * 2;
            return (Resources.getSystem().getDisplayMetrics().widthPixels - (sideMargins + gridSpacing))/ 3;
        }
    }
}

