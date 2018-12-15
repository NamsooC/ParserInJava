

import java.util.Iterator;
import java.util.List;

public class Node implements MDElement{
	private List<Token> tokenList; 
	public Node(){}
	public Node(List<Token> tokenList){ setToken(tokenList); }
	
	//��ū ����Ʈ setter
	public void setToken(List<Token> token){
		this.tokenList = token;
	}
	//��ū ����Ʈ getter
	public List<Token> getTokenList(){
		return this.tokenList;
	}
	
	//�� ��尡 ����ִ� ��ū ����Ʈ ������ִ� �Լ�. 
	//���� ���α׷����� �� ���� ����.
	public void printTokens(List<Token> tokenList){
		//��ū����Ʈ ��ȸ�ϴ� Iterator ����.
		Iterator<Token> tokenIterator = tokenList.iterator();
		while(tokenIterator.hasNext())
			System.out.println(tokenIterator.next().getContent());
	}
	
	
	//�� ��尡 ������ �ִ� ������ ���
	//���߿� �� ��忡�� �������̵� �ؼ� ���. 
	//���� ���� ���α׷����� �� ���� ����.
	public void printNodeInfo(){
		printTokens(this.tokenList);
	}
	
	
	
	//������ ppt�� �־ ����� �ߴµ� �Ⱦ�������.
	public Node create(String str){
		Node node = new Node();
		return node;
	}
	
	//���߿� �����Ͱ� �湮�ؾ���. ���� �Է¹޴� Ŭ���� ����.
	@Override
	public String accept(MDElementVisitor visitor) {
		return null;
		// TODO Auto-generated method stub
		
	}

}
