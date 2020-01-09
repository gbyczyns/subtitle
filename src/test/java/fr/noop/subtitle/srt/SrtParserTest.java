package fr.noop.subtitle.srt;

import fr.noop.subtitle.model.SubtitleParsingException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SrtParserTest {

    private final SrtParser srtParser = new SrtParser(StandardCharsets.UTF_8);

    @Test
    public void shouldParseSubtitlesWithoutEof() throws IOException, SubtitleParsingException {
        parseResource("/srt/no-eof-nl.srt");
    }

    @Test
    public void shouldParseSubtitlesWithBom() throws IOException, SubtitleParsingException {
        parseResource("/srt/bom_utf-8.srt");
    }

    private void parseResource(String resource) throws IOException, SubtitleParsingException {
        InputStream is = SrtParserTest.class.getResourceAsStream(resource);
        SrtObject srtObject = srtParser.parse(is);

        Assert.assertEquals(2, srtObject.getCues().size());
    }
}
