import java.util.ArrayList;
import java.util.LinkedList;

public class N_Blockquote extends Node{
	public LinkedList<Node> list = new LinkedList<Node>();

	
	public void addToList(String text){
		N_TextNode textnode = new N_TextNode();
		textnode.setContent(text);
		this.list.add(textnode);
	}
	
	public void addToList(N_Blockquote bqNode){
		this.list.add(bqNode);
	}
	
	public LinkedList<Node> getList(){
		return this.list;
	}
	
	public void deleteList(int index){
		this.list.remove(index);
	}
	
	public void addNewParagraph(){
		addToList("</p>\n<p>");
	}
	
	public int getListSize(){
		return this.list.size();
	}
	
	
	public void printNodeInfo(){
		System.out.println("[Blockquote Node]");
		System.out.print("text: ");
		for(int i=0;i<list.size();i++){
			list.get(i).printNodeInfo();
		}
	}
	
	public String accept(MDElementVisitor visitor){
		return visitor.visit(this);
	}
}
