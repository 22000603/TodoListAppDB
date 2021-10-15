package com.todo.menu;
public class Menu {

    public static void displaymenu()
    {
        System.out.println();
        System.out.println("< ToDoList 기능 >");
        System.out.println("[항목 추가] add");
        System.out.println("[항목 삭제] del");
        System.out.println("[항목 수정] edit");
        System.out.println("[항목 완료] comp");
        System.out.println("[항목 완료 취소] not_comp");	//추가 기능 1 : 완료 체크 취소하기 
        System.out.println("[전체 목록] ls");
        System.out.println("[제목내용 검색 기능] find <keyword>");
        System.out.println("[카테고리 검색 기능] find_cate <keyword>");
        System.out.println("[제목순 정렬] ls_name_asc");
        System.out.println("[제목역순 정렬] ls_name_desc");
        System.out.println("[날짜순 정렬] ls_date");
        System.out.println("[최신순 정렬] ls_date_desc");
        System.out.println("[중요도 낮은순 정렬] ls_imp_least");	//추가 기능 2 : 중요도 낮은 기준으로 나열하기 
        System.out.println("[중요도 높은순 정렬] ls_imp_most");	//추가 기능 3 : 중요도 높은 기준으로 나열하기 
        System.out.println("[카테고리 목록 출력] ls_cate");
        System.out.println("[완료된 항목 출력] ls_comp");
        System.out.println("[완료되지 않은 항목 출력] ls_not_comp");
        System.out.println("[기간지난 항목 삭제] clean");		//추가 기능 4 : 마감시간 지난 항목들 나열 후 모두 삭제하기  
        System.out.println("[종료] exit");
     
    }
    public static void prompt()
    {
    	System.out.print("\nCommand >> ");
    }

}
