package com.valentech.p4gguide.fragment;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.valentech.p4gguide.R;
import com.valentech.p4gguide.model.social_link.Availability;
import com.valentech.p4gguide.model.social_link.SocialLink;
import com.valentech.p4gguide.util.ResourceUtility;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.valentech.p4gguide.util.ResourceUtility.getPixelsFromDP;
import static com.valentech.p4gguide.util.ResourceUtility.getSocialLinkImgId;

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
            InputStream in_s = res.openRawResource(ResourceUtility.getRawId(getActivity(), name));

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

        List<String> days = Arrays.asList(getResources().getStringArray(R.array.social_link_days));
        ArrayList<CalendarItem> calendarItems = new ArrayList<>();

        ArrayList<CalendarItem> tempArray = new ArrayList<>();
        for(String day : days) {
            calendarItems.add(new CalendarItem(CalendarItemType.HEADER, day, false));

            try {
                Field field = Availability.class.getDeclaredField(ResourceUtility.sanitizeItemName(day));
                field.setAccessible(true);
                Boolean available = field.getBoolean(socialLink.getAvailability());
                tempArray.add(new CalendarItem(CalendarItemType.VALUE, "", available));
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }

        calendarItems.addAll(tempArray);

        GridView calendarGrid = (GridView) view.findViewById(R.id.calendar_grid);
        calendarGrid.setAdapter(new CalendarAdapter(getActivity(), R.layout.generic_grid_item, calendarItems));

        layout.addView(view);
    }

    private class CalendarAdapter extends ArrayAdapter<CalendarItem> {

        private LayoutInflater inflater;

        CalendarAdapter(Context context, int resource, ArrayList<CalendarItem> socialLinkItems) {
            super(context, resource, socialLinkItems);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.generic_grid_item, null);
            final CalendarItem item = getItem(position);
            RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.grid_item_container);

            if(item != null && item.getType() == CalendarItemType.HEADER) {
                TextView headerText = new TextView(getActivity());
                headerText.setText(item.getDayName());
                container.addView(headerText);
            } else if(item != null && item.getType() == CalendarItemType.VALUE) {
                TextView headerText = new TextView(getActivity());
                headerText.setText("X");
                if(item.isAvailable()) {
                    headerText.setText("O");
                }
                container.addView(headerText);
            }

            return convertView;
        }

        private int getWidth() {
            int sideMargins = getPixelsFromDP(16) * 2;
            int gridSpacing = getPixelsFromDP(8) * 2;
            return (Resources.getSystem().getDisplayMetrics().widthPixels - (sideMargins + gridSpacing))/ 8;
        }
    }

    private enum CalendarItemType {
        HEADER,
        VALUE
    }

    private class CalendarItem {
        private CalendarItemType type = CalendarItemType.HEADER;
        private String dayName = "";
        private boolean isAvailable = false;

        CalendarItem(CalendarItemType type, String dayName, boolean isAvailable) {
            this.type = type;
            this.dayName = dayName;
            this.isAvailable = isAvailable;
        }

        CalendarItemType getType() {
            return type;
        }

        String getDayName() {
            return dayName;
        }

        boolean isAvailable() {
            return isAvailable;
        }
    }
}


