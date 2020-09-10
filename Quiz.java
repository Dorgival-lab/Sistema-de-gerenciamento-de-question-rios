import java.io.*;
import java.lang.*;

class Quiz
{
	private InputStreamReader ir=new InputStreamReader(System.in);
	private BufferedReader br;
	private FileOutputStream fos;
	private	PrintStream ps; 
	private FileReader frs;
	
	//função para verificar a existência de um arquivo
	private boolean checkFile(String filename)
	{
		File myFile=new File(filename);
		boolean b=myFile.exists();	
		
		return b;
	}
	
// crie e salve perguntas no arquivo mestre
// retorna verdadeiro se bem-sucedido, caso contrário, retorna falso
	private boolean createQuestions()
	{
		int go=0,saved=0;
		boolean result=false,ret=false;
		
		String[] ques;
		String msg="",nos="";
		
		try
		{
			br=new BufferedReader(ir);
			while(result==false)
			{
				System.out.print("\n Por favor mencione não. de perguntas para armazenar");
				System.out.print("\n [Digite 0 para pular esta etapa] : ");
				nos=br.readLine();
				
				result=isNum(nos);
				if(result==false)
				{
					System.out.println("A entrada deve ser apenas um número inteiro positivo ...!");	
				}
				else
				{
					if(nos.equals("0"))
					{
						go=0;	
					}	
					else
					{
						go=1;	
					}
				}
			}
			
			if(go==0)
			{
				ret=true;	
			}
			else
			{
				for(int i=1;i<=Integer.parseInt(nos);i++)
				{
					saved=0;
					ques=new String[6];
					while(saved!=1)
					{
						msg="";
						br=new BufferedReader(ir);
						//question body
						System.out.print("\n Digite a pergunta nº "+i+" : ");
						ques[0]=br.readLine();
						//choice 1
						System.out.print("\n Opção 1 : ");
						ques[1]=br.readLine();
						//choice 2
						System.out.print("\n Opção 2 : ");
						ques[2]=br.readLine();
						//choice 3
						System.out.print("\n Opção 3 : ");
						ques[3]=br.readLine();
						//choice 4
						System.out.print("\n Opção 4 : ");
						ques[4]=br.readLine();
						//correct answer
						System.out.print("\n Resposta correta (mencione apenas a opção nº): ");
						ques[5]=br.readLine();
						
						//validating input
						if(ques[0].equals(""))
						{
							msg="O corpo da pergunta não deve ficar em branco ...!\n";	
						}
						for(int j=1;j<=4;j++)
						{
							if(ques[j].equals(""))
							{
								msg=msg+"Opção "+j+" deveria ter sido mencionado ...!\n";	
							}	
						}
						if(isNum(ques[5])==false)
						{
							msg=msg+"A entrada para a resposta correta deve ser um número inteiro ...!\n";	
						}
						else
						{
							if(Integer.parseInt(ques[5])>=1 && Integer.parseInt(ques[5])<=4)
							{
								// não fazer nada, a entrada para a resposta correta está ok
							}
							else
							{
								msg=msg+"Indicação de resposta inválida não. Deve estar entre 1 e 4 ...!";	
							}	
						}
						
						// dados violados de acordo com a regra, então exiba os erros
						if(!msg.equals(""))
						{
							System.out.print("\n Os seguintes erros ocorreram :-");
							System.out.print("\n --------------------------------------------");
							System.out.print("\n " + msg);
							saved=0;	
						}
						else // salve a pergunta e prossiga
						{
							try
							{
								fos=new FileOutputStream("Questions.txt",true);
								ps=new PrintStream(fos);
								
								ps.println(ques[0]+","+ques[1]+","+ques[2]+","+ques[3]+","+ques[4]+","+ques[5]);
								fos.close();
								System.out.println("Salvo com sucesso...");
								saved=1;
							}
							catch(Exception rx)
							{
								System.out.println("Erro ao aceitar detalhes. Por favor, tente novamente...!");
								saved=0;
							}
						}
					}
					ret=true;	
				}	
			}
		}
		catch(Exception ex)
		{
			ret=false;
		}

		return ret;
	}
	
