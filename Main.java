import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyUtils;

public class Main {
	public static void main(String[] args) {
		String option = new String();	//option ���� (Plain, Fancy, Slide ��)
		ArrayList<String> inputFile = new ArrayList<String>();		//.md �����̸� ����Ʈ
		ArrayList<String> outputFile = new ArrayList<String>();		//.html �����̸� ����Ʈ
		
		//Command Line �Է� ���� Ȯ��
		checkCLI(args, inputFile, outputFile, option);
		
		//Parser ��ü ����
		MDParser mdParser = new MDParser();
		
		//���� �б�/���� �غ�
		File fin = new File("src/"+inputFile.get(0));
		File fout = new File("src/"+outputFile.get(0));
		FileReader fr = null;
		FileWriter fw = null;
		BufferedReader in = null;
		BufferedWriter out = null;
		
		try {
			fr = new FileReader(fin);
			in = new BufferedReader(fr);
			fw = new FileWriter(fout);
			out = new BufferedWriter(fw);
			
			String temp=new String();
			
			//.md���Ͽ��� temp�� ���پ� �о �� ���ڿ��� ó��
			while((temp = in.readLine()) != null){
				Node tempNode = new Node();
				tempNode.setToken(mdParser.tokenize(temp));
				mdParser.addNodeToList(tempNode, mdParser.nodeList);
			}
			
			
			//� ������ �ִ��� Ȯ�ο�. �������� ���� �ʿ� ����.
			for(int i=0;i<mdParser.nodeList.size();i++){
				mdParser.nodeList.get(i).printNodeInfo();
			}

			//Visitor �������� plain ��Ÿ�� html ����
			PlainVisitor plainvisitor = new PlainVisitor();
			out.write(plainvisitor.startHtml());
			for(int i=0;i<mdParser.nodeList.size();i++){
				System.out.println(mdParser.nodeList.get(i).accept(plainvisitor));
				out.write(mdParser.nodeList.get(i).accept(plainvisitor));
			}
			out.write(plainvisitor.endHtml());
			
			//���ϴݱ�
			in.close();
			out.close();
			fr.close();
			fw.close();
			
		} catch (IOException e) {e.printStackTrace();}
	
		
		//JTidy �� html �˻�
		HtmlValidator jtidy = new HtmlValidator();
		jtidy.checkHtml(outputFile.get(0));
		
		
		
	}


	//========= -help �Է� �� ��� =================
	public static void printHelp(){
		System.out.println("----------------------------------------------------------------");
		System.out.println("	command line format : java CLI_main -input md_file_name.md -output html_file_name.html -option option_command");
		System.out.println("	option command : plain / fancy / slide\n");
		System.out.println("	you can omit -option command");
		System.out.println("	you can input several md files");
		System.out.println("	you can output several html files\n");
		System.out.println("	But, You must enter the same number of md files and html files \n");
		System.out.println("	you must input md files to same directory of CLI_mian.class file.");
		System.out.println("	html files are created to directory of CLI_mian.class file.\n");
		System.out.println("	this CLI is not support overriding html files");
		System.out.println("----------------------------------------------------------------");
	}

	//CLI �Է� Ȯ��
	public static void checkCLI(String[] args, ArrayList<String> inputFile, ArrayList<String> outputFile, String option){
		int index_input = -1;
		int index_output = -1;
		int index_option = -1;
		int input_count = 0;
		int output_count = 0;

		
		
		if(args.length == 0){
			System.out.println("No argument");
			System.exit(0);
		}		
		//1. args0 �� help => help page ���
		else if(args[0].equals("-help") || args[0].equals("-HELP"))
			printHelp();
		//2. help�� �ƴ϶��, input, output, option�� ��ġ ���
		else{
			int i=0;
			while(i<args.length){
				if(args[i].equals("-input") || args[i].equals("-INPUT"))
					index_input = i;
				else if(args[i].equals("-output") || args[i].equals("-OUTPUT"))
					index_output = i;
				else if(args[i].equals("-option") || args[i].equals("-OPTION"))
					index_output = i;
				i++;
			}
			//input, output�� �Ƚ�ų� input, output, option ������ Ʋ�� ���
			if(index_input == -1 || index_output == -1 ||
				index_input>index_output || index_option>index_output ||
				index_output - index_input == 1){
				System.out.println("Wrong command. Check -help for commnad line syntax");
				System.exit(0);
			}
			//input�� output ������ ���� Ȯ��
			input_count = index_output - index_input;
			if(index_option != -1)
				output_count = index_option - index_output;
			else
				output_count = args.length - index_output;
			
			if(input_count != output_count || input_count <= 0 || output_count <= 0){
				System.out.println("Number of input files and output files doesn't match");
				System.exit(0);
			}
			
			//option ������ option�� ����
			if(index_option	== -1)
				option = "plain";
			else if(!args[index_option+1].equals("plain") ||!args[index_option+1].equals("fancy") ||!args[index_option+1].equals("slide")){
				System.out.println("Wrong option");
				System.exit(0);
			}else
				option = args[index_option+1];

			//inputFile����Ʈ�� input �����̸� ����, outputFile����Ʈ�� output �����̸� ����
			for(int k=1;k<index_output-index_input;k++){
				inputFile.add(args[index_input+k]);
				outputFile.add(args[index_output+k]);
			}
		}
		
		
		//Ȯ���� Ȯ��
		if(checkExtension(inputFile, "in")==false || checkExtension(outputFile, "out") == false)
			System.exit(0);
		
		
		
		//Input���� ���翩�� Ȯ��
		for(int i=0;i<inputFile.size();i++){
			File file = new File("src/"+inputFile.get(i));
			if(file.exists()==false){
				System.out.println("No input file. Check file name");
				System.exit(0);
			}
		}
		
		//Output���� Ȯ��
		for(int i=0;i<outputFile.size();i++){
			File file = new File("src/"+outputFile.get(i));
			if(file.isFile() == true){
				System.out.println("There already exists file: "+outputFile.get(i));
				System.out.print("Overwrite? (y/n)");
				InputStreamReader ir = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(ir);
				String newFile;
				char yn;
				try {
					yn = 'y'; System.out.println();//�� �� ���߿� �����.
					//yn = br.readLine().charAt(0);
					if(yn == 'n' || yn == 'N'){
						while(true){
							System.out.print("Enter new output filename : ");
							newFile = br.readLine();
							if(newFile.endsWith(".html")==false)
								System.out.println("Wrong filename");
							else{
								outputFile.add(i,newFile);
								outputFile.remove(i+1);
								i--;
								break;								
							}
						}
					}else{
						//overwrite
					}
				} catch (IOException e) {e.printStackTrace();}	
			}
		}
	
	}
	
	
	//.md, .html Ȯ���� Ȯ���ϴ� �޼ҵ�
	public static boolean checkExtension(ArrayList<String> list, String type){
		for(int i=0;i<list.size();i++){
			if(type.equals("in") && list.get(i).endsWith(".md")==false){
				System.out.println("Wrong extension. \nUse .md for Input");
				return false;
			}else if(type.equals("out") && list.get(i).endsWith(".html")==false){
				System.out.println("Wrong extension. \nUse .html for Output");
				return false;
			}
		}
		return true;
	}

}
