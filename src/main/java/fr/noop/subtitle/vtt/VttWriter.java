/*
 *  This file is part of the noOp organization .
 *
 *  (c) Cyrille Lebeaupin <clebeaupin@noop.fr>
 *
 *  For the full copyright and license information, please view the LICENSE
 *  file that was distributed with this source code.
 *
 */

package fr.noop.subtitle.vtt;

import fr.noop.subtitle.base.BaseSubtitleObject;
import fr.noop.subtitle.model.SubtitleCue;
import fr.noop.subtitle.model.SubtitleLine;
import fr.noop.subtitle.model.SubtitleObject;
import fr.noop.subtitle.model.SubtitleRegionCue;
import fr.noop.subtitle.model.SubtitleStyled;
import fr.noop.subtitle.model.SubtitleText;
import fr.noop.subtitle.model.SubtitleWriter;
import fr.noop.subtitle.util.SubtitleRegion;
import fr.noop.subtitle.util.SubtitleStyle;
import fr.noop.subtitle.util.SubtitleTimeCode;
import fr.noop.subtitle.util.SubtitleRegion.VerticalAlign;
import fr.noop.subtitle.util.SubtitleStyle.FontStyle;

import java.awt.SystemColor;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by clebeaupin on 11/10/15.
 */
public class VttWriter implements SubtitleWriter {

    private static final String NEW_LINE = "\n";

    private final Charset charset;

    public VttWriter(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void write(SubtitleObject subtitleObject, OutputStream os) throws IOException {
        try {
            // Write header
            os.write(("WEBVTT" + NEW_LINE + NEW_LINE).getBytes(this.charset));

            // Write cues
            for (SubtitleCue cue : subtitleObject.getCues()) {
                if (cue.getId() != null) {
                    // Write number of subtitle
                    os.write(cue.getId().getBytes(this.charset));
                    os.write(NEW_LINE.getBytes(this.charset));
                }

                // Write Start time and end time
                String startToEnd = this.formatTimeCode(cue.getStartTime()) + " --> " + this.formatTimeCode(cue.getEndTime());
                os.write(startToEnd.getBytes(this.charset));

                String verticalPosition = this.verticalPosition(cue);
                if (verticalPosition != null) {
                    os.write(" ".getBytes(this.charset));
                    os.write(verticalPosition.getBytes(this.charset));
                }
                os.write(NEW_LINE.getBytes(this.charset));

                // Write text
                String text = "";
                for (SubtitleLine line : cue.getLines()) {
                    for (SubtitleText inText : line.getTexts()) {
                        if (inText instanceof SubtitleStyled) {
                            SubtitleStyle style = ((SubtitleStyled)inText).getStyle();
                            String textString = inText.toString();
                            if (style.getFontStyle() == FontStyle.ITALIC || style.getFontStyle() == FontStyle.OBLIQUE) {
                                textString = String.format("<i>%s</i>", textString);
                            }
                            if (style.getColor() != null){
                                textString = String.format("<c.%s>%s</c>", style.getColor(), textString);
                            }
                            text += textString;
                        } else {
                            text += inText.toString();
                        }
                        text += NEW_LINE;
                    }
                }
                os.write(text.getBytes(this.charset));

                // Write empty line
                os.write(NEW_LINE.getBytes(this.charset));

                // Get region

            }
        } catch (UnsupportedEncodingException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    private String verticalPosition(SubtitleCue cue) {
        if (cue instanceof SubtitleRegionCue) {
            VerticalAlign va =  ((SubtitleRegionCue) cue).getRegion().getVerticalAlign();
            if (va == VerticalAlign.TOP) {
                return "line:0";
            }
        }
        return null;
    }

    private String formatTimeCode(SubtitleTimeCode timeCode) {
        return String.format("%02d:%02d:%02d.%03d",
                timeCode.getHour(),
                timeCode.getMinute(),
                timeCode.getSecond(),
                timeCode.getMillisecond());
    }
}
