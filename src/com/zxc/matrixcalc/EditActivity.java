package com.zxc.matrixcalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

public class EditActivity extends Activity {
	
	private EditText edRows, edCols, edName;
	private GridView grid;
	private MatrixAdapter adapter;
	private Matrix mat;
	private double[][] entries;
	private int r, c;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matrix_input);
		
		mat = (Matrix) getIntent().getSerializableExtra("matrix");
		entries = mat.getEntries();
		
		edName = (EditText) findViewById(R.id.editTextName);
		edName.setEnabled(false);
		edName.setText(getIntent().getStringExtra("name"));
		grid = (GridView) findViewById(R.id.gridView1);
		adapter = new MatrixAdapter(this, new double[][] {{ 0 }});
		grid.setAdapter(adapter);
		edRows = (EditText) findViewById(R.id.editTextRows);
		edCols = (EditText) findViewById(R.id.editTextCols);
		edRows.setText(String.valueOf(mat.getRowCount()));
		edCols.setText(String.valueOf(mat.getColCount()));
		edRows.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean focused) {
				if (!focused)
					matrixSizeChanged();
			}
		});
		edCols.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean focused) {
				if (!focused)
					matrixSizeChanged();
			}
		});
		matrixSizeChanged();
	}
	
	public void buttonConfirm_OnClick(View view) {
		Intent intent = getIntent();
		if (r == 0 || c == 0)
			return;
		// Retrieve data
		int count = grid.getChildCount();
		for (int i = 0; i < count; i++) {
			EditText v = (EditText) grid.getChildAt(i);
			int pos = Integer.parseInt(v.getTag().toString());
			entries[pos / c][pos % c] = Double.parseDouble(v.getText().toString());
		}
		// Put data into intent and return
		Matrix m = new Matrix(entries);
		intent.putExtra("name", edName.getText().toString());
		intent.putExtra("matrix", m);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void buttonCancel_OnClick(View view) {
		setResult(RESULT_CANCELED);
		finish();
	}

	private void matrixSizeChanged()
	{
		// Check number of rows and number of columns
		try {
			r = Integer.parseInt(edRows.getText().toString());
			c = Integer.parseInt(edCols.getText().toString());
		} catch (NumberFormatException ex) { 
			return;
		}
		if (r == 0 || c == 0)
			return;
		entries = changeSize(r, c, entries);
		// Change layout of gridView
		grid.setNumColumns(c);
		grid.setLayoutParams(new LinearLayout.LayoutParams(100 * c, LinearLayout.LayoutParams.MATCH_PARENT));
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
