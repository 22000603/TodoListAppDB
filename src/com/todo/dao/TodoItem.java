package com.todo.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoItem {
	private int id;
	private String title;
    private String desc;
    private String current_date;
    private String category;
    private String due_date;
    private int is_completed = 0;

    public TodoItem(String category, String title, String desc, String due_date){
    	this.category = category;
        this.title = title;
        this.desc = desc;
        this.due_date = due_date;
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        this.current_date=f.format ( new Date());
    }
    public TodoItem(int id, String category, String title, String desc, String due_date, String date, int is_completed){
    	this.id = id;
    	this.category = category;
        this.title=title;
        this.desc=desc;
        this.due_date = due_date;
        this.current_date=date;
        this.is_completed=is_completed;
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

		if(is_completed==1)
			return "[v]"+ id + ". [" + category + "] "+ title + " : " + desc + " [기간:" + due_date +  "] - " + current_date ;
		else
		return "[ ]"+id + ". [" + category + "] "+ title + " : " + desc + " [기간:" + due_date +  "] - " + current_date ;
	}
	public String toSaveString() {
		return category+"##"+title+"##"+desc+"##"+due_date+"##"+current_date +"\n";
	}
	public int getCompleted() {
		return is_completed;
	}
	public void setCompleted(int com) {
		this.is_completed = com;
	}
    
}
