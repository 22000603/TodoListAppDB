package com.todo.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.todo.service.DbConnect;
import com.todo.service.TodoSortByDate;
import com.todo.service.TodoSortByDateDesc;
import com.todo.service.TodoSortByName;

public class TodoList {
	Connection conn;

	/**
	 * Data base를 사용할 수 있도록 만들어 줍니다. 
	 */
	public TodoList() {
		this.conn = DbConnect.getConnection();
	}

	/**
	 * 특정 파일의 데이터를 todolist.db에 저장해 줍니다.
	 * @param filename
	 */
	public void importData(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			String sql = "INSERT into list (title, memo, category, current_date, due_date)" + "values (?, ?, ?, ?, ?);";
			int records = 0;
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "##");
				String category = st.nextToken();
				String title = st.nextToken();
				String desc = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();

				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, title);
				pstmt.setString(2, desc);
				pstmt.setString(3, category);
				pstmt.setString(4, current_date);
				pstmt.setString(5, due_date);
				int count = pstmt.executeUpdate();
				if (count > 0)
					records++;
				pstmt.close();
			}
			System.out.println(records + " recods read!!");
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 새로운 항목을 todolist.db에 등록해 줍니다.
	 * @param The list that user wants to add.
	 * @return A positive value when db is changed correctly.
	 */
	public int addItem(TodoItem t) {
		String sql = "insert into list (title, memo, category, current_date, due_date, rest_days, importance)"
				+ " values (?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			pstmt.setInt(6, t.getRest_days());
			pstmt.setInt(7,  t.getImportance());
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 사용자가 원하는 항목의 데이터를 todolist.db에서 삭제합니다. 
	 * @param ID of the list user want to delete.
	 * @return A positive value when db is changed correctly.
	 */
	public int deleteItem(int index) {
		String sql = "delete from list where id=?;";
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * todolist.db에서 사용자가 원하는 항의 내용을 변경해 줍니다.
	 * @param The new content of the list that user wants to edit.
	 * @return A positive value when db is changed correctly.
	 */
	public int editItem(TodoItem t) {
		String sql = "update list set title=?, memo=?, category=?, current_date=?, due_date=?, rest_days=?, importance=?" + " where id = ?;";
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			pstmt.setInt(6, t.getRest_days());
			pstmt.setInt(7, t.getImportance());
			pstmt.setInt(8, t.getId());
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * todolist db에 존재하는 모든 항목들을 구해줍니다.
	 * @return all data of todolist db
	 */
	public ArrayList<TodoItem> getList() {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM list;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed = rs.getInt("is_completed");
				int importance = rs.getInt("importance");
				TodoItem t = new TodoItem(id, category, title, description, due_date, current_date, is_completed, importance);
				list.add(t);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * field에 keyword가 포함되어 있는 항목 데이터를 찾아줍니다. 
	 * 사용자가 find, find_cate를 입력했을 시 작동되는 메소드 입니다.
	 * 
	 * @param word (field + keyword)
	 * @return the list that contains keyword
	 */
	public ArrayList<TodoItem> getList(String word) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		String[] search = word.split(",");
		// word의 마지막 단어는 keyword이다.
		String keyword = "'%" + search[search.length - 1] + "%'";
		
		try {
			String sql = "SELECT * FROM list WHERE ";
			int i;
			for (i = 0; i < search.length - 2; i++) {
				sql += search[i] + " like " + keyword + " or ";
				/*
				 * 모든 영역에서 keyword를 포함하는 list를 찾을 수 도 있다 sql = sql + search[i] + " like " +
				 * keyword + "and ";
				 */
			}
			sql += search[i] + " like " + keyword + ";"; // last sentence
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed = rs.getInt("is_completed");
				int importance = rs.getInt("importance");
				TodoItem t = new TodoItem(id, category, title, description, due_date, current_date, is_completed, importance);
				list.add(t);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * todolist.db에 존재하는 항목들의 개수를 구할 때 씁니다.
	 * 
	 * @return count of todolist's data
	 */
	public int getCount() {
		Statement stmt;
		int count = 0;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT count(id) FROM list;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count = rs.getInt("count(id)");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * field에 value값을 가지는 항목들의 개수를 반환합니다.
	 * 1개 이상이면 중복임을 나타냅니다. 
	 * @param field
	 * @param value
	 * @return existence
	 */
	public boolean isDuplicate(String field, String value) {
		
		
		Statement stmt;
		int count = 0;
		try {
			String sql = "select count(id) from list where "+ field +" = '"+value+"';";
			stmt = conn.createStatement();
			ResultSet r = stmt.executeQuery(sql);
			r.next();
			count = r.getInt("count(id)");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(count>0) return true;
		else return false;
		
	}

	/**
	 * 현재 todolist.db 에 존재하는 모든 카테고리들이 무엇이 있는 지 구해줍니다.
	 * @return list of all categories
	 */
	public ArrayList<String> getCategories() {

		ArrayList<String> list = new ArrayList<String>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT DISTINCT category FROM list;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
			list.add(rs.getString("category"));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 사용자의 원하는 기준에 따라 데이터들을 정렬합니다. 
	 * @param 정렬하고 싶은 기준 
	 * @param 오름차순, 내림차순에 대한 정보 
	 * @return 정렬된 데이터 
	 */
	public ArrayList<TodoItem> getOrderedList(String field, int ordering) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM LIST ORDER BY " + field;
			if(ordering==0) sql += " desc";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed = rs.getInt("is_completed");
				int importance = rs.getInt("importance");
				TodoItem t = new TodoItem(id, category, title, description, due_date, current_date, is_completed, importance);
				list.add(t);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getList(int com) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM list where is_completed = " + com;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed = rs.getInt("is_completed");
				int importance = rs.getInt("importance");
				TodoItem t = new TodoItem(id, category, title, description, due_date, current_date, is_completed, importance);
				list.add(t);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public int compleItem(int number) {
		
		Statement stmt;
		int count = 0;
		try {
			stmt = conn.createStatement();
			String sql = "update list set is_completed = 1 where id = " + number + ";";
			count = stmt.executeUpdate(sql);
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int uncompleItem(int number) {
		Statement stmt;
		int count = 0;
		try {
			stmt = conn.createStatement();
			String sql = "update list set is_completed = 0 where id = " + number + ";";
			count = stmt.executeUpdate(sql);
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

}
