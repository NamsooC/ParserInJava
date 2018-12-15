/*
 * 각 토큰에 필요한 정보는 일단 문자열로 된 content이고,
 * 그 content를 추가하거나 보는 것은 getter/setter 사용
 */

public class Token {
	private String content;
	public void setContent(String str){
		this.content = str;
	}
	public String getContent(){
		return this.content;
	}


	
}
