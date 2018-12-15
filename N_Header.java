/*
 * ��� ���.
 * ���� ������ �ؽ�Ʈ(����)�� ������ ��.
 * �� �� ������ private�� �����ϰ� setter/getter�� ������ ó��
 * setLevel�� ��� "###" �̷��� �ްų�, ���ڸ� �ްų� �� �� �ֵ��� ��.
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
