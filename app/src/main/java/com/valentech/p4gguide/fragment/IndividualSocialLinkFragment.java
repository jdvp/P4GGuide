package com.valentech.p4gguide.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
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
import com.valentech.p4gguide.model.social_link.Choice;
import com.valentech.p4gguide.model.social_link.Location;
import com.valentech.p4gguide.model.social_link.Option;
import com.valentech.p4gguide.model.social_link.Rank;
import com.valentech.p4gguide.model.social_link.SocialLink;
import com.valentech.p4gguide.util.ResourceUtility;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
            createHeaderCard(socialLink, layout, inflater);
            createRankCards(socialLink, layout, inflater);
        } catch (Exception e) {
            //set layout to failed to load
        }
        return myView;
    }

    private void createRankCards(SocialLink socialLink, LinearLayout layout, LayoutInflater inflater) {
        if(socialLink.getRank() != null && !socialLink.getRank().isEmpty()) {
            View dialogueExplanationView = inflater.inflate(R.layout.dialgoue_explanation_card, layout, false);
            layout.addView(dialogueExplanationView);
        }
        for(Rank rank : socialLink.getRank()) {
            View view = inflater.inflate(R.layout.social_link_rank_card, layout, false);

            //set title
            TextView title = (TextView) view.findViewById(R.id.rank_card_title);
            title.setText(String.format(getString(R.string.social_link_ranking), rank.getLevel(), rank.getLevel() + 1));

            //set points needed
            TextView points = (TextView) view.findViewById(R.id.rank_card_points);
            points.setText(String.format(getString(R.string.social_link_points), rank.getPoints()));

            //set choices
            LinearLayout choiceLayout = (LinearLayout) view.findViewById(R.id.choice_list);
            boolean isEvenRow = false;
            for(Choice choice : rank.getChoices()) {
                View choiceView = inflater.inflate(R.layout.choice_item, layout, false);

                if(isEvenRow) {
                    choiceView.setBackgroundColor(getActivity().getResources().getColor(R.color.dialogue_row_one));
                } else {
                    choiceView.setBackgroundColor(getActivity().getResources().getColor(R.color.dialogue_row_two));
                }

                isEvenRow = !isEvenRow;

                TextView dialogue = (TextView) choiceView.findViewById(R.id.dialogue);
                dialogue.setText(choice.getDialogue());

                if(choice.getSpecial() != null) {
                    TextView special = (TextView) choiceView.findViewById(R.id.special);
                    special.setText(choice.getSpecial());
                    special.setVisibility(View.VISIBLE);
                }

                ArrayList<String> optionArray = new ArrayList<>();
                optionArray.add("");
                optionArray.add("W");
                optionArray.add("W/O");

                ArrayList<Option> options = choice.getOptions();
                for(int i = 0; i < options.size(); i++) {
                    optionArray.add((i + 1) + ". " + options.get(i).getResponse());
                    String with = options.get(i).getWith();
                    if(with == null) {
                        with = "-";
                    }
                    String without = options.get(i).getWithout();
                    if(without == null) {
                        without = "-";
                    }
                    optionArray.add(with);
                    optionArray.add(without);
                }

                GridView choiceGrid = (GridView) choiceView.findViewById(R.id.choice_grid);
                choiceGrid.setAdapter(new GenericGridTextAdapter(getActivity(), R.layout.generic_grid_item, optionArray, true));
                choiceLayout.addView(choiceView);
            }

            //set results
            TextView results = (TextView) view.findViewById(R.id.social_link_results);
            if(rank.getResults() != null) {
                results.setText(rank.getResults());
            } else {
                view.findViewById(R.id.social_link_results_heading).setVisibility(View.GONE);
                results.setVisibility(View.GONE);
            }
            layout.addView(view);
        }
    }

    private void createHeaderCard(SocialLink socialLink, LinearLayout layout, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.social_link_header_card, layout, false);

        //set title and image
        ImageView image = (ImageView) view.findViewById(R.id.link_header_card_image);
        image.setImageDrawable(getResources().getDrawable(getSocialLinkImgId(getActivity(), name)));
        TextView itemName = (TextView) view.findViewById(R.id.link_header_card_name);
        itemName.setText(socialLink.getTitle());

        //set availability calendar
        GridView calendarGrid = (GridView) view.findViewById(R.id.calendar_grid);
        ArrayList<CalendarItem> calendarItems = getCalendarItems(socialLink);
        if(!calendarItems.isEmpty()) {
            calendarGrid.setAdapter(new CalendarAdapter(getActivity(), R.layout.generic_grid_item, calendarItems));
        } else {
            view.findViewById(R.id.calendar_grid_header).setVisibility(View.GONE);
            calendarGrid.setVisibility(View.GONE);
        }

        //set location availability
        GridView locationGrid = (GridView) view.findViewById(R.id.location_grid);
        if(socialLink.getAvailability() != null && socialLink.getAvailability().getLocation() != null) {
            locationGrid.setAdapter(new GenericGridTextAdapter(getActivity(), R.layout.generic_grid_item, getLocationItems(socialLink.getAvailability().getLocation())));
        } else {
            view.findViewById(R.id.location_grid_header).setVisibility(View.GONE);
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
        if(socialLink.getAvailability() == null) {
            return new ArrayList<>();
        }

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

        //check for unique values
        HashSet<Boolean> vals = new HashSet<>();
        for(CalendarItem item : calendarItems) {
            vals.add(item.isAvailable());
        }
        if(vals.size() <= 1) {
            calendarItems.clear();
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

                if(item.isAvailable()) {
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

    private class GenericGridTextAdapter extends ArrayAdapter<String> {

        private LayoutInflater inflater;
        private boolean isTwoLines = false;

        GenericGridTextAdapter(Context context, int resource, ArrayList<String> socialLinkItems) {
            super(context, resource, socialLinkItems);
            inflater = LayoutInflater.from(context);
        }

        GenericGridTextAdapter(Context context, int resource, ArrayList<String> socialLinkItems, boolean isTwoLines) {
            super(context, resource, socialLinkItems);
            inflater = LayoutInflater.from(context);
            this.isTwoLines = isTwoLines;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.generic_grid_item, null);
            final String item = getItem(position);
            RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.grid_item_container);

            if(item != null) {
                TextView textView = new TextView(getActivity());
                textView.setText(item);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                container.addView(textView);

                if(isTwoLines) {
                    textView.setLines(2);
                    if(position % 3 == 0) {
                        container.setGravity(Gravity.START);
                    }
                }
            }

            container.setOnClickListener(null);
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


