package com.zxc.matrixcalc;

import java.io.Serializable;

public class Matrix implements Serializable {

	private static final long serialVersionUID = 2L;

	private double[][] _e;

	public Matrix(int rows, int cols) {
		_e = new double[rows][cols];
	}
	
	public Matrix(double[][] entries) {
		if (entries == null)
			throw new NullPointerException();
		int l = 0;
		for (int i = 0; i < entries.length; i++) {
			if (entries[i] == null)
				throw new NullPointerException();
			if (i == 0)
				l = entries[0].length;
			else if (l != entries[i].length)
				throw new IllegalArgumentException();
		}
		_e = entries;
	}
	
	public int getRowCount() {
		return _e.length;
	}
	
	public int getColCount() {
		return _e[0].length;
	}
	
	public double getValue(int r, int c) {
		return _e[r][c];
	}
	
	public void setValue(int r, int c, double value) {
		_e[r][c] = value;
	}
	
	public double[][] getEntries() {
		return _e.clone();
	}
	
	public static Matrix Add(Matrix A, Matrix B) {
		if (A == null || B == null)
			throw new NullPointerException();
		int r = A.getRowCount();
		int c = A.getColCount();
		if (r != B.getRowCount() || c != B.getColCount())
			throw new IllegalArgumentException();
		Matrix ret = new Matrix(r, c);
		for (int i = 0; i < r; i++)
			for (int j = 0; j < c; j++)
				ret._e[i][j] = A._e[i][j] + B._e[i][j];
		return ret;
	}
	
	public static Matrix Minus(Matrix A, Matrix B) {
		if (A == null || B == null)
			throw new NullPointerException();
		int r = A.getRowCount();
		int c = A.getColCount();
		if (r != B.getRowCount() || c != B.getColCount())
			throw new IllegalArgumentException();
		Matrix ret = new Matrix(r, c);
		for (int i = 0; i < r; i++)
			for (int j = 0; j < c; j++)
				ret._e[i][j] = A._e[i][j] - B._e[i][j];
		return ret;
	}
	
	public static Matrix Multi(Matrix A, Matrix B) {
		if (A == null || B == null)
			throw new NullPointerException();
		int t = A.getColCount();
		if (t != B.getRowCount())
			throw new IllegalArgumentException();
		int r = A.getRowCount();
		int c = B.getColCount();
		Matrix ret = new Matrix(r, c);
		for (int i = 0; i < r; i++)
			for (int j = 0; j < c; j++) {
				ret._e[i][j] = 0;
				for (int k = 0; k < t; k++)
					ret._e[i][j] += A._e[i][k] * B._e[k][j];
			}
		return ret;
	}
	
	public static Matrix Transpose(Matrix A) {
		if (A == null)
			throw new NullPointerException();
		int r = A.getRowCount();
		int c = A.getColCount();
		Matrix ret = new Matrix(c, r);
		for (int i = 0; i < r; i++)
			for (int j = 0; j < c; j++)
				ret._e[j][i] = A._e[i][j];
		return ret;
	}
	
	public double[] toArray() {
		int r = _e.length;
		int c = _e[0].length;
		double[] ret = new double[r * c];
		for (int i = 0, k = 0; i < r; i++)
			for (int j = 0; j < c; j++, k++)
				ret[k] = _e[i][j];
		return ret;
	}
}
