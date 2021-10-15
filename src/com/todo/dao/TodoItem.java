package com.todo.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TodoItem {
	private int id;
	private String title;
    private String desc;
    private String current_date;
    private String category;
    private String due_date;
    private int is_completed = 0;
    private int rest_days;
    private int importance = 0;

    public TodoItem(String category, String title, String desc, String due_date, int importance){
    	this.category = category;
        this.title = title;
        this.desc = desc;
        this.due_date = due_date;
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        this.current_date=f.format ( new Date());
        rest_days = getRest_days(this.current_date, this.due_date);
        this.importance = importance;
    }
    public TodoItem(int id, String category, String title, String desc, String due_date, String date, int is_completed, int importance){
    	this.id = id;
    	this.category = category;
        this.title=title;
        this.desc=desc;
        this.due_date = due_date;
        this.current_date=date;
        this.is_completed=is_completed;
        
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        String now = f.format ( new Date());
        rest_days = getRest_days(now, this.due_date);
        this.importance = importance;
        
    }
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;;
    }
    
	@Override
	public String toString() {
		String list = "";
		String memo = "["+category+"] "+title+" : "+desc.trim();
		if(is_completed==1)
			list = String.format("%2d.[v][%d]%-30s [D%+02d] 마감:%s (작성:%s)", id, importance, memo,rest_days, due_date, current_date);
		else
			list = String.format("%2d.[ ][%d]%-30s [D%+02d] 마감:%s (작성:%s)", id, importance, memo,rest_days, due_date, current_date);
		return list;
	}
	public String toSaveString() {
		return importance+"##"+category+"##"+title+"##"+desc+"##"+due_date+"##"+current_date +"\n";
	}
	public int getCompleted() {
		return is_completed;
	}
	public void setCompleted(int com) {
		this.is_completed = com;
	}
	public int getRest_days(String now_date, String rest_date) {
		
		String [] n = now_date.substring(0,10).split("/");
		Calendar now = new GregorianCalendar(Integer.parseInt(n[0]), Integer.parseInt(n[1]), Integer.parseInt(n[2]));
		String [] r =rest_date.substring(0,10).split("/");
		Calendar rest = new GregorianCalendar(Integer.parseInt(r[0]), Integer.parseInt(r[1]), Integer.parseInt(r[2]));
		
		long diff = (now.getTimeInMillis() - rest.getTimeInMillis())/1000;
		int diffDays = (int) (diff / (24*60*60));
		
		return diffDays;
	}
	public int getRest_days() {
		return rest_days;
	}
	public void setRest_days(int rest_days) {
		this.rest_days = rest_days;
	}
	public int getImportance() {
		return importance;
	}
	public void setImportance(int importance) {
		this.importance = importance;
	}
    
}
