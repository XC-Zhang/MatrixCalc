package com.zxc.matrixcalc;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;

public class InputAdapter extends BaseAdapter {

	private Context mContext;
	private double[][] mEntries;
	
	public InputAdapter(Context c, double[][] e) {
		mContext = c;
		mEntries = e;
	}
	
	public void setEntries(double[][] e) {
		mEntries = e;
	}
	
	@Override
	public int getCount() {
		if (mEntries == null)
			return 0;
		return mEntries.length * mEntries[0].length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (mEntries == null)
			return 0;
		return position / mEntries[0].length;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mEntries == null)
			return null;
		EditText edittext;
		if (convertView == null) {
			edittext = new EditText(mContext);
			edittext.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
			edittext.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			edittext.setSingleLine(true);			
			edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean focused) {
					if (focused)
						return;
					EditText edittext = (EditText) view;
					int pos = (Integer) edittext.getTag();
					mEntries[pos / mEntries[0].length][pos % mEntries[0].length] = Double.parseDouble(edittext.getText().toString());
				}
			});
		} else {
			edittext = (EditText) convertView;
		}
		edittext.setText(String.valueOf(mEntries[position / mEntries[0].length][position % mEntries[0].length]));
		edittext.setTag(position);
		return edittext;
	}
}
