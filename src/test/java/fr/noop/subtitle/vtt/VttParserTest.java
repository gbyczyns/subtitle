package fr.noop.subtitle.vtt;

import fr.noop.subtitle.model.SubtitleCue;
import fr.noop.subtitle.model.SubtitleParsingException;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class VttParserTest {

    private final VttParser vttParser = new VttParser(StandardCharsets.UTF_8);

    @Test
    public void shouldParseSubtitles() throws IOException, SubtitleParsingException {
        Assert.assertEquals(12, parseSubtitles("/vtt/test.vtt").size());
    }

    public void shouldParseSubtitlesWithBom() throws IOException, SubtitleParsingException {
        Assert.assertEquals(2, parseSubtitles("/vtt/bom_utf-8.vtt").size());
    }

    private List<SubtitleCue> parseSubtitles(String resource) throws IOException, SubtitleParsingException {
        InputStream is = VttParserTest.class.getResourceAsStream("/vtt/test.vtt");
        VttObject vttObject = vttParser.parse(is);
        return vttObject.getCues();
    }
}
