/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxexperience.tools.application;

import com.fxexperience.tools.handler.ViewHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 *
 * @author andje22
 */
public abstract class AbstractController implements Initializable {

    protected final ViewHandler viewHandler;

    public AbstractController(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
    }

    @Override
    public abstract void initialize(URL location, ResourceBundle bundle);

}
