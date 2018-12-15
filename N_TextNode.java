/*
 * 텍스트노드.
 * 순수하게 텍스트만 들고있는 노드
 * MD의 시작이 일반 텍스트로 시작되는 경우 그냥 문자열 하나만 들고 있으면 됨.
 * 중간에 HTML이 들어가는 경우는 따로 구현.
 */

public class N_TextNode extends Node{
	private String text;
	
	public void setContent(String text){
		this.text = text;
	}
	public String getContent(){
		return text;
	}
	
	public void printNodeInfo(){
		System.out.println("[Text Node]");
		System.out.println("Text : "+ this.text);
	}
	
	
	public String accept(MDElementVisitor visitor){
		return visitor.visit(this);
	}
	

}
