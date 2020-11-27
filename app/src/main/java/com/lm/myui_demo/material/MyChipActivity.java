package com.lm.myui_demo.material;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.textfield.TextInputEditText;
import com.lm.myui_demo.R;
import java.util.HashMap;
import java.util.Map;

public class MyChipActivity extends AppCompatActivity {
    private TextInputEditText editText;
    private Chip chipTom, chipMike, chipLily;
    private Map<String, ImageSpan> spanMap = new HashMap<>();
    private boolean skip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chip);
        editText = findViewById(R.id.edit_text);

        editText.setFilters(new InputFilter[]{new NameFilter(this)});

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    skip = true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CharSequence ds = s.subSequence(start, start + count);
                String dstr = ds.toString();
                if (after == 0 && ("Tom".equals(dstr) || "Mike".equals(dstr) || "Lily".equals(dstr))) {
                    unCheckChip(dstr);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        chipTom = findViewById(R.id.chip_tom);
        chipMike = findViewById(R.id.chip_mike);
        chipLily = findViewById(R.id.chip_lily);
        setListener(chipTom, "Tom");
        setListener(chipMike, "Mike");
        setListener(chipLily, "Lily");
    }

    private void unCheckChip(String str) {
        if ("Tom".equals(str)) {
            chipTom.setChecked(false);
        } else if ("Mike".equals(str)) {
            chipMike.setChecked(false);
        } else if ("Lily".equals(str)) {
            chipLily.setChecked(false);
        }
    }

    private void setListener(Chip chip, final String str) {
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip=false;
            }
        });
        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText.append(str);
                } else {
                    ImageSpan imageSpan = spanMap.get(str);
                    if (imageSpan != null && !skip) {
                        int start = editText.getText().getSpanStart(imageSpan);
                        editText.getText().removeSpan(spanMap.get(str));
                        spanMap.remove(str);
                        editText.getText().delete(start, start + imageSpan.getSource().length());
                    }
                }
            }
        });
    }

    class NameFilter implements InputFilter {
        private Context context;

        public NameFilter(Context context) {
            this.context = context;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if ("Tom".equals(source.toString()) || "Mike".equals(source.toString()) || "Lily".equals(source.toString())) {
                ChipDrawable drawable = ChipDrawable.createFromResource(context, R.xml.chip);
                drawable.setText(source.toString());
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                SpannableString sp = SpannableString.valueOf(source);
                ImageSpan imageSpan = new ImageSpan(drawable, source.toString());
                spanMap.put(source.toString(), imageSpan);
                sp.setSpan(imageSpan, 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return sp;
            }
            return source;
        }
    }
}
