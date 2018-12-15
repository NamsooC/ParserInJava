

import java.util.Iterator;
import java.util.List;

public class Node implements MDElement{
	private List<Token> tokenList; 
	public Node(){}
	public Node(List<Token> tokenList){ setToken(tokenList); }
	
	//토큰 리스트 setter
	public void setToken(List<Token> token){
		this.tokenList = token;
	}
	//토큰 리스트 getter
	public List<Token> getTokenList(){
		return this.tokenList;
	}
	
	//이 노드가 들고있는 토큰 리스트 출력해주는 함수. 
	//실제 프로그램에서 쓸 일은 없음.
	public void printTokens(List<Token> tokenList){
		//토큰리스트 순회하는 Iterator 생성.
		Iterator<Token> tokenIterator = tokenList.iterator();
		while(tokenIterator.hasNext())
			System.out.println(tokenIterator.next().getContent());
	}
	
	
	//각 노드가 가지고 있는 정보를 출력
	//나중에 각 노드에서 오버라이딩 해서 사용. 
	//역시 실제 프로그램에서 쓸 일은 없음.
	public void printNodeInfo(){
		printTokens(this.tokenList);
	}
	
	
	
	//교수님 ppt에 있어서 만들긴 했는데 안쓸꺼같음.
	public Node create(String str){
		Node node = new Node();
		return node;
	}
	
	//나중에 비지터가 방문해야함. 아직 입력받는 클래스 못씀.
	@Override
	public String accept(MDElementVisitor visitor) {
		return null;
		// TODO Auto-generated method stub
		
	}

}
