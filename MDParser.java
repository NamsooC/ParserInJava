
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.*;
public class MDParser {
	LinkedList<Node> nodeList = new LinkedList<Node>();
	List<Token> tokenList = new LinkedList<Token>();
	boolean bqFlag = false;

//=========tokenize: String을 받아서 토큰리스트 리턴==============================
	public List<Token> tokenize(String str){
		List<Token> tokenList = new ArrayList<Token>();
		if(str.length()==0){
			T_newLine token = new T_newLine();
			token.setContent("\n");
			tokenList.add(token);
			return tokenList;
		}
		//패턴: (PlainText) 이거나 (Symbol) 들을 찾는다
		Pattern p = Pattern.compile("([a-zA-Z0-9]+)|(\\S+)");
		Matcher matcher = p.matcher(str);
		
		while(matcher.find()){
			for (int i = 1; i <= matcher.groupCount(); i++) {
				//Group1: 이 토큰이 plainText라면 PlainText토큰 노드를 생성해서 setContent하고(텍스트추가) 토큰리스트에 추가
		    	if(i==1 && matcher.group(i) != null){
			        T_plainText token = new T_plainText();
			        token.setContent(matcher.group(i));
			    	tokenList.add(token);
		        }
				//Group2: 이 토큰이 symbol이라면 MDSymbol토큰 노드를 생성해서 setContent하고(텍스트추가) 토큰리스트에 추가
		    	else if(i==2 && matcher.group(i) != null ){
		    		//Symbol 중 < 로 시작하는건 HTML로 처리해야되는데 일단 보류하고 여기선 plainText처럼 처리...
		    		if(matcher.group(i).charAt(0) == '<'){
		    			T_plainText token = new T_plainText();
				        token.setContent(matcher.group(i));
				    	tokenList.add(token);
		    		}
		    		else{	//Symbol 토큰 만들어서 추가		    		
			    		T_symbol token = new T_symbol();
			        	token.setContent(matcher.group(i));
				    	tokenList.add(token);
		    		}
		        }
		    	//Group3
		    }
		}
		//토큰리스트 리턴
		return tokenList;
	}
//============================================================================

	
	
	
//==========addNodeToList: 노드랑 리스트를 받아서 노드를 리스트에 삽입===================
	public void addNodeToList(Node tempNode, LinkedList<Node> nodeList){
		List<Token> tokenList = tempNode.getTokenList();
		if(tempNode instanceof N_Blockquote){
			nodeList.add(tempNode);
			return;
		}
		//첫 토큰 : Symbol
		if(tokenList.get(0) instanceof T_symbol){
			//if header
			if(header(tempNode, nodeList) == true)
				return;
			//if blockquote
			else if(blockquote(tempNode, nodeList) == true){
				return;
			}
				
			}
		//첫 토큰 : Plain Text
		else if(tokenList.get(0) instanceof T_plainText)
		{
			//TextNode 생성
			N_TextNode textnode = new N_TextNode();
			String text = new String();
			//단어단위로 연결된 토큰들을 한개의 문자열로 합침
			int size = tokenList.size();
			for(int i=0;i<size;i++){
				text = text.concat(tokenList.get(i).getContent()+ " ");
			}
			//textnode에 하나로 합친 문자열을 전달
			textnode.setContent(text);
			nodeList.add(textnode);
		}
		//첫 토큰 : New Line
		else if(tokenList.get(0) instanceof T_newLine){
			N_newLine newLine = new N_newLine();
			nodeList.add(newLine);
		}
		else{
			System.out.println("다른 토큰는 아직 구현 안됨");
		}
		
	}
	
	
	
	
//==========================================================================//
//==========================================================================//
//							노드별 메소드										//
//==========================================================================//
//==========================================================================//
	
	
	
//==========header: 토큰리스트랑 노드리스트 받아서 삽입하기===================
	public boolean header(Node tempNode, LinkedList<Node> nodeList){
		//----------------------------------- Header Node -------------------------------------------
		List<Token> tokenList = tempNode.getTokenList();
		//토큰의 첫번째 Symbol이 #종류인지 확인
		if( tokenList.get(0).getContent().equals("#") ||
			tokenList.get(0).getContent().equals("##") ||
			tokenList.get(0).getContent().equals("###") ||
			tokenList.get(0).getContent().equals("####") ||
			tokenList.get(0).getContent().equals("#####") ||
			tokenList.get(0).getContent().equals("######") )
		{
			//헤더노드 생성해서 정보 전달
			N_Header header = new N_Header();
		//1. 레벨 설정
			header.setLevel(tokenList.get(0).getContent());
		//2. 텍스트 설정
			String text = "";
			int size = tokenList.size();
			for(int i=1;i<size;i++){
				//PlainText 토큰만 헤더노드의 Text로 전달
				if(tokenList.get(i) instanceof T_plainText){
					text = text.concat(tokenList.get(i).getContent()+ " ");
				}
			}
			header.setText(text);
			//노드 리스트에 지금 만든 헤더 노드 삽입
			nodeList.add(header);
			return true;
		}
		//토큰에 '=' 종류만 있고 뒤에는 아무것도 없는 경우
		else if(tokenList.get(0).getContent().charAt(0)=='=' && tokenList.size()==1){
			if(nodeList.getLast() instanceof N_TextNode){	// '='나오기 직전 노드가 텍스트노드인지 확인
				//헤더 노드 생성
				N_Header header = new N_Header();
				//1. 레벨 설정. (= 계통은 1)
				header.setLevel(1);
				//2. 텍스트 설정 (직전 노드에서 텍스트를 가져와서 그걸 사용)
				header.setText(((N_TextNode)nodeList.getLast()).getContent());
				//이제 직전노드(텍스트만 있는)는 필요없으니까 지우고 노드리스트에 헤더노드 삽입
				nodeList.removeLast();
				nodeList.add(header);
				return true;
			}else{System.out.println("md syntax error"); return false; }
		}
		//토큰에 '-' 종류만 있고 뒤에는 아무것도 없는 경우
		else if(tokenList.get(0).getContent().charAt(0)=='-' && tokenList.size()==1){
			if(nodeList.getLast() instanceof N_TextNode){	// '-'나오기 직전 노드가 텍스트노드인지 확인
				//헤더 노드 생성
				N_Header header = new N_Header();
				//1. 레벨 설정. (= 계통은 1)
				header.setLevel(2);
				//2. 텍스트 설정 (직전 노드에서 텍스트를 가져와서 그걸 사용)
				header.setText(((N_TextNode)nodeList.getLast()).getContent());
				//이제 직전노드(텍스트만 있는)는 필요없으니까 지우고 노드리스트에 헤더노드 삽입
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
		//blockquote가 아니면 false리턴
		if(tokenList.get(0).getContent().equals(">") != true){	
			return false;
		}
		
		if(tokenList.get(0).getContent().equals(">") == true)// && tokenList.get(1).getContent().equals(">") == true){
		{
			
			//이번 노드가 nested BQ인 경우
			if(tokenList.size()>1 && tokenList.get(1).getContent().equals(">") == true)
			{
				//이전에 BQ없는데 처음부터 nested BQ인 경우
				if(nodeList.size()<=0 || ( nodeList.size()>0 && !((lastNode=nodeList.getLast()) instanceof N_Blockquote)))
				{
					N_Blockquote bq = new N_Blockquote();
					addNodeToList(bq, nodeList);
					addNodeToList(newNode,nodeList);	//아래 경우로 만드는 것
					return true;
				}
				//마지막 노드가 BQ이고 그 안에서 nest 하는 경우
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
			//Nested BQ 아닌 경우
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
				//리스트의 마지막 노드가 BQ였다
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
