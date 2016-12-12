package com.valentech.p4gguide;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.social_link_item, null);
            final String item = getItem(position);
            ImageView image = (ImageView) convertView.findViewById(R.id.social_link_item_image);
            int imgSourceID = getResources().getIdentifier("social_link_" + sanitizeItemName(item), "drawable", getActivity().getPackageName());

            image.setImageDrawable(getResources().getDrawable(imgSourceID));
            TextView itemName = (TextView) convertView.findViewById(R.id.social_link_item_name);
            itemName.setText(item);

            convertView.setLayoutParams(new GridView.LayoutParams(getWidth(), getHeight()));
            return convertView;
        }

        private int getWidth() {
            return Resources.getSystem().getDisplayMetrics().widthPixels / 3;
        }

        private int getHeight() {
            return Double.valueOf(getWidth() * 1.15).intValue();
        }

        private String sanitizeItemName(String item) {
            return item.replaceAll("[^a-zA-Z]", "").toLowerCase().trim();
        }
    }
}


