
public interface MDElementVisitor {
	public String visit(N_Header header);
	public String visit(N_TextNode textnode);
	public String visit(N_Blockquote blockquote);
	public String visit(N_newLine newLine);
}
