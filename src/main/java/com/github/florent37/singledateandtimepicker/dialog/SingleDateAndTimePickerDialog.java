package com.github.florent37.singledateandtimepicker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.FontUtils;
import com.github.florent37.singledateandtimepicker.R;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.widget.WheelMinutePicker;

import java.util.Calendar;
import java.util.Date;

public class SingleDateAndTimePickerDialog extends BaseDialog {

    Dialog pickerDialog;
    Context context;
    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;
    private SingleDateAndTimePicker picker;
    @Nullable
    private String title;

    private SingleDateAndTimePickerDialog(Context context) {
        this(context, false, "");
    }

    private SingleDateAndTimePickerDialog(Context context, boolean bottomSheet, String title) {
        final int layout = bottomSheet ? R.layout.bottom_sheet_picker_bottom_sheet :
                R.layout.bottom_sheet_picker;
        this.bottomSheetHelper = new BottomSheetHelper(context, layout);
        this.context = context;
        this.bottomSheetHelper.setListener(new BottomSheetHelper.Listener() {
            @Override
            public void onOpen() {
            }

            @Override
            public void onLoaded(View view) {

            }

            @Override
            public void onClose() {
                SingleDateAndTimePickerDialog.this.onClose();
            }
        });
    }


    private void init(Context context, String title) {
        pickerDialog = new Dialog(context, R.style.DialogTheme);
        pickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pickerDialog.getWindow().getAttributes().windowAnimations = R.style.Animations_LoadingDialogBottom;
        pickerDialog.setContentView(R.layout.bottom_sheet_picker_bottom_sheet);

        WindowManager.LayoutParams layoutParams = pickerDialog.getWindow().getAttributes();
        pickerDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.dimAmount = 0.6f;
        pickerDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        pickerDialog.getWindow().setAttributes(layoutParams);
        pickerDialog.setCancelable(true);
        pickerDialog.setCanceledOnTouchOutside(true);

        picker = (SingleDateAndTimePicker) pickerDialog.findViewById(R.id.picker);
        picker.setIsAmPm(true);

        final Button buttonOk = (Button) pickerDialog.findViewById(R.id.buttonOk);
        buttonOk.setTypeface(FontUtils.getFontBold(context));
        if (buttonOk != null) {
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    okClicked = true;
                    close();
                }
            });

            if (mainColor != null) {
                buttonOk.setTextColor(mainColor);
            }
        }

        TextView titleTextView = (TextView) pickerDialog.findViewById(R.id.titleView);
        titleTextView.setTypeface(FontUtils.getFontBold(context));
        if (titleTextView != null) {
            titleTextView.setText(title);
        }

        final View sheetContentLayout = pickerDialog.findViewById(R.id.sheetContentLayout);
        if (sheetContentLayout != null) {
            sheetContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            if (backgroundColor != null) {
                sheetContentLayout.setBackgroundColor(backgroundColor);
            }
        }


        final View pickerTitleHeader = pickerDialog.findViewById(R.id.pickerTitleHeader);
        if (mainColor != null && pickerTitleHeader != null) {
            pickerTitleHeader.setBackgroundColor(mainColor);
        }

        if (curved) {
            picker.setCurved(true);
            picker.setVisibleItemCount(3);
        } else {
            picker.setCurved(false);
            picker.setVisibleItemCount(3);
        }
        picker.setMustBeOnFuture(mustBeOnFuture);

        picker.setStepMinutes(minutesStep);

        if (mainColor != null) {
            picker.setSelectedTextColor(mainColor);
        }

        if (minDate != null) {
            picker.setMinDate(minDate);
        }else {
            picker.setMinDate(Calendar.getInstance().getTime());
        }

        if (maxDate != null) {
            picker.setMaxDate(maxDate);
        }

        if (defaultDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(defaultDate);
            picker.selectDate(calendar);
        }else {
            picker.selectDate(Calendar.getInstance());
        }
    }

    public SingleDateAndTimePickerDialog setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public SingleDateAndTimePickerDialog setCurved(boolean curved) {
        this.curved = curved;
        return this;
    }

    public SingleDateAndTimePickerDialog setMinutesStep(int minutesStep) {
        this.minutesStep = minutesStep;
        return this;
    }

    public SingleDateAndTimePickerDialog setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public SingleDateAndTimePickerDialog setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        return this;
    }

    public SingleDateAndTimePickerDialog setMinDateRange(Date minDate) {
        this.minDate = minDate;
        return this;
    }

    public SingleDateAndTimePickerDialog setMaxDateRange(Date maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public SingleDateAndTimePickerDialog setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
        return this;
    }

    @Override
    public void display() {
        init(context, title);
        pickerDialog.show();
//        super.display();
//        bottomSheetHelper.display();
    }

    @Override
    public void close() {
        pickerDialog.dismiss();

        if (listener != null && okClicked) {
            listener.onDateSelected(picker.getDate());
        }
    }

    public interface Listener {
        void onDateSelected(Date date);
    }

    public static class Builder {
        private final Context context;
        private SingleDateAndTimePickerDialog dialog;

        @Nullable
        private Listener listener;

        private String title;

        private boolean bottomSheet;

        private boolean curved;
        private boolean mustBeOnFuture;
        private int minutesStep = WheelMinutePicker.STEP_MINUTES_DEFAULT;

        @ColorInt
        @Nullable
        private Integer backgroundColor = null;

        @ColorInt
        @Nullable
        private Integer mainColor = null;

        @ColorInt
        @Nullable
        private Integer titleTextColor = null;

        @Nullable
        private Date minDate;
        @Nullable
        private Date maxDate;
        @Nullable
        private Date defaultDate;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder title() {
            return this;
        }

        public Builder bottomSheet() {
            this.bottomSheet = true;
            return this;
        }

        public Builder curved() {
            this.curved = true;
            return this;
        }

        public Builder mustBeOnFuture() {
            this.mustBeOnFuture = true;
            return this;
        }

        public Builder minutesStep(int minutesStep) {
            this.minutesStep = minutesStep;
            return this;
        }

        public Builder listener(@Nullable Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder titleTextColor(@NonNull @ColorInt int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder backgroundColor(@NonNull @ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder mainColor(@NonNull @ColorInt int mainColor) {
            this.mainColor = mainColor;
            return this;
        }

        public Builder minDateRange(Date minDate) {
            this.minDate = minDate;
            return this;
        }

        public Builder maxDateRange(Date maxDate) {
            this.maxDate = maxDate;
            return this;
        }

        public Builder defaultDate(Date defaultDate) {
            this.defaultDate = defaultDate;
            return this;
        }


        public SingleDateAndTimePickerDialog build(String title) {
            final SingleDateAndTimePickerDialog dialog = new SingleDateAndTimePickerDialog(context, bottomSheet, title)
                    .setTitle(title)
                    .setListener(listener)
                    .setCurved(curved)
                    .setMinutesStep(minutesStep)
                    .setMaxDateRange(maxDate)
                    .setMinDateRange(minDate)
                    .setDefaultDate(defaultDate)
                    .setMustBeOnFuture(mustBeOnFuture);

            if (mainColor != null) {
                dialog.setMainColor(mainColor);
            }

            if (backgroundColor != null) {
                dialog.setBackgroundColor(backgroundColor);
            }

            if (titleTextColor != null) {
                dialog.setTitleTextColor(titleTextColor);
            }


            return dialog;
        }

        public void display() {
            dialog = build("");
            dialog.display();
        }

        public void close() {
            if (dialog != null) {
                dialog.close();
            }
        }
    }
}
