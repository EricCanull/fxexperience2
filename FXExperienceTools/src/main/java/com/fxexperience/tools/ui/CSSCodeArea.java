package com.fxexperience.tools.ui;

import com.fxexperience.tools.controller.StyleController;
import com.fxexperience.tools.util.css.CSSBaseStyle;
import com.fxexperience.tools.util.css.CSSHighlight;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;


public class CSSCodeArea extends CodeArea {

    public CSSCodeArea() {
        getStylesheets().add(StyleController.class.getResource("/styles/code_area.css").toExternalForm());
        setParagraphGraphicFactory(LineNumberFactory.get(this));
        richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX
                .subscribe(change -> this.setStyleSpans(0, CSSHighlight.computeHighlighting(this.getText())));
    }
}
