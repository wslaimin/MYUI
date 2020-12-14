package com.lm.myui_demo.material;

import android.os.Bundle;
import android.os.Parcel;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.lm.myui_demo.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MyDatePickerActivity extends AppCompatActivity {
    private Spinner boundStartYearSpinner, boundStartMonthSpinner, boundStartDaySpinner;
    private Spinner boundEndYearSpinner, boundEndMonthSpinner, boundEndDaySpinner;
    private Spinner selectionYearSpinner, selectionMonthSpinner, selectionDaySpinner;
    private Spinner selectionRangeYearSpinner, selectionRangeMonthSpinner, selectionRangeDaySpinner;
    private Spinner openYearSpinner, openMonthSpinner, openDaySpinner;
    private String title;
    private int mode;
    private int theme;
    private CalendarConstraints.DateValidator validator;
    private Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_date_picker);
        mode = 0;
        theme = resolveAttr(R.attr.materialCalendarTheme);
        calendar = Calendar.getInstance(TimeZone.getDefault());

        TextInputEditText editText = findViewById(R.id.title);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                title = s.toString();
            }
        });

        RadioGroup modeGroup = findViewById(R.id.mode_group);
        RadioGroup themeGroup = findViewById(R.id.theme_group);
        RadioGroup validGroup = findViewById(R.id.valid_group);

        modeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.mode_single) {
                    mode = 0;
                    showSelectionRange(false);
                } else {
                    mode = 1;
                    showSelectionRange(true);
                }
            }
        });

        themeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.theme_dialog) {
                    theme = resolveAttr(R.attr.materialCalendarTheme);
                } else {
                    theme = resolveAttr(R.attr.materialCalendarFullscreenTheme);
                }
            }
        });

        validGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.valid_all) {
                    validator = null;
                } else {
                    validator = new DateValidatorWeekdays();
                }
            }
        });

        boundStartYearSpinner = findViewById(R.id.bound_start_year);
        boundStartMonthSpinner = findViewById(R.id.bound_start_month);
        boundStartDaySpinner = findViewById(R.id.bound_start_day);
        initDateSpinner(boundStartYearSpinner, boundStartMonthSpinner, boundStartDaySpinner);

        boundEndYearSpinner = findViewById(R.id.bound_end_year);
        boundEndMonthSpinner = findViewById(R.id.bound_end_month);
        boundEndDaySpinner = findViewById(R.id.bound_end_day);
        initDateSpinner(boundEndYearSpinner, boundEndMonthSpinner, boundEndDaySpinner);

        selectionYearSpinner = findViewById(R.id.selection_year);
        selectionMonthSpinner = findViewById(R.id.selection_month);
        selectionDaySpinner = findViewById(R.id.selection_day);
        initDateSpinner(selectionYearSpinner, selectionMonthSpinner, selectionDaySpinner);

        selectionRangeYearSpinner = findViewById(R.id.selection_range_year);
        selectionRangeMonthSpinner = findViewById(R.id.selection_range_month);
        selectionRangeDaySpinner = findViewById(R.id.selection_range_day);
        initDateSpinner(selectionRangeYearSpinner, selectionRangeMonthSpinner, selectionRangeDaySpinner);

        openYearSpinner = findViewById(R.id.open_year);
        openMonthSpinner = findViewById(R.id.open_month);
        openDaySpinner = findViewById(R.id.open_day);
        initDateSpinner(openYearSpinner, openMonthSpinner, openDaySpinner);

        showSelectionRange(false);
    }

    public void onClick(View view) {
        showPicker();
    }

    private void initDateSpinner(final Spinner yearSpinner, final Spinner monthSpinner, final Spinner daySpinner) {
        final Integer[] years = new Integer[100];
        for (int i = 0; i < years.length; i++) {
            years[i] = 2000 + i;
        }
        yearSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years));

        Integer[] months = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        monthSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months));

        final List<Integer> days = new ArrayList<>();
        int year = (Integer) yearSpinner.getSelectedItem();
        int month = (Integer) monthSpinner.getSelectedItem();
        int dayRange = getDayRange(year, month);
        for (int i = 0; i < dayRange; i++) {
            days.add(i + 1);
        }

        daySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days));

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (monthSpinner.getSelectedItemPosition() == 1) {
                    int day = (Integer) daySpinner.getSelectedItem();
                    int year = (Integer) yearSpinner.getItemAtPosition(position);
                    int maxDay;
                    if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                        maxDay = 29;
                    } else {
                        maxDay = 28;
                    }
                    modifyDays((ArrayAdapter<Integer>) daySpinner.getAdapter(), maxDay - daySpinner.getAdapter().getCount());
                    if (day > maxDay) {
                        daySpinner.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int day = (Integer) daySpinner.getSelectedItem();
                int maxDay = getDayRange((Integer) yearSpinner.getSelectedItem(), (Integer) monthSpinner.getItemAtPosition(position));
                modifyDays((ArrayAdapter<Integer>) daySpinner.getAdapter(), maxDay - daySpinner.getAdapter().getCount());
                if (day > maxDay) {
                    daySpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void modifyDays(ArrayAdapter<Integer> adapter, int delta) {
        int day = adapter.getItem(adapter.getCount() - 1);
        if (delta > 0) {
            for (int i = 1; i <= delta; i++) {
                adapter.add(day + i);
            }
        } else if (delta < 0) {
            for (int i = 0; i < Math.abs(delta); i++) {
                adapter.remove(adapter.getItem(adapter.getCount() - 1));
            }
        }
    }

    private int getDayRange(int year, int month) {
        int range;
        if (month == 2) {
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                range = 29;
            } else {
                range = 28;
            }
        } else {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    range = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    range = 30;
                    break;
                default:
                    range = 0;
                    break;

            }
        }
        return range;
    }

    private void showPicker() {
        int year = (Integer) boundStartYearSpinner.getSelectedItem();
        int month = (Integer) boundStartMonthSpinner.getSelectedItem();
        int day = (Integer) boundStartDaySpinner.getSelectedItem();
        calendar.set(year, month - 1, day);
        long boundStart = calendar.getTimeInMillis();

        year = (Integer) boundEndYearSpinner.getSelectedItem();
        month = (Integer) boundEndMonthSpinner.getSelectedItem();
        day = (Integer) boundEndDaySpinner.getSelectedItem();
        calendar.set(year, month - 1, day);
        long boundEnd = calendar.getTimeInMillis();

        year = (Integer) selectionYearSpinner.getSelectedItem();
        month = (Integer) selectionMonthSpinner.getSelectedItem();
        day = (Integer) selectionDaySpinner.getSelectedItem();
        calendar.set(year, month - 1, day);
        long selection = calendar.getTimeInMillis();

        year = (Integer) openYearSpinner.getSelectedItem();
        month = (Integer) openMonthSpinner.getSelectedItem();
        day = (Integer) openDaySpinner.getSelectedItem();
        calendar.set(year, month - 1, day);
        long openAt = calendar.getTimeInMillis();

        MaterialDatePicker.Builder<?> builder;
        if (mode == 0) {
            MaterialDatePicker.Builder<Long> singleBuilder = MaterialDatePicker.Builder.datePicker();
            singleBuilder.setSelection(selection);
            builder = singleBuilder;
        } else {
            MaterialDatePicker.Builder<Pair<Long, Long>> rangeBuilder = MaterialDatePicker.Builder.dateRangePicker();

            year = (Integer) selectionRangeYearSpinner.getSelectedItem();
            month = (Integer) selectionRangeMonthSpinner.getSelectedItem();
            day = (Integer) selectionRangeDaySpinner.getSelectedItem();
            calendar.set(year, month - 1, day);
            long selectionRange = calendar.getTimeInMillis();
            rangeBuilder.setSelection(new Pair<>(selection, selectionRange));
            builder = rangeBuilder;
        }
        builder.setTitleText(title);
        builder.setTheme(theme);
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setStart(boundStart);
        constraintBuilder.setEnd(boundEnd);
        constraintBuilder.setOpenAt(openAt);
        if (validator != null) {
            constraintBuilder.setValidator(validator);
        }
        MaterialDatePicker<?> picker = builder.setCalendarConstraints(constraintBuilder.build()).build();
        picker.show(getSupportFragmentManager(), picker.toString());
    }

    private int resolveAttr(int attr) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    private void showSelectionRange(boolean bool) {
        selectionRangeYearSpinner.setVisibility(bool ? View.VISIBLE : View.GONE);
        selectionRangeMonthSpinner.setVisibility(bool ? View.VISIBLE : View.GONE);
        selectionRangeDaySpinner.setVisibility(bool ? View.VISIBLE : View.GONE);
    }

    static class DateValidatorWeekdays implements CalendarConstraints.DateValidator {

        DateValidatorWeekdays() {
        }

        private Calendar utc = Calendar.getInstance(TimeZone.getDefault());

        public static final Creator<DateValidatorWeekdays> CREATOR =
                new Creator<DateValidatorWeekdays>() {
                    @Override
                    public DateValidatorWeekdays createFromParcel(Parcel source) {
                        return new DateValidatorWeekdays();
                    }

                    @Override
                    public DateValidatorWeekdays[] newArray(int size) {
                        return new DateValidatorWeekdays[size];
                    }
                };

        @Override
        public boolean isValid(long date) {
            utc.setTimeInMillis(date);
            int dayOfWeek = utc.get(Calendar.DAY_OF_WEEK);
            return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof DateValidatorWeekdays)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            Object[] hashedFields = {};
            return Arrays.hashCode(hashedFields);
        }
    }
}