	// função para verificar se a entrada é um número inteiro válido ou não
	private boolean isNum(String input)
	{
		boolean b=false;
		
		char a[]=new char[input.length()];
		a=input.toCharArray();
		
		for(int i=0;i<a.length;i++)
		{
		  if(a[i]>='0' && a[i]<='9')
		  {
		    b=true;	  
		  }	
		  else
		  {
		    b=false;
		    break;	  
		  }
		}
		
		return b;
	}	
	
// iniciar o módulo do concurso de teste
// retorna 0 se confirmado com sucesso, caso contrário, qualquer um dos seguintes: -
// 1 - o arquivo mestre não foi localizado
// 2 - o arquivo mestre está localizado com fluxo vazio
// 3 - qualquer outra exceção de tempo de execução
	private int startQuiz() //throws IOException
	{
		int retVal=0,counter=0,commit=0,nos=0;
		int attempted=0,skipped=0,correct=0,wrong=0;
		boolean made=false;
		
		String thisLine,summary="";
		String[] fullText;
		String[] question;
		
		if(checkFile("Questions.txt")==false)
		{
			retVal=1;	
		}
		else
		{
			try
			{
				frs=new FileReader("Questions.txt");
				br=new BufferedReader(frs);
				while((thisLine=br.readLine()) != null) // contando nº total de entradas
				{
					counter+=1;
					nos+=1;
				}
				
				frs.close();
				
				if(counter==0)  // arquivo localizado com fluxo vazio, então dê ao usuário a opção de criar um novo arquivo
				{
					retVal=2;	
				}
				else
				{
					frs=new FileReader("Questions.txt");
					br=new BufferedReader(frs);
					fullText=new String[counter];
					counter=0;
					while((thisLine=br.readLine()) != null) // enumerando por meio do arquivo mestre e buscando todas as entradas
					{
						fullText[counter]=thisLine;
						counter+=1;
					}
					frs.close();
					
					
					// exibindo perguntas ao usuário e respectivas ações
					for(int i=0;i<fullText.length;i++)
					{
						question=new String[6];
						question=fullText[i].split(",");
						commit=0;
						made=false;
						
						br=new BufferedReader(ir);
						
						while(commit!=1)
						{
							try
							{
								System.out.print("\n Question No. "+(i+1));
								System.out.print("\n ------------------");
								System.out.print("\n "+question[0]);
								System.out.print("\n Option 1 -->"+question[1]);
								System.out.print("\n Option 2 -->"+question[2]);
								System.out.print("\n Option 3 -->"+question[3]);
								System.out.print("\n Option 4 -->"+question[4]);
								while(made!=true)			
								{
									System.out.print("\n SUA RESPOSTA [1-4] (digite 0 para pular): ");
									String opt=br.readLine();
									if(isNum(opt)==false)
									{
										System.out.println("Entrada inválida. Sua escolha deve ser apenas um número inteiro ...! ");
										made=false;	
									}
									else
									{
										if(Integer.parseInt(opt)>=1 && Integer.parseInt(opt)<=4)
										{
											attempted+=1;
											if(Integer.parseInt(opt)==Integer.parseInt(question[5]))
											{
												correct+=1;	
											}
											else
											{
												if(summary.equals(""))
												{
													summary="Questão : "+question[0]+"\n";
													summary=summary + "Resposta correta : "+question[5]+" "+question[Integer.parseInt(question[5])]+"\n";
													summary=summary + "Sua resposta foi : "+opt+" "+question[Integer.parseInt(opt)];
												}
												else
												{
													summary=summary + "\n\n Questão : "+question[0]+"\n";
													summary=summary + "Resposta correta : "+question[5]+" "+question[Integer.parseInt(question[5])]+"\n";
													summary=summary + "Sua resposta foi : "+opt+" "+question[Integer.parseInt(opt)];
														
												}
												
												wrong+=1;
											}
											made=true;
										}
										else
										{
											if(Integer.parseInt(opt)==0)  //pergunta foi pulada pelo participante
											{
												skipped+=1;
												made=true;
											}
											else
											{
												System.out.println("Escolha incorreta. Por favor, faça entre 1 e 4 apenas ...!");
												made=false;	
											}	
										}
									}
								}
							}
							catch(Exception ex)
							{
								retVal=3;	
							}
							commit=1;
						}
					}
					
					if(retVal==0)
					{
						System.out.println("\n RESULTADO DO CONCURSO");
						System.out.println("\n ------------------------------");
						System.out.println("\n Nº de perguntas apresentadas = "+nos);
						System.out.println("\n Nº de perguntas tentadas = "+attempted);
						System.out.println("\n Nº de perguntas ignoradas = "+skipped); 		
						System.out.println("\n Respostas corretas = "+correct);
						System.out.println("\n Respostas erradas = "+wrong);
						if(attempted!=0)
						{
							System.out.println("\n Porcentagem de precisão = "+(correct*100)/attempted+"%\n");
						}
						else
						{
							System.out.println("\n Porcentagem de precisão = não calculado\n");	
						}
						
						if(!summary.equals(""))
						{
							System.out.println("\n\n Resposta (s) correta (s) para as perguntas mal respondidas :-\n");
							System.out.print("--------------------------------------------------------\n");
							System.out.print(summary+"\n");	
						}
						System.out.print("--------------------------------------------------------\n\n");
					}
				}
			}
			catch(Exception mx)
			{
				retVal=3;	
			}
		}
		
		return retVal;
	}
	
