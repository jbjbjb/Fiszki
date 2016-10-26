/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fiszki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 *
 * @author Justyna
 */
public class FXMLDocumentController implements Initializable {
    

    @FXML
    public AnchorPane run, data,main,start;
    @FXML
    public TextField question, answer,dataPl,dataEn;
    @FXML
    public Label lGood,lWrong,lNotrun,result;
    @FXML public TableView<Slowo> table;
    @FXML public TableColumn<Slowo, String> plValue;
    @FXML public TableColumn<Slowo, String> enValue;
    
    public int good=0;
    public int wrong=0;
    public int all=0;
    public int other=all;
    private int index;
    private ObservableList<Slowo> slowa;
    public int[] wyniki;
    Slowo s;
    
    @FXML
    private void handleButtonRestart(){
        init();
        result.setText("");
        good=0;
        wrong=0;
        nextWord();
    }
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
     
    }
    @FXML
    private void handleButtonStart(ActionEvent event){
        
        run.setVisible(true);
        start.setVisible(false);
        nextWord();
         
    
    }
       @FXML
    private void handleSelected(MouseEvent event) {
    ObservableList<Slowo> s = table.getItems();
        try{
            int ind = table.getSelectionModel().getSelectedIndex();
            Slowo p =s.get(ind);
            dataPl.setText(p.getPl());
            dataEn.setText(p.getEn());
           
        }catch(ArrayIndexOutOfBoundsException e){
            dataPl.setText("Nie wybrano wiersza");
        }
    }
    @FXML
    private void handleButtonData(ActionEvent event){
        start.setVisible(false);
        data.setVisible(true);
        table.getItems().addAll(readDB());
    }
    private void nextWord(){
        s=drow();
        question.setText(s.getPl());
        answer.setText("");
        lGood.setText(good+"");
        lWrong.setText(wrong+"");
        other=all-good-wrong-1;
        lNotrun.setText(other+"/"+all);
    }
    @FXML
    private void handleButtonNext(ActionEvent event){
        System.out.println("Wyniki:");
        for(int i=0;i<wyniki.length;i++){
            System.out.print(wyniki[i]);
        }
        String ans=answer.getText();
        if(s.getEn().equalsIgnoreCase(ans)){
            result.setText("DOBRZE");
            good++;
            wyniki[index]=1;
        }
        else{
              result.setText("ŹLE. Odpowiedź:"+s.getEn());
            wrong++;
        }
        nextWord();
        
    }
     @FXML
    private void handleUpdate(ActionEvent event) {
        ObservableList<Slowo> t=table.getItems();
     try{
            int ind = table.getSelectionModel().getSelectedIndex();
            Slowo p=new Slowo(dataPl.getText(),dataEn.getText());
        t.set(ind,p);
        }catch(ArrayIndexOutOfBoundsException e){
            dataPl.setText("Nie wybrano wiersza do edycji.");
        }
    }
        @FXML
    private void handleAdd(ActionEvent event) {
        ObservableList<Slowo> t=table.getItems();
     
        Slowo p=new Slowo(dataPl.getText(),dataEn.getText());
        t.add(p);
    }
    @FXML
    private void handleDelete(ActionEvent event) {
    ObservableList<Slowo> t = table.getItems();
        try{
            int ind = table.getSelectionModel().getSelectedIndex();
            t.remove(ind);
        }catch(ArrayIndexOutOfBoundsException e){
            dataPl.setText("Nie wybrano wiersza do usunięcia.");
        }
    }
    @FXML
    private void handleSave(ActionEvent event){
        ObservableList<Slowo> t = table.getItems();
        String line="";
        String split=";";
        BufferedWriter bw=null;
       
        FileWriter fw;
        File f=new File ("db.txt");
        try {
            f.createNewFile();
        } catch (IOException ex) {
            System.out.println("Nie udało się zapisać");
        }
        try {
            fw=new FileWriter(f);
            bw=new BufferedWriter(fw);
            for(int i=0;i<t.size();i++){
                Slowo p=t.get(i);
                line=p.getPl()+split+p.getEn();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Cannot save file");
        }
       finally {
		if (bw != null) {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        t.clear();
        data.setVisible(false);
        start.setVisible(true);
    }
@FXML
public void handleButtonClear (ActionEvent event){
    ObservableList<Slowo> tmp = table.getItems();
    tmp.clear();
    table.setItems(tmp);
}
  @FXML
    private void handleCancel(ActionEvent event){   
        ObservableList<Slowo> tmp = table.getItems();
        tmp.clear();
        
        data.setVisible(false);
        start.setVisible(true);
    }
    private Slowo drow(){
        ObservableList<Slowo> l=readDB();
        all=l.size();
        int f=0;
        Random r =new Random();  
        do{
        index=r.nextInt(all);
        System.out.println(f);
        f++;
        }while(wyniki[index]!=0&&f<(3*all));  // dopóki słowo nie było nieodgadnięte ( 
        if(f==3*all){
            end();
        }
        Slowo s=l.get(index); 
        return s;
    }
    @FXML
    public void end(){
        result.setText("KONIEC");
    }
    @FXML
    public void handleButtonFile(ActionEvent event){
        String line;
        String sep=";";
        Slowo s;
        slowa = table.getItems();
        FileChooser fileChooser = new FileChooser();
FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
fileChooser.getExtensionFilters().add(extFilter);
File file = fileChooser.showOpenDialog(null);
try {   
            BufferedReader bf = new BufferedReader(new FileReader(file));
            while (((line = bf.readLine()) != null)){
                String[] d=line.split(sep);
               System.out.println(d[1]);
               s=new Slowo(d[0],d[1]);
               slowa.add(s);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("Cannot read file");
        }
    }
    
    public ObservableList<Slowo> readDB() {
        String line;
        String sep=";";
        Slowo s;
        slowa = FXCollections.observableArrayList();
        try {   
            BufferedReader bf = new BufferedReader(new FileReader("db.txt"));
            while (((line = bf.readLine()) != null)){
                String[] d=line.split(sep);
               System.out.println(d[1]);
               s=new Slowo(d[0],d[1]);
               slowa.add(s);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("Cannot read file");
        }
  
                return slowa;
    }
    public void init(){
        wyniki=new int[100];
        for(int i=0;i<wyniki.length;i++){
        wyniki[i]=0;}
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        
  // TODO
    }
    }
    
