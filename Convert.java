import java.lang.StringBuilder;
import java.lang.Double;

public class Convert{

 
   public static double convert(String temperature, char scale){
      // converts a temperature (value/scale) to a new scale
      
      double x=1.8;
      char isolateletter= temperature.charAt(temperature.length()-1);
      double isolatenum= Double.parseDouble(temperature.substring(0, temperature.length()-1));
      double returnval;
      
      //starting with C-F conversion
      if ((scale=='F')||(scale=='f'))
      {
         //conversion for temp starting in Farenheit already 
         if(((isolateletter)=='F')||((isolateletter)=='f'))
         {
          return isolatenum;
         }
         //conversion for temp starting in Celsius
         else if (((isolateletter)=='C')||((isolateletter)=='c'))
         {
          returnval= ((isolatenum*x )+32);
          if(returnval>= -459.67)
          {
          return returnval;
          }
          else {return-459.67;}
         }
      }
      //then F-C conversion
      if ((scale=='C')||(scale=='c'))
      {
         //conversion for temp starting in Celsius already
         if(((isolateletter)=='C')||((isolateletter)=='c'))
         {
          return isolatenum;
         }
         //conversion for temp starting in Farenheit
         else if (((isolateletter)=='F')||((isolateletter)=='f'))
         {
          returnval= ((isolatenum-32)/x);
          if(returnval<-273.15)
          {
            return -273.15;
          }
          else { return returnval;}
         }
      }
       
      return x;
   }


   public static void main(String[] args){
      System.out.println("Testing Convert.convert");
      String in = "-195.79C";
      char scale = 'F';
      double expect = -320.42199999997;
      double out = Convert.convert(in, scale);
      System.out.println("convert(\"" + in + "\",\'" + scale + "\')");
      System.out.println("...expect : " + expect);
      System.out.println("...actual : " + out);
      
      in = "10.0C";
      scale = 'C';
      expect = 10.0;
      out = Convert.convert(in, scale);
      System.out.println("convert(\"" + in + "\",\'" + scale + "\')");
      System.out.println("...expect : " + expect);
      System.out.println("...actual : " + out);
      
   }
}