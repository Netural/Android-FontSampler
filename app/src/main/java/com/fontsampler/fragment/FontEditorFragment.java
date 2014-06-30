package com.fontsampler.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fontsampler.R;
import com.fontsampler.listener.OnTaskCompled;
import com.fontsampler.manager.FontReaderTask;
import com.fontsampler.util.CommonUtils;

/**
 * Created by sebastian on 26.06.14.
 */
public class FontEditorFragment extends Fragment implements OnTaskCompled {

    private static final String TAG = FontEditorFragment.class.getSimpleName();

    private Spinner availableFontsSpinner;
    private ArrayAdapter<String> availableFontsAdapter;
    private EditText sizePicker;
    private EditText colorPicker;
    private EditText inputText;


    private TextView myTextView;
    private TextView infoTextView;


    private boolean isSizeInPixel;


    public FontEditorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // get views
        myTextView = (TextView) rootView.findViewById(R.id.sampleText);
        infoTextView = (TextView) rootView.findViewById(R.id.info);
        inputText = (EditText) rootView.findViewById(R.id.sampleInput);
        sizePicker = (EditText) rootView.findViewById(R.id.sizeBox);
        colorPicker = (EditText) rootView.findViewById(R.id.colorBox);
        availableFontsSpinner = (Spinner) rootView.findViewById(R.id.spinner);


        return rootView;
    }


    public void resetText() {
        myTextView.setText(getString(R.string.hello_world));
    }

    public void setSizeMetric(boolean setSizeToPixel) {
        isSizeInPixel = setSizeToPixel;
        //change hint text
        if (isSizeInPixel) {
            sizePicker.setHint(R.string.hint_size);
        } else {
            sizePicker.setHint(R.string.hint_size_sp);
        }
        if (sizePicker.getText().length() > 0) {
            changeTextSize();
        }

    }


    private void setListeners() {

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                myTextView.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        sizePicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (sizePicker.getText().length() > 0) {
                    //calc sp of input pixel font size
                    if (isSizeInPixel) {
                        changeTextSize();
                    } else {
//hackel set
                        infoTextView.setText("");
                        int spSize = Integer.parseInt(sizePicker.getText().toString());
                        myTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);

                    }

                }
            }
        });


        colorPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (colorPicker.getText().length() > 5) {
                    //TODO: make it better fast
                    myTextView.setTextColor(Color.parseColor("#" + colorPicker.getText()));

                } else {
                    //default color value (reset if colorcode is changed)
                    myTextView.setTextColor(Color.BLACK);
                }
            }
        });


        availableFontsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String value = availableFontsAdapter.getItem(i);
                if (value != null && !value.isEmpty()) {

                    Typeface myTypeface = Typeface.createFromFile(getActivity().getExternalCacheDir() + "/" + value);

                    myTextView.setTypeface(myTypeface);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void changeTextSize() {
        int pixelSize = Integer.parseInt(sizePicker.getText().toString());
        float spSize = CommonUtils.pixelsToSp(getActivity(), pixelSize);
        //set size of font in sp
        myTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
        infoTextView.setText(pixelSize + "px=" + spSize + "sp");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setListeners();
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        //init data

        readFontsfromDir();
        super.onResume();
    }

    public void addAdditionalText() {
        if (myTextView != null) {
            myTextView.setText(myTextView.getText() + getString(R.string.sample_text));
        }
    }

    public void readFontsfromDir() {
        Log.i(TAG, "read Fonts from Cache-Directory");
        new FontReaderTask(this).execute(getActivity().getExternalCacheDir().getAbsolutePath());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSucess(String[] result) {

        availableFontsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, result);
        availableFontsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availableFontsSpinner.setAdapter(availableFontsAdapter);
    }

    @Override
    public void onError(Exception ex, String message) {
        Log.e(TAG, message);
        ex.printStackTrace();

    }
}


