package il.org.spartan.utils;

import static fluent.ly.___.unreachable;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import fluent.ly.nil;
import il.org.spartan.streotypes.Antiexample;

/** Static methods for I/O related operations */
@Antiexample
public class IO {
  public static String concatLines(final Iterable<String> ss) {
    final StringBuffer $ = new StringBuffer(1000);
    final Separator nl = new Separator("\n");
    for (final String ¢ : ss)
      $.append(nl).append(¢);
    return $ + "";
  }
  public static String concatLines(final String... ss) {
    final StringBuffer $ = new StringBuffer(1000);
    final Separator nl = new Separator("\n");
    for (final String ¢ : ss)
      $.append(nl).append(¢);
    return $ + "";
  }
  public static List<String> lines(final String s) throws IOException {
    final List<String> $ = new ArrayList<>();
    for (final BufferedReader br = new BufferedReader(new StringReader(s));;) {
      final String line = br.readLine();
      if (line == null)
        return $;
      $.add(line);
    }
  }
  public static InputStream toInputStream(final String $) {
    try {
      return new ByteArrayInputStream($.getBytes("UTF-8"));
    } catch (final UnsupportedEncodingException e) {
      unreachable();
      return nil.forgetting(e);
    }
  }
  /** Read the contents of the given class-path file.
   * @param clazz Class - Specifies a location in the class-path tree
   * @param path Relative path to the file from the given class
   * @return Contents of the file
   * @throws IOException If an I/O error occur */
  public static String toString(final Class<?> clazz, final String path) throws IOException {
    return toString(clazz.getResourceAsStream(path));
  }
  /** Read the contents of the given stream and return it as a String
   * @param ¢ Input stream
   * @return the entire content of <code>is</code>
   * @throws IOException If an I/O error occur */
  public static String toString(final InputStream ¢) throws IOException {
    return toString(new InputStreamReader(¢));
  }
  /** Read the contents of the given reader and return it as a String
   * @param r Reader
   * @return the entire content of <code>r</code>
   * @throws IOException If an I/O error occur */
  public static String toString(final Reader r) throws IOException {
    final StringBuilder $ = new StringBuilder();
    for (int c = r.read(); c >= 0; c = r.read())
      $.append((char) c);
    return $ + "";
  }
  /** Write a string to a file
   * @param outputFile File to be written
   * @param ss Strings to write
   * @throws IOException If an I/O error occur */
  public static void writeLines(final File outputFile, final String... ss) throws IOException {
    try (FileWriter fw = new FileWriter(outputFile)) {
      for (final String ¢ : ss) {
        fw.append(¢);
        fw.append("\n");
      }
    }
  }
}
