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
import com.valentech.p4gguide.model.calendar.Month;
import com.valentech.p4gguide.util.ResourceUtility;

/**
 * A fragment that will allow users to pick a certain day to view a walkthrough page for
 *
 * Created by JD on 12/18/2016.
 */

public class MonthPickerFragment extends Fragment {

    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View monthView = inflater.inflate(R.layout.month_picker, container, false);
        fragmentManager = getFragmentManager();

        LinearLayout months = (LinearLayout) monthView.findViewById(R.id.month_container);
        for(final Month month : Month.values()) {
            Button monthButton = new Button(getActivity());
            monthButton.setText(month.toString());
            monthButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            monthButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment;
                    fragment = fragmentManager.findFragmentByTag("walkthrough fragment day picker "+month.toString());
                    if(fragment == null) {
                        fragment = new DayPickerFragment();
                    }

                    Bundle args = new Bundle();
                    args.putSerializable("month", month);
                    fragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "walkthrough fragment day picker "+month.toString()).
                            addToBackStack("walkthrough day picker").commit();
                }
            });
            months.addView(monthButton);
        }
        return monthView;
    }

}
