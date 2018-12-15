
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.*;
public class MDParser {
	LinkedList<Node> nodeList = new LinkedList<Node>();
	List<Token> tokenList = new LinkedList<Token>();
	boolean bqFlag = false;

//=========tokenize: String�� �޾Ƽ� ��ū����Ʈ ����==============================
	public List<Token> tokenize(String str){
		List<Token> tokenList = new ArrayList<Token>();
		if(str.length()==0){
			T_newLine token = new T_newLine();
			token.setContent("\n");
			tokenList.add(token);
			return tokenList;
		}
		//����: (PlainText) �̰ų� (Symbol) ���� ã�´�
		Pattern p = Pattern.compile("([a-zA-Z0-9]+)|(\\S+)");
		Matcher matcher = p.matcher(str);
		
		while(matcher.find()){
			for (int i = 1; i <= matcher.groupCount(); i++) {
				//Group1: �� ��ū�� plainText��� PlainText��ū ��带 �����ؼ� setContent�ϰ�(�ؽ�Ʈ�߰�) ��ū����Ʈ�� �߰�
		    	if(i==1 && matcher.group(i) != null){
			        T_plainText token = new T_plainText();
			        token.setContent(matcher.group(i));
			    	tokenList.add(token);
		        }
				//Group2: �� ��ū�� symbol�̶�� MDSymbol��ū ��带 �����ؼ� setContent�ϰ�(�ؽ�Ʈ�߰�) ��ū����Ʈ�� �߰�
		    	else if(i==2 && matcher.group(i) != null ){
		    		//Symbol �� < �� �����ϴ°� HTML�� ó���ؾߵǴµ� �ϴ� �����ϰ� ���⼱ plainTextó�� ó��...
		    		if(matcher.group(i).charAt(0) == '<'){
		    			T_plainText token = new T_plainText();
				        token.setContent(matcher.group(i));
				    	tokenList.add(token);
		    		}
		    		else{	//Symbol ��ū ���� �߰�		    		
			    		T_symbol token = new T_symbol();
			        	token.setContent(matcher.group(i));
				    	tokenList.add(token);
		    		}
		        }
		    	//Group3
		    }
		}
		//��ū����Ʈ ����
		return tokenList;
	}
//============================================================================

	
	
	
//==========addNodeToList: ���� ����Ʈ�� �޾Ƽ� ��带 ����Ʈ�� ����===================
	public void addNodeToList(Node tempNode, LinkedList<Node> nodeList){
		List<Token> tokenList = tempNode.getTokenList();
		if(tempNode instanceof N_Blockquote){
			nodeList.add(tempNode);
			return;
		}
		//ù ��ū : Symbol
		if(tokenList.get(0) instanceof T_symbol){
			//if header
			if(header(tempNode, nodeList) == true)
				return;
			//if blockquote
			else if(blockquote(tempNode, nodeList) == true){
				return;
			}
				
			}
		//ù ��ū : Plain Text
		else if(tokenList.get(0) instanceof T_plainText)
		{
			//TextNode ����
			N_TextNode textnode = new N_TextNode();
			String text = new String();
			//�ܾ������ ����� ��ū���� �Ѱ��� ���ڿ��� ��ħ
			int size = tokenList.size();
			for(int i=0;i<size;i++){
				text = text.concat(tokenList.get(i).getContent()+ " ");
			}
			//textnode�� �ϳ��� ��ģ ���ڿ��� ����
			textnode.setContent(text);
			nodeList.add(textnode);
		}
		//ù ��ū : New Line
		else if(tokenList.get(0) instanceof T_newLine){
			N_newLine newLine = new N_newLine();
			nodeList.add(newLine);
		}
		else{
			System.out.println("�ٸ� ��ū�� ���� ���� �ȵ�");
		}
		
	}
	
	
	
	
//==========================================================================//
//==========================================================================//
//							��庰 �޼ҵ�										//
//==========================================================================//
//==========================================================================//
	
	
	
//==========header: ��ū����Ʈ�� ��帮��Ʈ �޾Ƽ� �����ϱ�===================
	public boolean header(Node tempNode, LinkedList<Node> nodeList){
		//----------------------------------- Header Node -------------------------------------------
		List<Token> tokenList = tempNode.getTokenList();
		//��ū�� ù��° Symbol�� #�������� Ȯ��
		if( tokenList.get(0).getContent().equals("#") ||
			tokenList.get(0).getContent().equals("##") ||
			tokenList.get(0).getContent().equals("###") ||
			tokenList.get(0).getContent().equals("####") ||
			tokenList.get(0).getContent().equals("#####") ||
			tokenList.get(0).getContent().equals("######") )
		{
			//������ �����ؼ� ���� ����
			N_Header header = new N_Header();
		//1. ���� ����
			header.setLevel(tokenList.get(0).getContent());
		//2. �ؽ�Ʈ ����
			String text = "";
			int size = tokenList.size();
			for(int i=1;i<size;i++){
				//PlainText ��ū�� �������� Text�� ����
				if(tokenList.get(i) instanceof T_plainText){
					text = text.concat(tokenList.get(i).getContent()+ " ");
				}
			}
			header.setText(text);
			//��� ����Ʈ�� ���� ���� ��� ��� ����
			nodeList.add(header);
			return true;
		}
		//��ū�� '=' ������ �ְ� �ڿ��� �ƹ��͵� ���� ���
		else if(tokenList.get(0).getContent().charAt(0)=='=' && tokenList.size()==1){
			if(nodeList.getLast() instanceof N_TextNode){	// '='������ ���� ��尡 �ؽ�Ʈ������� Ȯ��
				//��� ��� ����
				N_Header header = new N_Header();
				//1. ���� ����. (= ������ 1)
				header.setLevel(1);
				//2. �ؽ�Ʈ ���� (���� ��忡�� �ؽ�Ʈ�� �����ͼ� �װ� ���)
				header.setText(((N_TextNode)nodeList.getLast()).getContent());
				//���� �������(�ؽ�Ʈ�� �ִ�)�� �ʿ�����ϱ� ����� ��帮��Ʈ�� ������ ����
				nodeList.removeLast();
				nodeList.add(header);
				return true;
			}else{System.out.println("md syntax error"); return false; }
		}
		//��ū�� '-' ������ �ְ� �ڿ��� �ƹ��͵� ���� ���
		else if(tokenList.get(0).getContent().charAt(0)=='-' && tokenList.size()==1){
			if(nodeList.getLast() instanceof N_TextNode){	// '-'������ ���� ��尡 �ؽ�Ʈ������� Ȯ��
				//��� ��� ����
				N_Header header = new N_Header();
				//1. ���� ����. (= ������ 1)
				header.setLevel(2);
				//2. �ؽ�Ʈ ���� (���� ��忡�� �ؽ�Ʈ�� �����ͼ� �װ� ���)
				header.setText(((N_TextNode)nodeList.getLast()).getContent());
				//���� �������(�ؽ�Ʈ�� �ִ�)�� �ʿ�����ϱ� ����� ��帮��Ʈ�� ������ ����
				nodeList.removeLast();
				nodeList.add(header);
				return true;
			}else{System.out.println("md syntax error"); return false;}
		}
		else //code shouldn't reach here
			return false;
	}
	
	
	
	
	public boolean blockquote(Node newNode, LinkedList<Node> nodeList){
		List<Token> tokenList = newNode.getTokenList();
		Node lastNode;
		//blockquote�� �ƴϸ� false����
		if(tokenList.get(0).getContent().equals(">") != true){	
			return false;
		}
		
		if(tokenList.get(0).getContent().equals(">") == true)// && tokenList.get(1).getContent().equals(">") == true){
		{
			
			//�̹� ��尡 nested BQ�� ���
			if(tokenList.size()>1 && tokenList.get(1).getContent().equals(">") == true)
			{
				//������ BQ���µ� ó������ nested BQ�� ���
				if(nodeList.size()<=0 || ( nodeList.size()>0 && !((lastNode=nodeList.getLast()) instanceof N_Blockquote)))
				{
					N_Blockquote bq = new N_Blockquote();
					addNodeToList(bq, nodeList);
					addNodeToList(newNode,nodeList);	//�Ʒ� ���� ����� ��
					return true;
				}
				//������ ��尡 BQ�̰� �� �ȿ��� nest �ϴ� ���
				else if(nodeList.size()>0 && ((lastNode=nodeList.getLast()) instanceof N_Blockquote))
				{
					if(tokenList.size()==1){
						((N_Blockquote)lastNode).addNewParagraph();
						return true;
					}else{
						tokenList.remove(0);
						Node node = new Node(tokenList);
						addNodeToList(node, ((N_Blockquote)lastNode).getList());
						return true;
					}
				}
				else
					return false;
			}
			//Nested BQ �ƴ� ���
			else
			{
				
				if(nodeList.size()<=0 || ( nodeList.size()>0 && !((lastNode=nodeList.getLast()) instanceof N_Blockquote)))
				{
					N_Blockquote bq = new N_Blockquote();
					if(tokenList.size()==1){
						bq.addNewParagraph();
						return true;
					}else{
						tokenList.remove(0);
						Node node = new Node(tokenList);
						addNodeToList(node, bq.getList());
						addNodeToList(bq, nodeList);	
						return true;
					}
				}
				//����Ʈ�� ������ ��尡 BQ����
				else if(nodeList.size()>0 && ((lastNode=nodeList.getLast()) instanceof N_Blockquote))
				{
					if(tokenList.size()==1){
						((N_Blockquote)lastNode).addNewParagraph();
						return true;
					}else{
						tokenList.remove(0);
						Node node = new Node(tokenList);
						addNodeToList(node, ((N_Blockquote)lastNode).getList());
						return true;
					}			
				}
				else
					return false;
			
			}
		}
		return false;
	}
					
					
			
	
	
	
	
	
	
	
	
	
	
	public void printAllNode(){
		for(int i=0;i<nodeList.size();i++)
			nodeList.get(i).printNodeInfo();
	}
	
	
	public String concatString(List<Token> tokenList, int startIndex){
		String text = new String();
		for(int i=startIndex;i<tokenList.size();i++){
			text = text.concat(tokenList.get(i).getContent()+ " ");
		}
		return text;
	}

}
