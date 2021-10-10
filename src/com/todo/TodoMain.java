package com.todo;

import java.util.Scanner;

import com.todo.dao.TodoList;
import com.todo.menu.Menu;
import com.todo.service.TodoUtil;

public class TodoMain {
	/**
	 * todolist 어플을 실행시킵니다.
	 */
	public static void start() {
	
		Scanner sc = new Scanner(System.in);
		TodoList l = new TodoList();
		//boolean isList = false;
		boolean quit = false;
		String keyword;
		//l.importData("todolist.txt");
		Menu.displaymenu();
		do {
			
			Menu.prompt();
			//isList = false;
			String choice = sc.next();
			//sc.next();
			
			switch (choice) {

			case "add":
				TodoUtil.createItem(l);
				break;
			
			case "del":
				TodoUtil.deleteItem(l);
				break;
				
			case "edit":
				TodoUtil.updateItem(l);
				break;
				
			case "ls":
				TodoUtil.listAll(l);
				break;

			case "ls_name":
				//l.sortByName();
				System.out.println("정렬 방법: 제목순 ");
				TodoUtil.listAll(l, "title", 1);
				break;

			case "ls_name_desc":
				System.out.println("정렬 방법: 제목역순 ");
				TodoUtil.listAll(l, "title", 0);
				break;
				
			case "ls_date":
				System.out.println("정렬 방법: 날짜순 ");
				TodoUtil.listAll(l, "due_date", 1);
				break;
				
			case "ls_date_desc":
				System.out.println("정렬 방법: 날짜역순 ");
				TodoUtil.listAll(l, "due_date", 0);
				break;
				
			case "exit":
				quit = true;
				break;
			
			case "help":
				Menu.displaymenu();
				break;
				
			case "find":
				keyword = sc.nextLine().trim();
				TodoUtil.findList(l, "title,memo,"+keyword);
				break;
				
			case "find_cate":
				keyword = sc.nextLine().trim();
				TodoUtil.findList(l, "category,"+keyword);
				break;
		
			case "ls_cate":
				TodoUtil.listCategory(l);
				break;
				
			case "comp":
				int number = sc.nextInt();
				TodoUtil.completeItem(l, number);
				break; 
				
			case "ls_comp":
				System.out.println("완료된 항목 : ");
				TodoUtil.listAll(l, 1);
				break;
				
			case "ls_not_comp":
				System.out.println("완료되지 않은 항목 : ");
				TodoUtil.listAll(l, 0);
				break;

			default:
				System.out.println("존재하지 않는 기능입니다.\n목록을 보시려면 help를 입력해 주세요.");
				break;
			}
			
			//if(isList) l.listAll();
			//if(isList) TodoUtil.listAll(l);
		} while (!quit);
		//TodoUtil.saveList(l, "todolist.txt");
		System.out.print("어플을 종료합니다. ");
	}
}
