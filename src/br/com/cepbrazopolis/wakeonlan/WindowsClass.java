/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cepbrazopolis.wakeonlan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;


/**
 *
 * @author cristianoaf81
 */
public final class WindowsClass extends Application{
    private AnchorPane WindowPane;
    private Button btnOn;
    private Stage WindowStage;
    private Scene WindowScene;
    private String ipStr;
    private String macStr;
    public static final int PORT = 9;
    private TextField txtMac;
    private Tooltip MacTp;
    private Label MacLb,TpLb;

    
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        initWindowComponents();
        initWindowLayout();
        primaryStage = WindowStage;
        primaryStage.show();
        RedrawLayoutWindow();
        initWindowCompEvents();
    }
    
    public void initWindowComponents(){
        WindowPane = new AnchorPane();
        btnOn = new Button("Ligar Servidor");
        txtMac = new TextField();
        txtMac.setPromptText("XX:XX:XX:XX:XX:XX");
        MacTp = new Tooltip(
            "Formato: XX:XX:XX:XX:XX:XX\n"
            +"na dúvida deixe em branco!"    
        );
        txtMac.setTooltip(MacTp);
        MacLb = new Label("MacAddress:");
        TpLb = new Label(
          "Caso queira ligar outro pc da lan indique o MAC no Campo Acima\n"
          +"caso contrário apenas clique no botão abaixo \n"
          +"e o servidor principal será ativado"      
        );
        WindowPane.getChildren().addAll(btnOn,txtMac,MacLb,TpLb);
        WindowScene = new Scene(WindowPane);
        WindowStage = new Stage();
        WindowStage.setTitle("Ativador de Servidor CEP");
        WindowStage.setResizable(false);
        WindowStage.getIcons().add(
                new Image(
                        WindowsClass.class
                                .getResourceAsStream(
                                        "/br/com/cepbrazopolis/wakeonlan/"
                                                +"resources/cep.png"
                                )
        ));
        WindowStage.setTitle("Ativador de Servidor CEP");
        WindowStage.setResizable(false);
        WindowStage.setScene(WindowScene);
        
        
    }
    
    public void initWindowLayout(){
        WindowPane.setPrefSize(400, 300);
        btnOn.setLayoutX(0);
        btnOn.setLayoutY(0);
        txtMac.setPrefSize(200, 35);
        txtMac.setLayoutX(10);
        txtMac.setLayoutY(50);
        MacLb.setLayoutX(20);
        MacLb.setLayoutY(30);
        TpLb.setLayoutX(30);
        TpLb.setLayoutY(70);
    }
    
    public void RedrawLayoutWindow(){
        btnOn.setLayoutX(
          (WindowPane.getWidth() - btnOn.getWidth())/2      
        );
        
        btnOn.setLayoutY(
          (WindowPane.getHeight() - btnOn.getHeight())/2 + 30     
        );
        
        txtMac.setLayoutX(
          (WindowPane.getWidth() - txtMac.getWidth())/2
        );
        
        MacLb.setLayoutX(
         (WindowPane.getWidth()-MacLb.getWidth())/2
        );
        
        TpLb.setLayoutX(
         (WindowPane.getWidth()-TpLb.getWidth())/2
        );
        
        TpLb.setLayoutY(txtMac.getLayoutY()+50);
    }
    
    public void initWindowCompEvents(){
        btnOn.setOnAction((ActionEvent evt)->{
            String MacAddress = txtMac.getText();
            txtMac.setText("");
            if(MacAddress.equals("") || MacAddress == null){
			   // REPLACE FOR PC MAC HERE	
               macStr = "XX:XX:XX:XX:XX:XX";
            }else{
                macStr = MacAddress;
            }
            ipStr = "255.255.255.255";
            
                try{
                    byte[] macBytes = getMacBytes(macStr);
                    byte[] bytes = new byte[6 + 16 * macBytes.length];
                    int i;
                    for (i = 0; i < 6; i++) {
                        bytes[i] = (byte) 0xff;
                    }
                    for (i = 6; i < bytes.length; i += macBytes.length) {
                        System.arraycopy(macBytes, 0, bytes, i
                                , macBytes.length);
                    }

                    InetAddress address = InetAddress.getByName(ipStr);
                    DatagramPacket packet = new DatagramPacket(bytes
                            , bytes.length
                            , address
                            , PORT);
                    DatagramSocket socket = new DatagramSocket();
                    socket.send(packet);
                    socket.close();
                    

                    Alert WolSuccess = new Alert(Alert.AlertType.INFORMATION
                            , ""
                            , ButtonType.OK);
                    WolSuccess.setTitle("Comando Wake On Lan enviado");
                    WolSuccess.setHeaderText(
                        "O comando Wake on lan foi enviado com sucesso!"
                    );
                    WolSuccess.setContentText("Mac da máquina: "+macStr);
                    WolSuccess.showAndWait();
                    System.out.println(
                            "Pacote Wake-on-lan enviado com sucesso!"
                    );

                }catch(Exception e){
                    Alert WolFailed = new Alert(Alert.AlertType.ERROR
                            ,""
                            ,ButtonType.OK);
                    WolFailed.setTitle("Falha ao enviar comando wol");
                    WolFailed.setHeaderText(
                        "O comando Wake on lan não foi enviado com sucesso!\n"
                        +"cheque os detalhes abaixo e entre em contato com o\n"
                        +"desenvolvedor"
                    );
                    WolFailed.setContentText(e.getMessage());
                    WolFailed.showAndWait();
                    System.out.println("Falha ao enviar pacote Wake-on-LAN" 
                            + e.getMessage());
                    
                }
        });
    }
    
    private static byte[] getMacBytes(String macStr) 
            throws IllegalArgumentException {
        
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("MAC address inválido.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Digito inválido no endereço MAC informado."
            );
        }
        return bytes;
    }
}
