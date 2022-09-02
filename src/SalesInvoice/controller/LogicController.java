/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SalesInvoice.controller;

import SalesInvoice.Model.Invoice;
import SalesInvoice.Model.InvoiceTable;
import SalesInvoice.Model.Lines;
import SalesInvoice.Model.LinesTable;
import SalesInvoice.View.InvFrame;
import SalesInvoice.View.InvoiceData;
import SalesInvoice.View.ItemData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.Line;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Samer.Sameh
 */
public class LogicController implements ActionListener , ListSelectionListener{

    private InvFrame ui;   
    private InvoiceData newInvoiceData;
    private ItemData newItemData;
    
    public LogicController(InvFrame ui){
    this.ui =ui;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
    System.out.println("Action : " + actionCommand);
    
        switch (actionCommand) {
            case "":
                
                break;
                 case "load file":
                loadfile();
                break;
                
                 case "save":
                saveFile();
                break;
                
                 case "New Invoice":
                createInvoice();
                break;
                
                 case "Delete Invoice":
                deleteInvoice();
                break;
                
                 case "New Item":
                createItem();
                break;
                case "Delete Item":
                deleteItem();
                break;
                
                case "createInvoiceOK":
                    
                    createInvoiceOK();
                    break;
                case "createInvoiceCancel":
                    createInvoiceCancel();
                    break;
                    
                case "createOK":    
                    createLineOK();
                   
                    break;
                case "createLineCancel":  
                    createLineCancel();
                    break;
            default:
                throw new AssertionError();
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        
        int selectedInvoiceRow =ui.getInvoiceTable().getSelectedRow();
        
        if(selectedInvoiceRow != -1){
          System.out.println("row number "+ selectedInvoiceRow);
          Invoice selectedInvoice= ui.getInvoices().get(selectedInvoiceRow);
          ui.getSoLabel().setText(""+selectedInvoice.getSonumber());
          ui.getDateLabel().setText(selectedInvoice.getDate());
          ui.getNameLabel().setText(selectedInvoice.getCustomerName());
          ui.getTotalLabel().setText(""+selectedInvoice.getinvoiceCod());
          LinesTable linesTableModel= new LinesTable(selectedInvoice.getLines());

          ui.getLinesTable().setModel(linesTableModel);
          linesTableModel.fireTableDataChanged(); 
        }
    }
    
    private void loadfile() {
        JFileChooser fileChooser =new JFileChooser() ;
        try {
       int result = fileChooser.showOpenDialog(ui);
         if(result==JFileChooser.APPROVE_OPTION) {
           
             File headerFile=fileChooser.getSelectedFile();
            Path headerPath = Paths.get(headerFile.getAbsolutePath());
            List<String> headerLines = Files.readAllLines(headerPath);
            System.out.println("upload is Done");
            ArrayList<Invoice> invoices = new ArrayList<>();
            
            for( String headerLine :headerLines){
                 
              String[] parts = headerLine.split(",");
              int soNumber = Integer.parseInt(parts[0]);
              String invoiceDate = parts[1];
              String customerName= parts[2];
              
              Invoice invoice = new Invoice(soNumber, invoiceDate, customerName);
              invoices.add(invoice);
            }
            System.out.println("check"); 
            result= fileChooser.showOpenDialog(ui);
            if(result==JFileChooser.APPROVE_OPTION){
            File lineFile= fileChooser.getSelectedFile();
            Path linePath = Paths.get(lineFile.getAbsolutePath());
            List<String> lineLines =Files.readAllLines(linePath);
           
            System.out.println("upload is Done");
           
            for(String lineLine : lineLines){
                
                String lineParts [] =lineLine.split(",");
                 int soNumb = Integer.parseInt(lineParts[0]);
                 String itemname= lineParts[1];
                 double itemPrice= Double.parseDouble(lineParts[2]);
                 int quantity = Integer.parseInt(lineParts[3]);
                 
                 Invoice invo =null;
                 for(Invoice invoice :invoices){
                    if(invoice.getSonumber()==soNumb){
                        invo= invoice ;
                        break;
                    }
                 }
                 
                 Lines line = new Lines( itemname, itemPrice, quantity, invo);
                 
                 invo.getLines().add(line);
                 
                
              }
      
            }
             ui.setInvoices(invoices);
             InvoiceTable invoicesTableModel = new InvoiceTable(invoices);
             ui.setInvoicesTableModel(invoicesTableModel);
             ui.getInvoiceTable().setModel(invoicesTableModel);
             ui.getInvoicesTableModel().fireTableDataChanged();
         }
        } catch(IOException ex){
        
        ex.printStackTrace();
        }
   }
   
        private void saveFile() {
            JFileChooser fileChooser = new JFileChooser();
            ArrayList<Invoice> invoices = ui.getInvoices();
            ArrayList<Lines> saveLines = null;
           try {
            int result = fileChooser.showSaveDialog(ui);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fileChooser.getSelectedFile();
                FileWriter headerWriter = new FileWriter(headerFile);
            for (Invoice invoice :invoices ){
                String invoiceNumber = "" + invoice.getSonumber();
                String invoiceDate = invoice.getDate();
                String customerName = invoice.getCustomerName();
                
                headerWriter.write(invoiceNumber + "," + invoiceDate + "," + customerName + "\n");
                    headerWriter.flush();
                    }
                headerWriter.close();
                result = fileChooser.showSaveDialog(ui);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fileChooser.getSelectedFile();
                    FileWriter lineWriter = new FileWriter(lineFile);
                    for (Invoice saveInvoice : invoices) {
                        saveLines = saveInvoice.getLines();
                        for (Lines saveLine : saveLines) {
                            String invoiceNumber = "" + saveLine.getInvoice().getSonumber();
                            String itemName = saveLine.getProduct();
                            String itemPrice = "" + saveLine.getPrice();
                            String count = "" + saveLine.getQty();
                            lineWriter.write(invoiceNumber + "," + itemName + "," + itemPrice + "," + count + "\n");
                            lineWriter.flush();
                        }
                    }
                    lineWriter.close();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("Files Saved Successfully");
      
    }
               
   

    private void createInvoice() {
        newInvoiceData = new InvoiceData(ui);
        newInvoiceData.setVisible(true);
        
   }

    private void deleteInvoice() {
        int selectedInvoiceRow = ui.getInvoiceTable().getSelectedRow();
        if(selectedInvoiceRow != -1){
             ui.getInvoices().remove(selectedInvoiceRow);
             for (int i = 0; i < ui.getInvoices().size(); i++) {
                ui.getInvoices().get(i).setNumber(i + 1);
            }
             ui.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createItem() {
        newItemData = new ItemData(ui);
        newItemData.setVisible(true);
   }

    private void deleteItem() {
        int selectedinvoice =ui.getInvoiceTable().getSelectedRow();
        int selectedItemRow= ui.getLinesTable().getSelectedRow();
        if( selectedItemRow != -1){
             LinesTable lineTableModel = (LinesTable) ui.getLinesTable().getModel();
             lineTableModel.getLines().remove(selectedItemRow);
             lineTableModel.fireTableDataChanged();
             ui.getInvoicesTableModel().fireTableDataChanged();
             

        }
    } 

    private void createInvoiceOK() {
           String date = newInvoiceData.getinvoiceDateField().getText();
           String Customer= newInvoiceData.getcustomerNameField().getText();
           int number = ui.getNextInvoicenumber();
           
           Invoice invoice = new Invoice(number,date,Customer);
           ui.getInvoices().add(invoice);
           ui.getInvoicesTableModel().fireTableDataChanged();
           newInvoiceData.setVisible(false);
           newInvoiceData.dispose();
           newInvoiceData = null;
           
    }

    private void createInvoiceCancel() {
        newInvoiceData.setVisible(false);
        newInvoiceData.dispose();
        newInvoiceData =null;
        
    }

    private void createLineCancel() { 
         newItemData.setVisible(false);
         newItemData.dispose();
         newItemData = null;

    }

    private void createLineOK() {
        String itemName = newItemData.getItemNameField().getText();
        String quantityStr = newItemData.getItemQtyField().getText();
        String priceStr = newItemData.getItemPriceField().getText();
        
        int quantity = Integer.parseInt(quantityStr);
        double price =  Double.parseDouble(priceStr);
        
        int selectedInvoice = ui.getInvoiceTable().getSelectedRow();
        
        if(selectedInvoice!=-1){
        Invoice invoice = ui.getInvoices().get(selectedInvoice);
        
        Lines line = new Lines(itemName, price, quantity, invoice);
        invoice.getLines().add(line);
        LinesTable linesTableModel = (LinesTable) ui.getLinesTable().getModel();
        linesTableModel.fireTableDataChanged();
        ui.getInvoicesTableModel().fireTableDataChanged(); 
        ui.getTotalLabel().setText("" + invoice.getInvoiceTotal());
                
        }
        
        
        newItemData.setVisible(false);
        newItemData.dispose();
        newItemData = null;    }

    
    
}
