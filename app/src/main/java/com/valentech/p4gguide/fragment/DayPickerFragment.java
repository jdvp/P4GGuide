package com.valentech.p4gguide.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.valentech.p4gguide.R;
import com.valentech.p4gguide.model.calendar.Day;
import com.valentech.p4gguide.model.calendar.Month;

/**
 * A fragment that will allow users to pick a certain day to view a walkthrough page for
 *
 * Created by JD on 12/18/2016.
 */

public class DayPickerFragment extends Fragment {

    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View monthView = inflater.inflate(R.layout.month_picker, container, false);
        fragmentManager = getFragmentManager();

        Month month = (Month) getArguments().get("month");
        LinearLayout dayContainer = (LinearLayout) monthView.findViewById(R.id.month_container);
        if(month != null) {
            for (int i = month.getStart(); i <= month.getEnd(); i++) {

                final Button dayButton = new Button(getActivity());
                dayButton.setText(month.toString() + " " + i);
                dayButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                dayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment;
                        fragment = fragmentManager.findFragmentByTag("walkthrough fragment day picker "+ dayButton.getText());
                        if(fragment == null) {
                            fragment = new DayFragment();
                        }

                        Bundle args = new Bundle();
                        args.putString("date", Day.fromString(dayButton.getText().toString()).toString());
                        fragment.setArguments(args);

                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "walkthrough fragment day "+dayButton.getText()).
                                addToBackStack("walkthrough day day").commit();
                    }
                });
                dayContainer.addView(dayButton);
            }
        }
        return monthView;
    }

}
