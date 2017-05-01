package com.example.attendance.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.attendance.R;

/**
 * Created by 陈振聪 on 2017/4/25.
 */
public class MultipleChoiceListItem extends LinearLayout implements Checkable {
    private CheckBox mCheckBox;
    private TextView mTextView;
    public MultipleChoiceListItem(Context context) {
        super(context);
        initview(context);
        // TODO Auto-generated constructor stub
    }

    public MultipleChoiceListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview(context);
        // TODO Auto-generated constructor stub
    }

    public MultipleChoiceListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initview(context);
        // TODO Auto-generated constructor stub
    }
    private void initview(Context context){
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.grade_list, this,true);
        mCheckBox=(CheckBox) v.findViewById(R.id.grade_select);
        mTextView=(TextView) v.findViewById(R.id.grade);
    }

    public void setText(String text){
        mTextView.setText(text);
    }
    @Override
    public void setChecked(boolean checked) {
        // TODO Auto-generated method stub
        mCheckBox.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        // TODO Auto-generated method stub
        return mCheckBox.isChecked();
    }

    @Override
    public void toggle() {
        // TODO Auto-generated method stub
        mCheckBox.toggle();
    }

}
