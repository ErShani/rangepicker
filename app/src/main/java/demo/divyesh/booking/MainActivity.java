package demo.divyesh.booking;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import com.andexert.calendarlistview.library.DayPickerView;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    com.squareup.timessquare.CalendarPickerView calendarPickerView;
    DayPickerView dayPickerView;
    CalendarView calendarView;

    Date checkInDate = null;
    Date checkOutDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("CHECK IN"));
        tabLayout.addTab(tabLayout.newTab().setText("CHECK OUT"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 1) {
                    Log.e("TabSelectedPosition 1", tab.getText().toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        initCalendar();

    }

    public void reCreateCalendar(List<Date> selectedDates) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        if (selectedDates == null) {
            calendarPickerView.init(new Date(), calendar.getTime()) //
                    .inMode(com.squareup.timessquare.CalendarPickerView.SelectionMode.RANGE);
        } else {

            calendarPickerView.init(selectedDates.get(0), calendar.getTime()) //
                    .inMode(com.squareup.timessquare.CalendarPickerView.SelectionMode.RANGE)
                    .withSelectedDates(selectedDates);
        }
    }

    public void initCalendar() {

        calendarPickerView = findViewById(R.id.calendarView);

        final List<Date> disabledDates = new ArrayList<>();
        disabledDates.add(new Date(2017, 12, 30));
        calendarPickerView.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendarPickerView.setDateSelectableFilter(new CalendarPickerView.DateSelectableFilter() {
            @Override
            public boolean isDateSelectable(Date date) {
                boolean isAllow = true;
                if (date.getDay() == new Date().getDay()+1) {
                    isAllow = false;
                }
                return isAllow;
            }
        });

        reCreateCalendar(null);

        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {

                Calendar calendar = Calendar.getInstance();

                if (checkInDate == null) {
                    checkInDate = date;
                    calendar.add(Calendar.DAY_OF_MONTH, 2);
                    checkOutDate = calendar.getTime();

                } else {
                    if (isValidRange(checkInDate, date)) {
                        checkOutDate = date;
                    } else {
                        calendar.setTime(checkInDate);
                        calendar.add(Calendar.DAY_OF_MONTH, 2);
                        checkOutDate = calendar.getTime();
                        Toast.makeText(MainActivity.this, "You can not select more then 4 days", Toast.LENGTH_LONG).show();
                    }
                }

                reCreateCalendar(getSelectedDates(checkInDate, checkOutDate));

                Log.e("selected dates", checkInDate + "\n" + checkOutDate);
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

    }

    public boolean isValidRange(Date checkIn, Date checkOut) {

        Calendar checkOutDay = Calendar.getInstance();
        checkOutDay.setTime(checkOut);

        Calendar checkInDay = Calendar.getInstance();
        checkInDay.setTime(checkIn);

        long msDiff = checkOutDay.getTimeInMillis() - checkInDay.getTimeInMillis();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        if (daysDiff > 4) {
            return false;
        } else {
            return true;
        }
    }

    public List<Date> getSelectedDates(Date checkIn, Date checkOut) {
        List<Date> selectedDates = new ArrayList<>();
        selectedDates.add(checkIn);
        selectedDates.add(checkOut);

        return selectedDates;
    }

}
