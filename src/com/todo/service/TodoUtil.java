package com.todo.service;

import java.io.BufferedReader;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {

	/**
	 * todolist에 새로운 항목을 추가해 줍니다.
	 * 
	 * @param 사용자가 실행하고 있는 todolist 어플
	 */
	public static void createItem(TodoList list) {

		String title, desc, category, due_date;
		int importance;
		Scanner sc = new Scanner(System.in);

		System.out.print("[항목 추가]\n");
		System.out.print("카테고리 : ");
		category = sc.nextLine().trim();

		System.out.print("제목 : ");
		title = sc.nextLine().trim();
		if (list.isDuplicate("title", title)) {
			System.out.printf("제목이 중복되어 사용하실 수 없습니다 ! \n");
			return;
		}

		System.out.print("내용 : ");
		desc = sc.nextLine().trim();

		System.out.print("마감날짜 ex)2021/09/26 : ");
		due_date = sc.nextLine().trim();
		
		System.out.print("중요도 (1/2/3) : ");
		importance = sc.nextInt();

		TodoItem t = new TodoItem(category, title, desc, due_date, importance);
		if (list.addItem(t) > 0)
			System.out.println("새로운 리스트가 추가되었습니다. ");
	}

	/**
	 * 삭제하고 싶은 항목의 번호를 여러 개 입력 받아 삭제해 줍니다.
	 * 
	 * @param 사용자가 실행하고 있는 todolist 어플
	 */
	public static void deleteItem(TodoList l) {

		Scanner sc = new Scanner(System.in);

		System.out.print("[항목 삭제]\n" + "삭제할 항목의 번호를 입력하세요 : ");
		String number = sc.nextLine().trim();
		// 입력된 여러 개의 번호를 배열로 저장
		String[] numbers = number.split(",");
		// Stream API를 사용하여 string 배열을 int배열로 변환
		int[] n = Arrays.stream(numbers).mapToInt(Integer::parseInt).toArray();
		String d;
		
		for (int deletion : n) {
			for (TodoItem item : l.getList()) {
				if (deletion == item.getId()) {
					System.out.println(item.toString());
					System.out.print("위 항목을 삭제하시겠습니까? (y/n) : ");
					d = sc.next();
					if (d.equals("y")) {
						if (l.deleteItem(deletion) > 0)
							System.out.println(deletion + "번이 삭제되었습니다.");
					} else {
						System.out.println(deletion + "번 변경이 취소되었습니다.");
					}
					break;
				}
			}
		}
	}

	/**
	 * 사용자가 원하는 번호의 list 내용들을 수정해 줍니다. 사용자가 원하는 영역만 수정할 수 있습니다.
	 * 
	 * @param 사용자가 실행하고 있는 todolist 어플
	 */
	public static void updateItem(TodoList l) {

		String new_title, new_desc, new_category, new_due_date;
		int importance;
		Scanner sc = new Scanner(System.in);

		System.out.print("[항목 수정]\n" + "수정할 항목의 번호를 입력하세요 : ");
		int number = sc.nextInt();
		sc.nextLine();

		if (!(l.isDuplicate("id", Integer.toString(number)))) {
			System.out.println(number + "번이 존재하지 않습니다! ");
			return;
		} else {
			int edition = 0;
			for (TodoItem item : l.getList()) {
				if (number == item.getId()) {
					System.out.println(item.toString());

					System.out.print("수정할 항목의 영역들을 입력하세요 (카테고리/제목/내용/마감/중요도) : ");
					String update = sc.nextLine().trim();

					if (update.contains("카테고리")) {
						System.out.print("새 카테고리 : ");
						new_category = sc.nextLine().trim();
						item.setCategory(new_category);
					}
					if (update.contains("제목")) {
						while (true) {
							System.out.print("새 제목 : ");
							new_title = sc.nextLine().trim();

							if (l.isDuplicate("title", new_title))
								System.out.println("제목이 중복되어 사용하실 수 없습니다!");
							else
								break;
						}
						item.setTitle(new_title);
					}
					if (update.contains("내용")) {
						System.out.print("새 내용 : ");
						new_desc = sc.nextLine().trim();
						item.setDesc(new_desc);
					}
					if (update.contains("마감")) {
						System.out.print("새 마감날짜 : ");
						new_due_date = sc.nextLine().trim();
						item.setDue_date(new_due_date);
					}
					if (update.contains("중요도")) {
						System.out.print("새 중요도 : ");
						importance = sc.nextInt();
						item.setImportance(importance);
					}
					
					SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
			        item.setCurrent_date(f.format(new Date()));
					
					if (l.editItem(item) > 0)
						System.out.println(number + "번이 변경되었습니다.");
					break;
				}
			}
		}
	}

	/**
	 * todolist의 모든 항목들을 출력해 줍니다.
	 * 
	 * @param 사용자가 실행하고 있는 todolist 어플
	 */
	public static void listAll(TodoList l) {
		System.out.printf("<전체 목록, 총 %d개>\n", l.getCount());
		for (TodoItem item : l.getList()) {
			System.out.println(item.toString());
		}
	}

	public static void listAll(TodoList l, String field, int ordering) {
		System.out.printf("<전체 목록, 총 %d개>\n", l.getCount());
		for (TodoItem item : l.getOrderedList(field, ordering)) {
			System.out.println(item.toString());
		}
	}

	public static void listAll(TodoList l, int com) {
		int count = 0;
		for (TodoItem item : l.getList(com)) {
			System.out.println(item.toString());
			count++;
		}
		
		if(com==1) System.out.printf("%d개의 항목이 완료되었습니다 :)\n", count);
		else System.out.printf("%d개의 항목이 완료되지 않았습니다 :(\n", count);
			
		
	}

	/**
	 * 특정 키워드를 포함하고 있는 항목을 찾아 출력해 줍니다.
	 * 
	 * @param 사용자가 실행하고 있는 todolist 어플
	 * @param word ( field + keyword )
	 */
	public static void findList(TodoList l, String word) {
		int count = 0;
		for (TodoItem item : l.getList(word)) {
			System.out.println(item.toString());
			count++;
		}
		System.out.printf("총 %d개 항목을 찾았습니다.\n", count);
	}

	/**
	 * todolist.db에 저장된 모든 카테고리들을 출력해 줍니다.
	 * 
	 * @param 사용자가 실행하고 있는 todolist 어플
	 */
	public static void listCategory(TodoList l) {

		int number = 0;
		for (String item : l.getCategories()) {
			System.out.print(item + " ");
			number++;
		}
		System.out.println("\n총 " + number + "개의 카테고리가 등록되어 있습니다.");

	}

	public static void completeItem(TodoList l, String number) {
		String [] numbers = number.split(",");
		for(String n : numbers) {
			l.compleItem(Integer.parseInt(n));
		}
		System.out.printf("%s번 완료 체크하였습니다. ", number);
	
	}

	
	public static void uncompleteItem(TodoList l, String number) {
		String [] numbers = number.split(",");
		for(String n : numbers) {
			l.uncompleItem(Integer.parseInt(n));
		}
		System.out.printf("%s번 완료 체크를 취소하였습니다. ", number);
		
	}

	public static void cleanList(TodoList l) {
		ArrayList<Integer> closed = new ArrayList<Integer>();
		for(TodoItem item : l.getList()) {
			if(item.getRest_days()>0) {
				System.out.println(item.toString());
				closed.add(item.getId());
			}
		}
		Scanner sc = new Scanner(System.in);
		System.out.print("위 항목들을 모두 삭제하시겠습니까? (y/n) : ");
		String d = sc.next();
		if (d.equals("y")) {
			for( int id : closed) l.deleteItem(id);
				System.out.println("TodoList가 청소되었습니다.");
		} else {
			System.out.println("청소가 취소되었습니다.");
		}
		
	}
}
