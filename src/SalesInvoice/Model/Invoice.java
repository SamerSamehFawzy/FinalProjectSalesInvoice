/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SalesInvoice.Model;

import java.util.ArrayList;

/**
 *
 * @author Samer.Sameh
 */
public class Invoice {
    
     private int Number;
     private String Date;
     private String CustomerName;
     private ArrayList<Lines> lines;
     
    public Invoice() {
    }

    public Invoice(int number, String Date, String CustomerName) {
        this.Number = number;
        this.Date = Date;
        this.CustomerName = CustomerName;
    }
      
   
    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getDate() {
        return Date;
    }
    public void setNumber(int number) {
        this.Number = number;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public int getSonumber() {
        return Number;
    }

    public void setSonumber(int Sonumber) {
        this.Number = Sonumber;
    }

    
    public ArrayList<Lines> getLines() {
        if(lines ==null){
        lines =new ArrayList<>();
        }
        return lines;
    }
    
      public double getinvoiceCod (){
         double invoicePrice=0.0 ;
               for(Lines line: getLines()){
                  invoicePrice += line.getLineTotalPrice();
    
    }
        return invoicePrice;
    }
      
      public String  getAsCsv(){
 
      return Number + "," + Date + "," + CustomerName ;
      
      }
          public double getInvoiceTotal() {
        double total = 0.0;
        for (Lines line : getLines()) {
            total += line.getLineTotalPrice();
        }
        return total;
    }
}
