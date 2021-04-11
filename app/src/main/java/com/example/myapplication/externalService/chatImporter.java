package com.example.myapplication.externalService;


import android.content.Context;

import java.util.Scanner;
import java.io.*;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class chatImporter {
//ArrayList<HashMap<String,String>>
    public static ArrayList<HashMap<String,String>> getData(String path) {
        // TODO Auto-generated method stub

        File f = new File(path);
        ArrayList<HashMap<String,String>> parsedData = new ArrayList<HashMap<String,String>>();


        final String regex = "([0-2][1-9]|[3][0-1])\\/([1][0-2]|[0][1-9])\\/([0-9][0-9][0-9][0-9],)";
        final String regex_new = "(([0-2][0-9])|([3][0-1]))/(([0][1-9])|([1][0-2]))/([0-9][0-9][0-9][0-9],)";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

        try {
            Scanner s = new Scanner(f);
            String data = "";
            String myline ="";
            // while(s.hasNextLine() {
            // 	//System.out.println(s.nextLine());
            // 	myline = s.nextLine();

            // 	if(myline.contains("\r\n")){
            // 		myline.replace("\r\n", "~\n");
            // 		System.out.println(myline);
            // 	}

            // 	data += myline;
            // }

            FileReader fr=new FileReader(f);   //Creation of File Reader object
            BufferedReader br=new BufferedReader(fr);  //Creation of BufferedReader object
            int c = 0;
            while((c = br.read()) != -1)         //Read char by Char
            {
                if((char) c == '\n'){
                    data+=myline;
                    myline = "";
                    continue;
                }
                myline+= (char) c;         //converting integer to char
                //System.out.println(character);        //Display the Character
            }

            //System.out.println(data);
            // String [] d = data.split(date_regex);

            final Matcher matcher = pattern.matcher(data);
            ArrayList<String> lines= new ArrayList<String>();int j=0;

            int count = 0;
            while (matcher.find()) {
                count++;
            }
            int [] next = new int[count];
            System.out.println("number of messages: "+ count);
            matcher.reset();
            while (matcher.find()) {

                //System.out.println("Full match: " + matcher.group(0));
                //System.out.println(matcher.start());
                next[j]=matcher.start();

                //System.out.println(data.charAt(matcher.end()-1));
//			    int prev;
//			    for (int i =0; i < 18;i++) {
//
//
//			    	if (j==0) {
//			    		lines.add((String) data.subSequence(0, matcher.end()));
//			    		prev = matcher.start();
//			    	}
//			    	else {
//			    		lines.add((String) data.subSequence(matcher.end(), matcher.end()));
//			    	}
//			    }
                j++;


            }
            //System.out.println();

            // for (int i =0; i<next.length; i++) {
            // 	if (i==0) {
            // 		lines.add((String) data.subSequence(0, next[1]));
            // 	}
            // 	else if ((i+1)!=next.length){
            // 		lines.add((String) data.subSequence(next[i], next[i+1]));
            // 	}
            // 	else if((i+1)>=next.length) {
            // 		//int id = data.indexOf('^');
            // 		//System.out.println(id);
            // 		lines.add((String) data.subSequence(next[i], data.length()));
            // 	}

            // }

            for (int i =0; i<next.length; i++) {
                if (i==0) {
                    lines.add((String) data.subSequence(0, next[1]));
                }
                else if ((i+1)!=next.length){
                    lines.add((String) data.subSequence(next[i], next[i+1]));
                }
                else if((i+1)>=next.length) {
                    //int id = data.indexOf('^');
                    //System.out.println(id);
                    lines.add((String) data.subSequence(next[i], data.length()));
                }

            }


            //System.out.println(lines);
            HashMap<String,String> line ;

            for (String i : lines)
            {
                //System.out.println(i);
                line = new HashMap<String,String>();
                line.put("date",(String) i.subSequence(0, i.indexOf(",")));
                // System.out.println(i.indexOf(","));
                // System.out.println(i.indexOf("-"));
                // System.out.println(i.indexOf(":",15));
                // System.out.println(i.charAt(i.length()-1));
                line.put("time",(String) i.subSequence(i.indexOf(",")+1, i.indexOf("-")));
                line.put("sender",(String) i.subSequence(i.indexOf("-")+1, i.indexOf(":",i.indexOf("-"))));
                line.put("message",(String) i.subSequence(i.indexOf(":",i.indexOf("-"))+1, i.length()));


                parsedData.add(line);

                //System.out.println();
            }



            String myJson = "[";
            //myJson+="\n";

            for(HashMap<String,String> i : parsedData) {
                myJson+="\n";
                myJson+="{"+"\n";
                myJson+="\"date\":\""+ i.get("date")+"\""+","+"\n";
                myJson+="\"sender\":\""+ i.get("sender")+"\""+","+"\n";
                myJson+="\"time\":\""+ i.get("time")+"\""+","+"\n";
                //System.out.println(i.get("message"));
                //if (i.get("message").contains("\r")){
                // String x = i.get("message");
                // //x.replaceAll("^ +| +$|( )+", "$1");
                // int countSpace = 0;
                // char[] chars = new char[x.length()];
                // chars = x.toCharArray();
                // int ascii;
                // Boolean check= false;
                // System.out.println("Entering Loop");
                // for (char ch : chars){
                // 	ascii = (int) ch;
                // 	if (ascii == 13){
                // 		ch = '~';
                // 		check = true;
                // 	}

                // 	System.out.print(ch + "-");

                // 	if(check && ascii == 10){
                // 		ch = ' ';
                // 		check = false;
                // 	}



                // }
                // System.out.println(chars.toString());
                // x = chars.toString();
                // //x.replace("\r\n","~");
                // i.put("message",x);
                //}


                String x = i.get("message");
                //System.out.println(x);
                //x.replaceAll("^ +| +$|( )+", "$1");
                // char[] chars = new char[x.length()];
                // chars = x.toCharArray();
                int ascii;
                Boolean check= false;
                char ch;
                //System.out.println("Entering Loop");
                StringBuilder build = new StringBuilder(x);

                for (int k=0;k<x.length();k++){
                    ch = x.charAt(k);
                    ascii = (int) ch;
                    if (ascii == 13){
                        //ch = '~';
                        System.out.println("Found");
                        build.setCharAt(k, '~');
                        check = true;
                    }

                    //System.out.print(ch + "-");

                    if(check && ascii == 10){
                        //ch = ' ';
                        build.setCharAt(k, ' ');
                        check = false;
                    }

                    //chars[k]= ch;


                }
                //System.out.println(chars.toString());
                //x = chars.toString();
                //x.replace("\r\n","~");
                i.put("message",x);



                // String x = i.get("message");
                // 	char[] chars = new char[x.length()];
                // 	chars = x.toCharArray();
                // 	for (char ch : chars){
                // 		System.out.print(ch+ "-");
                // 	}
                // 	System.out.println("\n");
                myJson+="\"message\":\""+ x.toString()+"\""+"\n"+"}";
                myJson+=",";
            }

            myJson = myJson.substring(0, myJson.length()-1);

            myJson+="\n"+"]";

            //System.out.println(myJson);




            // for(HashMap<String,String> i : parsedData){
            // 	System.out.println(i
            // 	+ "\n");
            // }
//			int fs = 1;
//			while (true) {
//				System.out.println(lines);
//				fs++;
//			}

        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("File Closed with Exception.");
        }
        catch(IOException e1){
            e1.printStackTrace();
        }
        //f.close();
        return parsedData;
    }


}


