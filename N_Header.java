/*
 * 헤더 노드.
 * 레벨 정보랑 텍스트(내용)이 있으면 됨.
 * 둘 다 변수는 private로 선언하고 setter/getter로 정보를 처리
 * setLevel의 경우 "###" 이런걸 받거나, 숫자를 받거나 할 수 있도록 함.
 */
public class N_Header extends Node{
	private int level;
	private String text;
	
	public void setLevel(String hash){
		level = hash.length();
	}
	public void setLevel(int num){
		level = num;
	}
	public int getLevel(){
		return this.level;
	}
	public void setText(String text){
		this.text = text;
	}
	public String getText(){
		return this.text;
	}
	
	public void printNodeInfo(){
		System.out.println("[Header Node]");
		System.out.println("level : "+level+" "+"Text : "+text);
	}
	
	public String accept(MDElementVisitor visitor){
		return visitor.visit(this);
	}
	
}
