package com.kamabizbazti.model.entities;


public class ChartWrapper {
private ChartType chartType;
private Object dataTable;
	private String title;
	private String vAxis;
	private String hAxis;

public ChartWrapper() {
	super();
}

public ChartWrapper(ChartType chartType, Object dataTable, String title, String vAxis, String hAxis) {
	super();
	this.chartType = chartType;
	this.dataTable = dataTable;
	this.title = title;
	this.vAxis = vAxis;
	this.hAxis = hAxis;
}

public ChartWrapper(ChartType chartType, DataTable dataTable, String title, String vAxis, String hAxis) {
	super();
	this.chartType = chartType;
	this.dataTable = dataTable;
	this.title = title;
	this.vAxis = vAxis;
	this.hAxis = hAxis;
}

	public ChartWrapper(ChartType chartType, Object dataTable, String title) {
		super();
		this.chartType = chartType;
		this.dataTable = dataTable;
		this.title = title;
	}

	public ChartWrapper(ChartType chartType, DataTable dataTable, String title) {
		super();
		this.chartType = chartType;
		this.dataTable = dataTable;
		this.title = title;
	}

public ChartType getChartType() {
	return chartType;
}

public void setChartType(ChartType chartType) {
	this.chartType = chartType;
}

public Object getDataTable() {
	return dataTable;
}

public void setDataTable(DataTable dataTable) {
	this.dataTable = dataTable;
}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getvAxis() {
		return vAxis;
	}

	public String gethAxis() {
		return hAxis;
	}

	public void setvAxis(String vAxis) {
		this.vAxis = vAxis;
	}

	public void sethAxis(String hAxis) {
		this.hAxis = hAxis;
	}
}
