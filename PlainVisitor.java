import java.util.LinkedList;

public class PlainVisitor implements MDElementVisitor{
	public String startHtml(){
		String str="<!DOCTYPE html>\n<html>\n<body>\n";
		return str;
	}
	
	public String endHtml(){
		String str="\n</body>\n</html>";
		return str;
	}
	
	@Override
	public String visit(N_Header header) {
		int level = header.getLevel();
		String text = header.getText();
		String str = "<h"+level+">"+text+"</h"+level+">";
		return str;
	}

	@Override
	public String visit(N_TextNode textnode) {
		String str = textnode.getContent();
		return str;
	}
	
	public String visit(N_newLine newLine){
		return "\n";
	}



	public String visit(N_Blockquote blockquote){
		LinkedList<Node> list = blockquote.getList();
		String str = new String();
		str = str.concat("<blockquote>");
	
		for(int i=0;i<list.size();i++){
	
			if(list.get(i) instanceof N_TextNode){
				str = str.concat(visit((N_TextNode)list.get(i)));
			}else if(list.get(i) instanceof N_Header){
				str = str.concat(visit((N_Header)list.get(i)));
			}else if(list.get(i) instanceof N_newLine){
				str = str.concat(visit((N_newLine)list.get(i)));
			}else if(list.get(i) instanceof N_Blockquote){
				str = str.concat(visit((N_Blockquote)list.get(i)));
			}else{}

		}
		
		str = str.concat("</blockquote>");
		return str;
	}
	

}
