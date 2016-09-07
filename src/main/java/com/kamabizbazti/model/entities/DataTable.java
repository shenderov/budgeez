package com.kamabizbazti.model.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DataTable {
	
private List <String> labels;
private List <Object []> rows;

public DataTable() {
	labels = newArrayList();
    rows = newArrayList();
}

public DataTable(List<String> labels, List <Object []> rows) {
	this.labels = labels;
    this.rows = rows;
}

	public DataTable(String [] labels, List <Object []> rows) {
		this.labels = Arrays.asList(labels);
		this.rows = rows;
	}

public void setLabels(String [] labels) {
	setLabels(Arrays.asList(labels));
}

public void setLabels(List <String> labels) {
	this.labels = labels;
}

//GET CHECK
public void addLabel(String label){
	labels.add(label);
}

public void addLabel(String label, int index){
	labels.add(index, label);
}

public List <String> getLabelsAsList(){
	return labels;
}

public String [] getLabelsAsArray(){
	return (String[]) labels.toArray();
}

public void setRows(List <Object []> rows){
	this.rows = rows;
}

public void addRow(Object [] row) {
	rows.add(row);
}

public void addRow(List <Object []> row) {
	rows.add(row.toArray());
}

public void addRows(Object[][] rows) {
	for(Object [] row: rows)
		addRow(row);
}

public void addRows(List <Object []> rows) {
	this.rows.addAll(rows);
//	for(Object [] row: rows)
//		addRow(row);
}

public List<Object []> getRowsAsList(){
	return rows;
}

public Object [][] getRowsAsArray(){
	return (Object[][]) rows.toArray();
}

public Object [] getDataTableAsArray(){
	return getDataTableAsList().toArray();
}

public List <Object []> getDataTableAsList(){
	List <Object []> res = new LinkedList<>();
		res.add(labels.toArray());
		res.addAll(rows);
	return res;
}

@Override
public String toString() {
	String str = "DataTable [labels=[";
		for(String s : labels)
			str = str + s + ", ";
		str = str + "], rows=\"[";
		for(Object [] o : rows){
			str = str + "[";
			for(Object obj : o)
				str = str + obj.toString() + ", ";
			str = str + "]";
		}
	return str;
			
}

private static <E> ArrayList<E> newArrayList() {
  return new ArrayList<E>();
}
}
