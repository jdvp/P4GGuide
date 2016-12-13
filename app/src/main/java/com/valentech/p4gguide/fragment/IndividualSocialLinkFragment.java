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
import com.valentech.p4gguide.model.social_link.Location;
import com.valentech.p4gguide.model.social_link.SocialLink;
import com.valentech.p4gguide.util.ResourceUtility;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

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

        //set title and image
        ImageView image = (ImageView) view.findViewById(R.id.link_header_card_image);
        image.setImageDrawable(getResources().getDrawable(getSocialLinkImgId(getActivity(), name)));
        TextView itemName = (TextView) view.findViewById(R.id.link_header_card_name);
        itemName.setText(socialLink.getTitle());

        //set availability calendar
        GridView calendarGrid = (GridView) view.findViewById(R.id.calendar_grid);
        GridView locationGrid = (GridView) view.findViewById(R.id.location_grid);
        if(socialLink.getAvailability() != null) {
            calendarGrid.setAdapter(new CalendarAdapter(getActivity(), R.layout.generic_grid_item, getCalendarItems(socialLink)));
            locationGrid.setAdapter(new LocationAdapter(getActivity(), R.layout.generic_grid_item, getLocationItems(socialLink.getAvailability().getLocation())));
        } else {
            calendarGrid.setVisibility(View.GONE);
            locationGrid.setVisibility(View.GONE);
        }

        //set activation info
        TextView activationTextView = (TextView) view.findViewById(R.id.social_link_activation);
        if(socialLink.getAvailability() != null && socialLink.getAvailability().getActivation() != null) {
            activationTextView.setText(socialLink.getAvailability().getActivation());
        } else {
            view.findViewById(R.id.social_link_activation_heading).setVisibility(View.GONE);
            activationTextView.setVisibility(View.GONE);
        }

        //set last day info
        TextView lastDayTextView = (TextView) view.findViewById(R.id.social_link_last);
        if(socialLink.getAvailability() != null && socialLink.getAvailability().getLast() != null) {
            lastDayTextView.setText(socialLink.getAvailability().getLast());
        } else {
            view.findViewById(R.id.social_link_last_heading).setVisibility(View.GONE);
            lastDayTextView.setVisibility(View.GONE);
        }

        //set notes section
        TextView notesTextView = (TextView) view.findViewById(R.id.social_link_notes);
        if(socialLink.getNotes() != null) {
            notesTextView.setText(socialLink.getNotes());
        } else {
            view.findViewById(R.id.social_link_notes_heading).setVisibility(View.GONE);
            notesTextView.setVisibility(View.GONE);
        }
        layout.addView(view);
    }

    private ArrayList<CalendarItem> getCalendarItems(SocialLink socialLink) {
        List<String> days = Arrays.asList(getResources().getStringArray(R.array.social_link_days));
        ArrayList<CalendarItem> calendarItems = new ArrayList<>();
        for(String day : days) {
            try {
                Field field = Availability.class.getDeclaredField(ResourceUtility.sanitizeItemName(day));
                field.setAccessible(true);
                Boolean available = field.getBoolean(socialLink.getAvailability());
                calendarItems.add(new CalendarItem(day, available));
            } catch (Exception ignored) {
                //assume false
                if(day != null) {
                    calendarItems.add(new CalendarItem(day, false));
                }
            }
        }
        return calendarItems;
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

            if(item != null) {
                TextView headerText = new TextView(getActivity());
                headerText.setText(item.getDayName());
                container.addView(headerText);

                if(item.isAvailable) {
                    container.setBackgroundColor(getActivity().getResources().getColor(R.color.available));
                } else {
                    container.setBackgroundColor(getActivity().getResources().getColor(R.color.unavailable));
                }
            }


            return convertView;
        }
    }

    private ArrayList<String> getLocationItems(Location location) {
        List<String> days = Arrays.asList(getResources().getStringArray(R.array.social_link_locations));
        ArrayList<String> locationItems = new ArrayList<>();

        locationItems.add(days.get(0));
        if(location.getWeekdays() != null) {
            locationItems.add(location.getWeekdays());
        } else {
            locationItems.add(getString(R.string.not_available));
        }

        locationItems.add(days.get(1));
        if(location.getSundays() != null) {
            locationItems.add(location.getWeekdays());
        } else {
            locationItems.add(getString(R.string.not_available));
        }
        return locationItems;
    }

    private class LocationAdapter extends ArrayAdapter<String> {

        private LayoutInflater inflater;

        LocationAdapter(Context context, int resource, ArrayList<String> socialLinkItems) {
            super(context, resource, socialLinkItems);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.generic_grid_item, null);
            final String item = getItem(position);
            RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.grid_item_container);

            if(item != null) {
                TextView textView = new TextView(getActivity());
                textView.setText(item);
                container.addView(textView);
            }

            return convertView;
        }
    }



    private class CalendarItem {
        private String dayName = "";
        private boolean isAvailable = false;

        CalendarItem(String dayName, boolean isAvailable) {
            this.dayName = dayName;
            this.isAvailable = isAvailable;
        }

        String getDayName() {
            return dayName;
        }

        boolean isAvailable() {
            return isAvailable;
        }
    }
}


