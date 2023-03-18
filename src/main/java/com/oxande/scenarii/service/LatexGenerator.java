package com.oxande.scenarii.service;

import com.oxande.scenarii.model.Actor;
import com.oxande.scenarii.model.DBFilm;
import com.oxande.scenarii.model.Paragraph;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Locale;

/**
 * This class provides a LaTex output
 */
@ApplicationScoped
public class LatexGenerator {

    @Inject
    FilmService filmService;

    /**
     * Convert a text to a compatible Tex string.
     *
     * @param s the string (must not be null)
     * @return the converted string.
     */
    static String tex(String s) {
        StringBuilder buf = new StringBuilder(s.length() + 8);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '#':
                case '$':
                case '%':
                case '_':
                case '{':
                case '}':
                    buf.append("\\").append(c);
                    break;
                case '~':
                case '^':
                    buf.append(c).append("{}");
                    break;
                case '\\':
                    buf.append("\\textbackslash");
                    break;
                default:
                    buf.append(c);
            }
        }
        return buf.toString();
    }

    void appendDialog(StringBuilder buf, Paragraph dialog) {
        Actor actor = dialog.getActor();
        buf.append("\\begin{dialog}");
        if (StringUtils.isNotBlank(dialog.getDirection())) {
            buf.append("[").append(tex(dialog.getDirection().trim())).append("]");
        }
        buf.append("{")
                .append(tex(actor.getName()))
                .append("}\n")
                .append(tex(dialog.getText()))
                .append("\n\\end{dialog}\n");

    
    }

    void appendIntercut(StringBuilder buf, Paragraph dialog) {
        String[] directions = dialog.getDirections(1);
        buf.append("\\intercut{").append(tex(directions[0])).append("}");
    }

    void appendTitle(StringBuilder buf, Paragraph dialog) {
        if(StringUtils.isNotBlank(dialog.getText())){
            buf.append("\\begin{titleover}\n")
                    .append(tex(dialog.getText())).append("\n")
                    .append("\\end{titleover}\n");
        } else {
            String[] directions = dialog.getDirections(1);
            buf.append("\\centretitle{").append(tex(directions[0])).append("}");
        }
    }
    
    void appendScene(StringBuilder buf, Paragraph dialog) {
        String[] directions = dialog.getDirections(3);
        String command;
        switch (directions[0].toLowerCase(Locale.ROOT)){
            case "int":
                command = "\\intslug";
                break;
            case "ext":
                command = "\\extslug";
                break;
            case "int/ext":
                command = "\\intextslug";
                break;
            case "ext/int":
                command = "\\extintslug";
                break;
            default:
                throw new UnsupportedOperationException("Only INT/EXT supported.");
        }
        
        
        buf.append("\n").append(command);
        if(StringUtils.isNotEmpty(directions[2])){
            buf.append("[").append(tex(directions[2])).append("]");
        }
        buf.append("{").append(tex(directions[1])).append("}");
    }
    
    String writeDialog(Paragraph dialog) {
        StringBuilder buf = new StringBuilder();
        switch (dialog.getType()) {
            case Paragraph.DIALOG:
                appendDialog(buf, dialog);
                break;
            case Paragraph.SCENE:
                appendScene(buf, dialog);
                break;
            case Paragraph.INTERCUT:
                appendIntercut(buf, dialog);
                break;
            case Paragraph.TITLE:
                appendTitle(buf, dialog);
            default:
                throw new UnsupportedOperationException();
        }
        return buf.toString();
    }

    public String produceLatex(DBFilm film) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(buf, StandardCharsets.UTF_8))) {
            w.write("\\documentclass{screenplay}");
            w.newLine();
            w.write("\\usepackage[T1]{fontenc}");
            w.newLine();
            w.write(String.format("\\title{%s}", tex(film.getTitle())));
            w.newLine();
            w.newLine();
            w.write("\\begin{document}");
            w.newLine();
            w.write("\\coverpage");
            Iterator<Paragraph> iterator = filmService.streamCurrentDialogs(film);
            while (iterator.hasNext()) {
                Paragraph d = iterator.next();
                w.write(writeDialog(d));
                w.newLine();
            }
            w.write("\\theend");
            w.newLine();
            w.write("\\end{document}");
            w.flush();
            return buf.toString(StandardCharsets.UTF_8);
        }
    }
}