	public static void main(String args[])
	{
		Quiz z = new Quiz();
		
		int ch=0,allow=0;
		boolean go=false;
		String uname="",passwd="",msg="";
		
		InputStreamReader ir=new InputStreamReader(System.in);
		BufferedReader br=new BufferedReader(ir);
		
		while(ch!=3)
		{
			try
			{
				System.out.print("\n APLICAÇÃO DE QUIZ");
				System.out.print("\n ----------------");	
				System.out.print("\n 1. Crie perguntas");
				System.out.print("\n 2. Iniciar concurso de questionário");
				System.out.print("\n 3. Sair da aplicação");
				System.out.print("\n Por favor, insira uma escolha [1-3]:");
				
				ch=Integer.parseInt(br.readLine());
				if(ch>=1 && ch<=3)
				{
					switch(ch)
					{
						case 1:  // modifique o arquivo mestre para criar novas perguntas para o questionário
							if(z.checkFile("Questions.txt")==true)
							{
								System.out.print("\n O arquivo mestre já existe.");
								System.out.print("\n Você deseja alterá-lo[y/n]:");
								String s=br.readLine();
								
								if(s.equals("y") || s.equals("Y"))
								{
									go=true;	
								}
								else
								{
									go=false;	
								}
							}
							else
							{
								go=true;	
							}
							
							if(go==true)
							{
								allow=0;
								int t=1;
								
								while(allow==0 && t<=3)
								{
									System.out.print("\n Mencione o nome do usuário :");
									uname=br.readLine();
									System.out.print("\n Mencione a senha :");
									passwd=br.readLine();
									
									if(uname.equals("admin") && passwd.equals("admin"))
									{
										allow=1;										
									}
									else
									{
										if(t<3)
										{
											System.out.println("Usuário ou senha incorretos. Por favor, tente novamente...!");
											System.out.println("Tentativa = "+t+"/3");
											allow=0;	
											t+=1;
										}
										else
										{
											System.out.println("Desculpe, sua tentativa acabou. O sistema voltará ao prompt de opção ...");
											allow=2;	
										}
									}
								}
								
								if(allow==1) //permitir criar perguntas
								{
									boolean res=z.createQuestions();
									if(res==false)
									{
										System.out.println("Incapaz de continuar a execução. Por favor, tente novamente...!");	
									}
								}
								else
								{
									ch=0;	
								}
							}
							else
							{
								ch=0;	
							}
							break;		
						case 2:  //rotina principal do concurso de questionário
							int r=z.startQuiz();
							switch(r)
							{
								case 0:
									msg="";
									break;
								case 1:
									msg="\nNão foi possível iniciar o questionário. Não foi possível localizar o arquivo mestre ...!\n";
									msg=msg+"Certifique-se de que o arquivo \"Questions.txt\" existe no ";
									msg=msg+"caminho de aplicação.\n";
									msg=msg+"Se você não conseguir localizar o arquivo, clique em OPÇÃO 1 e ";
									msg=msg+"crie um novo arquivo.";
									break;
								case 2:
									msg="\nNão foi possível iniciar o questionário. Não foi possível localizar entradas no arquivo mestre ...!\n";
									msg=msg+"Certifique-se de que o arquivo mestre não está vazio e é válido em ";
									msg=msg+"o caminho do aplicativo. Caso contrário, clique na OPÇÃO 1 e crie um novo arquivo.";
									break;	
								case 3:
									msg="Desculpe, o programa foi interrompido devido a uma exceção no processo ...!";
									break;	
							}
							
							if(!msg.equals(""))
							{
								System.out.println(msg);	
							}
							break;
						case 3:  //exit the program
							System.out.println("Obrigado por usar este programa ...");
							System.out.println("Adeus...");	
					}
				}
				else
				{
					System.out.println("Opção inválida. Por favor, tente novamente...");
					ch=0;	
				}
			}	
			catch(Exception ex)
			{
				System.out.println("Erro ao aceitar entrada ...!");
				ch=0;	
			}		
		}
	}
}