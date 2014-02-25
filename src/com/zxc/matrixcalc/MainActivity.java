package com.zxc.matrixcalc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int PICK_MATRIX = 1;
	
	private Button buttonAns;
	private TextView textViewExpression;
	private LinearLayout linearLayoutMats;
	private GridView gridViewMats;
	private ArrayAdapter<Double> adapter;
	private ArrayList<String> namedMats;
	private LinkedList<Button> expression;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Instantiate variables
		namedMats = new ArrayList<String>();
		expression = new LinkedList<Button>();
		// Retrieve views
		textViewExpression = (TextView) findViewById(R.id.textViewExpression);
		linearLayoutMats = (LinearLayout) findViewById(R.id.linearLayoutMats);
		// Add preserved matrix ANSWER
		namedMats.add("ANS");
		buttonAns = (Button) findViewById(R.id.buttonAns);
		buttonAns.setTag(new Matrix(new double[][] { { 0 } }));
		// adapter
		adapter = new ArrayAdapter<Double>(this, R.layout.simple_textview);
		// gridViewMats
		gridViewMats = (GridView) findViewById(R.id.gridView0);
		gridViewMats.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void buttonAddMatrix_OnClick(View v) {
		Intent intent = new Intent();
		intent.setClass(this, AddMatrixActivity.class);
		intent.putExtra("namedMats", namedMats);
		startActivityForResult(intent, PICK_MATRIX);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == PICK_MATRIX) {
			// Retrieve data from intent
			String name = data.getStringExtra("name");
			Matrix mat = (Matrix) data.getSerializableExtra("matrix");
			// Add name
			namedMats.add(name);
			// Add a button
			Button temp = (Button) View.inflate(this, R.layout.simple_buttonbar_button, null);
			temp.setText(name);			
			temp.setTag(mat);
			temp.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					buttonMatrix_OnClick(v);
				}
			});
			linearLayoutMats.addView(temp);
		}
	}

	public void buttonMatrix_OnClick(View v) {
		Button button = (Button) v;
		// Show matrix
		showMatrix((Matrix) button.getTag());		
		// Add to expression
		addToExpression(button);
	}
	
	private void showMatrix(Matrix m) {
		int c = m.getColCount();
		// Change layout
		gridViewMats.setNumColumns(c);
		gridViewMats.setLayoutParams(new LinearLayout.LayoutParams(100 * c, LinearLayout.LayoutParams.MATCH_PARENT));
		// Update adapter
		adapter.clear();
		for (int i = 0; i < m.getRowCount(); i++)
			for (int j = 0; j < c; j++)
				adapter.add(m.getValue(i, j));
		adapter.notifyDataSetChanged();
	}
	
	private void addToExpression(Button b) {
		expression.add(b);
		textViewExpression.append(b.getText().toString());
	}
	
	private void removeLastOfExpression() {
		if (expression.isEmpty())
			return;
		Button b = expression.getLast();
		String s = textViewExpression.getText().toString();
		s = s.substring(0, s.length() - b.getText().length());
		textViewExpression.setText(s);
		expression.removeLast();
	}
	
	public void operators_OnClick(View v) {
		switch (v.getId()) {
		case R.id.buttonAdd :
		case R.id.buttonMinus :
		case R.id.buttonMulti :
		case R.id.buttonTrans :
		case R.id.buttonLBrac :
		case R.id.buttonRBrac :
			addToExpression((Button) v);
			break;
		case R.id.buttonBksp :
			removeLastOfExpression();
			break;
		case R.id.buttonEqual :
			if (expression.isEmpty())
				break;
			Matrix ans = calc();
			buttonAns.setTag(ans);
			showMatrix(ans);
			break;
		default :
			break;
		}
	}
	
	private Matrix calc() {
		@SuppressWarnings("unchecked")
		LinkedList<Button> ex = (LinkedList<Button>) expression.clone();
		
		Button tmp;
		Stack<Matrix> operands;
		Stack<Button> operators;
		operands = new Stack<Matrix>();
		operators = new Stack<Button>();
		while ((tmp = ex.pollFirst()) != null)
			if (tmp.getTag() instanceof Matrix)
				operands.add((Matrix) tmp.getTag());
			else {
				if (operators.isEmpty())
					operators.add(tmp);
				else {
					int prev = Integer.parseInt(operators.peek().getTag().toString());
					int curr = Integer.parseInt(tmp.getTag().toString());
					// Check if the operator is right bracket
					if (curr == 5) {
						operators.add(tmp);
						calcPartial(operands, operators);
						continue;
					} 
					if (!compare(curr, prev))
						calcPartial(operands, operators);
					operators.add(tmp);
				}
			}
		while (!operators.isEmpty())
			calcPartial(operands, operators);
		return operands.pop();
	}
	
	private boolean compare(int a, int b) {
		if (a <= b || b == 1)
			return true;
		else
			return false;
	}
	
	private void calcPartial(Stack<Matrix> operands, Stack<Button> operators) {
		Button tmp = operators.pop();
		Matrix m2, m1;
		switch (tmp.getId()) {
		case R.id.buttonAdd :
			m2 = operands.pop();
			m1 = operands.pop();
			operands.add(Matrix.Add(m1, m2));
			break;
		case R.id.buttonMinus :
			m2 = operands.pop();
			m1 = operands.pop();
			operands.add(Matrix.Minus(m1, m2));
			break;
		case R.id.buttonMulti :
			m2 = operands.pop();
			m1 = operands.pop();
			operands.add(Matrix.Multi(m1, m2));
			break;
		case R.id.buttonTrans :
			m1 = operands.pop();
			operands.add(Matrix.Transpose(m1));
			break;
		case R.id.buttonLBrac :
			break;
		case R.id.buttonRBrac :
			while (Integer.parseInt(operators.peek().getTag().toString()) != 1) {
				calcPartial(operands, operators);
			}
			calcPartial(operands, operators);
			break;
		default :
			break;
		}
	}
}
