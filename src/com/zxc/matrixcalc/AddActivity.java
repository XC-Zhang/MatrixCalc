package com.zxc.matrixcalc;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

public class AddActivity extends Activity {
	
	private EditText editTextName, editTextRows, editTextCols;
	private GridView gridView;
	private MatrixAdapter adapter;
	private double[][] entries;
	private int r, c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matrix_input);
		// editTextName
		editTextName = (EditText) findViewById(R.id.editTextName);
		// editTextRows
		editTextRows = (EditText) findViewById(R.id.editTextRows);
		editTextRows.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean focused) {
				if (!focused)
					matrixSizeChange();
			}
		});
		// editTextCols
		editTextCols = (EditText) findViewById(R.id.editTextCols);
		editTextCols.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean focused) {
				if (!focused)
					matrixSizeChange();
			}
		});
		// adapter
		adapter = new MatrixAdapter(this, null);
		// gridView
		gridView = (GridView) findViewById(R.id.gridView1);
		gridView.setAdapter(adapter);
	}
	
	@SuppressWarnings("unchecked")
	public void buttonConfirm_OnClick(View v) {
		String name = editTextName.getText().toString().trim();
		// Check name and size
		if (name.isEmpty())
			return;
		if (!name.matches("^[A-Za-z]+$"))
			return;
		if (r == 0 || c == 0)
			return;
		// Get list of names
		Intent intent = getIntent();
		ArrayList<String> mats = (ArrayList<String>) intent.getSerializableExtra("namedMats");
		if (mats.contains(name))
			return;
		// Retrieve data
		int count = gridView.getChildCount();
		for (int i = 0; i < count; i++) {
			EditText view = (EditText) gridView.getChildAt(i);
			int pos = Integer.parseInt(view.getTag().toString());
			entries[pos / c][pos % c] = Double.parseDouble(view.getText().toString());
		}
		// Put data into intent and return
		intent.putExtra("name", name);
		intent.putExtra("matrix", new Matrix(entries));
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void buttonCancel_OnClick(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	private void matrixSizeChange()
	{
		// Check number of rows and number of columns
		try {
			r = Integer.parseInt(editTextRows.getText().toString());
			c = Integer.parseInt(editTextCols.getText().toString());
		} catch (NumberFormatException ex) { 
			return;
		}
		if (r == 0 || c == 0)
			return;
		entries = changeSize(r, c, entries);
		// Change layout of gridView
		gridView.setNumColumns(c);
		gridView.setLayoutParams(new LinearLayout.LayoutParams(100 * c, LinearLayout.LayoutParams.MATCH_PARENT));
		adapter.setEntries(entries);
		adapter.notifyDataSetChanged();
		return;
	}
	
	private double[][] changeSize(int r, int c, double[][] source) {
		if (source == null)
			return new double[r][c];
		int rows = source.length;
		int cols = source[0].length;
		if (rows == r && cols == c)
			return source;
		int minr = Math.min(rows, r);
		int minc = Math.min(cols, c);
		double[][] temp = new double[r][c];
		for (int i = 0; i < minr; i++)
			for (int j = 0; j < minc; j++)
				temp[i][j] = source[i][j];
		return temp;
	}
}