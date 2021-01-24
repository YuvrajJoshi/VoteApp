/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voting.application;

/**
 *
 * @author Admin
 */
public class UniquePassword {
     
    public static long Code() //this code returns the  unique 16 digit code  
{  //creating a 16 digit code using Math.random function 
    long code   =(long)((Math.random()*9*Math.pow(10,15))+Math.pow(10,15)); 
    return code; //returning the code 
} 
    
    public String generatePassowrd(){
        long code=Code();//function calling 
    String unique_password=""; 
    for (long i=code;i!=0;i/=100)//a loop extracting 2 digits from the code  
        { 
            long digit=i%100;//extracting two digits 
            if (digit<=90) 
            digit=digit+32;  
            //converting those two digits(ascii value) to its character value 
            char ch=(char) digit; 
            // adding 32 so that our least value be a valid character  
            unique_password=ch+unique_password;//adding the character to the string 
        } 
        return unique_password; 
        } 
    }    
