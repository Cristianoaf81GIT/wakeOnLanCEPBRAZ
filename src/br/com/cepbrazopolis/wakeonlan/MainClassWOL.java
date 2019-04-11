/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cepbrazopolis.wakeonlan;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author cristianoaf81
 */
public class MainClassWOL {
    public static void main(String[] args){
        try {
            WindowsClass window = new WindowsClass();
            window.start(new Stage());
            try{
                Thread.sleep(1000);
            }catch(Exception e){
                
            }
        } catch (Exception WindowExp) {
            Alert WindowStartError = new Alert(Alert.AlertType.ERROR
                    , "", ButtonType.OK);
            WindowStartError.setTitle("Falha de inicialização!");
            WindowStartError.setHeaderText(
                "Desculpe houve um erro ao iniciar o programa\n"
                +"informe os detalhes do erro indicado abaixo\n"
                +"ao desenvolvedor"    
            );
            WindowStartError.setContentText(WindowExp.getMessage());
            WindowStartError.showAndWait();
        }
    }
}
