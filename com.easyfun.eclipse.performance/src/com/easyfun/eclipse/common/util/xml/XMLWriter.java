/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package com.easyfun.eclipse.common.util.xml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.StringTokenizer;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.ext.LexicalHandler;


/**
 * <p>
 * <code>XMLWriter</code> takes a DOM4J tree and formats it to a stream as
 * XML. It can also take SAX events too so can be used by SAX clients as this
 * object implements the {@link org.xml.sax.ContentHandler}and {@link
 * LexicalHandler} interfaces. as well. This formatter performs typical document
 * formatting. The XML declaration and processing instructions are always on
 * their own lines. An {@link OutputFormat}object can be used to define how
 * whitespace is handled when printing and allows various configuration options,
 * such as to allow suppression of the XML declaration, the encoding declaration
 * or whether empty documents are collapsed.
 * </p>
 * 
 * <p>
 * There are <code>write(...)</code> methods to print any of the standard
 * DOM4J classes, including <code>Document</code> and <code>Element</code>,
 * to either a <code>Writer</code> or an <code>OutputStream</code>.
 * Warning: using your own <code>Writer</code> may cause the writer's
 * preferred character encoding to be ignored. If you use encodings other than
 * UTF8, we recommend using the method that takes an OutputStream instead.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @author Joseph Bowbeer
 * @version $Revision: 1.7 $
 */
public class XMLWriter {
	private static final String PAD_TEXT = " ";

	protected static final String[] LEXICAL_HANDLER_NAMES = { "http://xml.org/sax/properties/lexical-handler",
			"http://xml.org/sax/handlers/LexicalHandler" };

	protected static final OutputFormat DEFAULT_FORMAT = new OutputFormat();

	/** Should entityRefs by resolved when writing ? */
	private boolean resolveEntityRefs = true;

	/**
	 * Stores the last type of node written so algorithms can refer to the
	 * previous node type
	 */
	protected int lastOutputNodeType;

	/** Stores the xml:space attribute value of preserve for whitespace flag */
	protected boolean preserve = false;

	/** The Writer used to output to */
	protected Writer writer;

	/** The format used by this writer */
	private OutputFormat format;

	/** whether we should escape text */
	private boolean escapeText = true;

	/**
	 * The initial number of indentations (so you can print a whole document
	 * indented, if you like)
	 */
	private int indentLevel = 0;

	/** buffer used when escaping strings */
	private StringBuffer buffer = new StringBuffer();

	private char lastChar;

	/** Whether a flush should occur after writing a document */
	private boolean autoFlush;

	/**
	 * what is the maximum allowed character code such as 127 in US-ASCII (7
	 * bit) or 255 in ISO- (8 bit) or -1 to not escape any characters (other
	 * than the special XML characters like &lt; &gt; &amp;)
	 */
	private int maximumAllowedCharacter;

	public XMLWriter(OutputStream out, OutputFormat format) throws UnsupportedEncodingException {
		this.format = format;
		this.writer = createWriter(out, format.getEncoding());
		this.autoFlush = true;
	}

	/**
	 * <p>
	 * This will print the <code>Document</code> to the current Writer.
	 * </p>
	 * 
	 * <p>
	 * Warning: using your own Writer may cause the writer's preferred character
	 * encoding to be ignored. If you use encodings other than UTF8, we
	 * recommend using the method that takes an OutputStream instead.
	 * </p>
	 * 
	 * <p>
	 * Note: as with all Writers, you may need to flush() yours after this
	 * method returns.
	 * </p>
	 * 
	 * @param doc
	 *            <code>Document</code> to format.
	 * 
	 * @throws IOException
	 *             if there's any problem writing.
	 */
	public void write(Document doc) throws IOException {
		writeDeclaration();

		if (doc.getDoctype() != null) {
			indent();
			writeDocType(doc.getDoctype());
		}

		NodeList nodes = doc.getChildNodes();

		for (int i = 0, size = nodes.getLength(); i < size; i++) {
			Node node = nodes.item(i);
			writeNode(node);
		}

		// writePrintln();

		if (autoFlush) {
			flush();
		}
	}
	
    /**
     * <p>
     * Writes the <code>{@link Element}</code>, including its <code>{@link
     * Attribute}</code>
     * s, and its value, and all its content (child nodes) to the current
     * Writer.
     * </p>
     * 
     * @param element
     *            <code>Element</code> to output.
     * 
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void write(Element element) throws IOException {
        writeElement(element);

        if (autoFlush) {
            flush();
        }
    }	

	/**
	 * Flushes the underlying Writer
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public void flush() throws IOException {
		writer.flush();
	}

	/**
	 * Writes the new line text to the underlying Writer
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public void println() throws IOException {
		writer.write(format.getLineSeparator());
	}

	/**
	 * Returns the maximum allowed character code that should be allowed
	 * unescaped which defaults to 127 in US-ASCII (7 bit) or 255 in ISO- (8
	 * bit).
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getMaximumAllowedCharacter() {
		if (maximumAllowedCharacter == 0) {
			maximumAllowedCharacter = defaultMaximumAllowedCharacter();
		}

		return maximumAllowedCharacter;
	}

	public boolean resolveEntityRefs() {
		return resolveEntityRefs;
	}

	public void setResolveEntityRefs(boolean resolve) {
		this.resolveEntityRefs = resolve;
	}

	// Implementation methods
	// -------------------------------------------------------------------------
	protected void writeElement(Element element) throws IOException {
		NodeList nodes = element.getChildNodes();
		int size = nodes.getLength();
		String qualifiedName = element.getNodeName();

		writePrintln();
		indent();

		writer.write("<");
		writer.write(qualifiedName);

		// Print out additional namespace declarations
		boolean textOnly = true;

		for (int i = 0; i < size; i++) {
			Node node = nodes.item(i);

			if (node instanceof Element) {
				textOnly = false;
			} else if (node instanceof Comment) {
				textOnly = false;
			}
		}

		writeAttributes(element);

		lastOutputNodeType = Node.ELEMENT_NODE;

		if (size <= 0) {
			writeEmptyElementClose(qualifiedName);
		} else {
			writer.write(">");

			if (textOnly) {
				// we have at least one text node so lets assume
				// that its non-empty
				writeElementContent(element);
			} else {
				// we know it's not null or empty from above
				++indentLevel;

				writeElementContent(element);

				--indentLevel;

				writePrintln();
				indent();
			}

			writer.write("</");
			writer.write(qualifiedName);
			writer.write(">");
		}

		lastOutputNodeType = Node.ELEMENT_NODE;
	}

	protected void writeCDATA(String text) throws IOException {
		writer.write("<![CDATA[");

		if (text != null) {
			writer.write(text);
		}

		writer.write("]]>");

		lastOutputNodeType = Node.CDATA_SECTION_NODE;
	}

	/**
	 * <p>
	 * This will write the declaration to the given Writer. Assumes XML version
	 * 1.0 since we don't directly know.
	 * </p>
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	protected void writeDeclaration() throws IOException {
		String encoding = format.getEncoding();

		// Only print of declaration is not suppressed
		if (!format.isSuppressDeclaration()) {
			// Assume 1.0 version
			if (encoding.equals("UTF8")) {
				writer.write("<?xml version=\"1.0\"");

				if (!format.isOmitEncoding()) {
					writer.write(" encoding=\"UTF-8\"");
				}

				writer.write("?>");
			} else {
				writer.write("<?xml version=\"1.0\"");

				if (!format.isOmitEncoding()) {
					writer.write(" encoding=\"" + encoding + "\"");
				}

				writer.write("?>");
			}

			if (format.isNewLineAfterDeclaration()) {
				println();
			}
		}
	}

	/**
	 * Outputs the content of the given element. If whitespace trimming is
	 * enabled then all adjacent text nodes are appended together before the
	 * whitespace trimming occurs to avoid problems with multiple text nodes
	 * being created due to text content that spans parser buffers in a SAX
	 * parser.
	 * 
	 * @param element
	 *            DOCUMENT ME!
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	protected void writeElementContent(Element element) throws IOException {
		boolean trim = format.isTrimText();
		boolean oldPreserve = preserve;
		NodeList nodes = element.getChildNodes();

		if (trim) { // verify we have to before more expensive test
			preserve = isElementSpacePreserved(element);
			trim = !preserve;
		}

		if (trim) {
			// concatenate adjacent text nodes together
			// so that whitespace trimming works properly
			Text lastTextNode = null;
			StringBuffer buff = null;
			boolean textOnly = true;

			for (int i = 0, size = nodes.getLength(); i < size; i++) {
				Node node = nodes.item(i);

				if (node instanceof Text) {
					if (lastTextNode == null) {
						lastTextNode = (Text) node;
					} else {
						if (buff == null) {
							buff = new StringBuffer(lastTextNode.getData());
						}

						buff.append(((Text) node).getData());
					}
				} else {
					if (!textOnly && format.isPadText()) {
						// only add the PAD_TEXT if the text itself starts with
						// whitespace
						char firstChar = 'a';
						if (buff != null && !"".equals(buff.toString())) {
							firstChar = buff.charAt(0);
						} else if (lastTextNode != null && lastTextNode.getData() != null 
								&& !"".equals(lastTextNode.getData())) {
							firstChar = lastTextNode.getData().charAt(0);
						}

						if (Character.isWhitespace(firstChar)) {
							writer.write(PAD_TEXT);
						}
					}

					if (lastTextNode != null) {
						if (buff != null) {
							writeString(buff.toString());
							buff = null;
						} else {
							writeString(lastTextNode.getData());
						}

						if (format.isPadText()) {
							// only add the PAD_TEXT if the text itself ends with whitespace
							char lastTextChar = 'a';
							if (lastTextNode != null && lastTextNode.getData() 
									!= null && !"".equals(lastTextNode.getData())) {
								String txt = lastTextNode.getData();
								lastTextChar = txt.charAt(txt.length() - 1);
							}

							if (Character.isWhitespace(lastTextChar)) {
								writer.write(PAD_TEXT);
							}
						}

						lastTextNode = null;
					}

					textOnly = false;
					writeNode(node);
				}
			}

			if (lastTextNode != null) {
				if (!textOnly && format.isPadText()) {
					// only add the PAD_TEXT if the text itself starts with
					// whitespace
					char firstChar = 'a';
					if (buff != null && !"".equals(buff.toString())) {
						firstChar = buff.charAt(0);
					} else if(lastTextNode != null && lastTextNode.getData() 
							!= null && !"".equals(lastTextNode.getData())){
						firstChar = lastTextNode.getData().charAt(0);
					}

					if (Character.isWhitespace(firstChar)) {
						writer.write(PAD_TEXT);
					}
				}

				if (buff != null) {
					writeString(buff.toString());
					buff = null;
				} else {
					writeString(lastTextNode.getData());
				}

				lastTextNode = null;
			}
		} else {
			Node lastTextNode = null;

			for (int i = 0, size = nodes.getLength(); i < size; i++) {
				Node node = nodes.item(i);

				if (node instanceof Text) {
					writeNode(node);
					lastTextNode = node;
				} else {
					if ((lastTextNode != null) && format.isPadText()) {
						// only add the PAD_TEXT if the text itself ends with
						// whitespace
						String txt = lastTextNode.getNodeValue();
						if(txt != null && !"".equals(txt)){
							char lastTextChar = txt.charAt(txt.length() - 1);
							if (Character.isWhitespace(lastTextChar)) {
								writer.write(PAD_TEXT);
							}
						}
					}

					writeNode(node);

					// if ((lastTextNode != null) && format.isPadText()) {
					// writer.write(PAD_TEXT);
					// }

					lastTextNode = null;
				}
			}
		}

		preserve = oldPreserve;
	}

	/**
	 * Writes the attributes of the given element
	 * 
	 * @param element
	 *            DOCUMENT ME!
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	protected void writeAttributes(Element element) throws IOException {
		// I do not yet handle the case where the same prefix maps to
		// two different URIs. For attributes on the same element
		// this is illegal; but as yet we don't throw an exception
		// if someone tries to do this
		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0, size = attributes.getLength(); i < size; i++) {
			Attr attribute = (Attr) attributes.item(i);
			char quote = format.getAttributeQuoteCharacter();
			writer.write(" ");
			writer.write(attribute.getName());
			writer.write("=");
			writer.write(quote);
			writeEscapeAttributeEntities(attribute.getValue());
			writer.write(quote);
		}
	}

	protected void writeAttribute(Attr attribute) throws IOException {
		writer.write(" ");
		writer.write(attribute.getName());
		writer.write("=");

		char quote = format.getAttributeQuoteCharacter();
		writer.write(quote);

		writeEscapeAttributeEntities(attribute.getValue());

		writer.write(quote);
		lastOutputNodeType = Node.ATTRIBUTE_NODE;
	}

	protected void writeDocType(DocumentType docType) throws IOException {
		if (docType != null) {
			//docType.write(writer);
			//writePrintln();
		}
	}

	protected void writeEntity(Entity entity) throws IOException {
		if (!resolveEntityRefs()) {
			writeEntityRef(entity.getNodeName());
		} else {
			writer.write(entity.getNodeValue());
		}
	}

	protected void writeEntityRef(String name) throws IOException {
		writer.write("&");
		writer.write(name);
		writer.write(";");

		lastOutputNodeType = Node.ENTITY_REFERENCE_NODE;
	}

	protected void writeComment(String text) throws IOException {
		if (format.isNewlines()) {
			println();
			indent();
		}

		writer.write("<!--");
		writer.write(text);
		writer.write("-->");

		lastOutputNodeType = Node.COMMENT_NODE;
	}

	/**
	 * This method is used to write out Nodes that contain text and still allow
	 * for xml:space to be handled properly.
	 * 
	 * @param node
	 *            DOCUMENT ME!
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	protected void writeNodeText(Text node) throws IOException {
		String text = node.getData();

		if ((text != null) && (text.length() > 0)) {
			if (escapeText) {
				text = escapeElementEntities(text);
			}

			lastOutputNodeType = Node.TEXT_NODE;
			writer.write(text);
			lastChar = text.charAt(text.length() - 1);
		}
	}

	protected void writeNode(Node node) throws IOException {
		int nodeType = node.getNodeType();

		switch (nodeType) {
			case Node.ELEMENT_NODE:
				writeElement((Element) node);
				break;

			case Node.ATTRIBUTE_NODE:
				writeAttribute((Attr) node);
				break;

			case Node.TEXT_NODE:
				writeNodeText((Text) node);
				break;

			case Node.CDATA_SECTION_NODE:
				writeCDATA(((CDATASection) node).getData());
				break;

			case Node.ENTITY_REFERENCE_NODE:
				writeEntity((Entity) node);
				break;

			case Node.PROCESSING_INSTRUCTION_NODE:
				writeProcessingInstruction((ProcessingInstruction) node);
				break;

			case Node.COMMENT_NODE:
				writeComment(((Comment) node).getData());
				break;

			case Node.DOCUMENT_NODE:
				write((Document) node);
				break;

			case Node.DOCUMENT_TYPE_NODE:
				writeDocType((DocumentType) node);
				break;

			default:
				throw new IOException("Invalid node type: " + node);
		}
	}

	protected void writeProcessingInstruction(ProcessingInstruction pi) throws IOException {
		// indent();
		writer.write("<?");
		writer.write(pi.getNodeName());
		writer.write(" ");
		writer.write(pi.getNodeValue());
		writer.write("?>");
		writePrintln();

		lastOutputNodeType = Node.PROCESSING_INSTRUCTION_NODE;
	}

	protected void writeString(String text) throws IOException {
		if ((text != null) && (text.length() > 0)) {
			if (escapeText) {
				text = escapeElementEntities(text);
			}

			// if (format.isPadText()) {
			// if (lastOutputNodeType == Node.ELEMENT_NODE) {
			// writer.write(PAD_TEXT);
			// }
			// }
			if (format.isTrimText()) {
				boolean first = true;
				StringTokenizer tokenizer = new StringTokenizer(text);

				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();

					if (first) {
						first = false;

						if (lastOutputNodeType == Node.TEXT_NODE) {
							writer.write(" ");
						}
					} else {
						writer.write(" ");
					}

					writer.write(token);
					lastOutputNodeType = Node.TEXT_NODE;
					lastChar = token.charAt(token.length() - 1);
				}
			} else {
				lastOutputNodeType = Node.TEXT_NODE;
				writer.write(text);
				lastChar = text.charAt(text.length() - 1);
			}
		}
	}

	protected void writeEscapeAttributeEntities(String txt) throws IOException {
		if (txt != null) {
			String escapedText = escapeAttributeEntities(txt);
			writer.write(escapedText);
		}
	}
	
    /**
     * Get an OutputStreamWriter, use preferred encoding.
     * 
     * @param outStream
     *            DOCUMENT ME!
     * @param encoding
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws UnsupportedEncodingException
     *             DOCUMENT ME!
     */
    protected Writer createWriter(OutputStream outStream, String encoding)
            throws UnsupportedEncodingException {
        return new BufferedWriter(new OutputStreamWriter(outStream, encoding));
    }	

	/**
	 * This will take the pre-defined entities in XML 1.0 and convert their
	 * character representation to the appropriate entity reference, suitable
	 * for XML attributes.
	 * 
	 * @param text
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected String escapeElementEntities(String text) {
		char[] block = null;
		int i;
		int last = 0;
		int size = text.length();

		for (i = 0; i < size; i++) {
			String entity = null;
			char c = text.charAt(i);

			switch (c) {
				case '<':
					entity = "&lt;";

					break;

				case '>':
					entity = "&gt;";

					break;

				case '&':
					entity = "&amp;";

					break;

				case '\t':
				case '\n':
				case '\r':

					// don't encode standard whitespace characters
					if (preserve) {
						entity = String.valueOf(c);
					}

					break;

				default:

					if ((c < 32) || shouldEncodeChar(c)) {
						entity = "&#" + (int) c + ";";
					}

					break;
			}

			if (entity != null) {
				if (block == null) {
					block = text.toCharArray();
				}

				buffer.append(block, last, i - last);
				buffer.append(entity);
				last = i + 1;
			}
		}

		if (last == 0) {
			return text;
		}

		if (last < size) {
			if (block == null) {
				block = text.toCharArray();
			}

			buffer.append(block, last, i - last);
		}

		String answer = buffer.toString();
		buffer.setLength(0);

		return answer;
	}

	/**
	 * This will take the pre-defined entities in XML 1.0 and convert their
	 * character representation to the appropriate entity reference, suitable
	 * for XML attributes.
	 * 
	 * @param text
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected String escapeAttributeEntities(String text) {
		char quote = format.getAttributeQuoteCharacter();

		char[] block = null;
		int i;
		int last = 0;
		int size = text.length();

		for (i = 0; i < size; i++) {
			String entity = null;
			char c = text.charAt(i);

			switch (c) {
				case '<':
					entity = "&lt;";

					break;

				case '>':
					entity = "&gt;";

					break;

				case '\'':

					if (quote == '\'') {
						entity = "&apos;";
					}

					break;

				case '\"':

					if (quote == '\"') {
						entity = "&quot;";
					}

					break;

				case '&':
					entity = "&amp;";

					break;

				case '\t':
				case '\n':
				case '\r':

					// don't encode standard whitespace characters
					break;

				default:

					if ((c < 32) || shouldEncodeChar(c)) {
						entity = "&#" + (int) c + ";";
					}

					break;
			}

			if (entity != null) {
				if (block == null) {
					block = text.toCharArray();
				}

				buffer.append(block, last, i - last);
				buffer.append(entity);
				last = i + 1;
			}
		}

		if (last == 0) {
			return text;
		}

		if (last < size) {
			if (block == null) {
				block = text.toCharArray();
			}

			buffer.append(block, last, i - last);
		}

		String answer = buffer.toString();
		buffer.setLength(0);

		return answer;
	}

	/**
	 * Determines if element is a special case of XML elements where it contains
	 * an xml:space attribute of "preserve". If it does, then retain whitespace.
	 * 
	 * @param element
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected final boolean isElementSpacePreserved(Element element) {
		final Attr attr = (Attr) element.getAttributeNode("space");
		boolean preserveFound = preserve; // default to global state

		if (attr != null) {
			if ("xml".equals(attr.getPrefix()) && "preserve".equals(attr.getValue())) {
				preserveFound = true;
			} else {
				preserveFound = false;
			}
		}

		return preserveFound;
	}

	protected void writeEmptyElementClose(String qualifiedName) throws IOException {
		// Simply close up
		if (!format.isExpandEmptyElements()) {
			writer.write("/>");
		} else {
			writer.write("></");
			writer.write(qualifiedName);
			writer.write(">");
		}
	}

	/**
	 * Should the given character be escaped. This depends on the encoding of
	 * the document.
	 * 
	 * @param c
	 *            DOCUMENT ME!
	 * 
	 * @return boolean
	 */
	protected boolean shouldEncodeChar(char c) {
		int max = getMaximumAllowedCharacter();

		return (max > 0) && (c > max);
	}

	/**
	 * Returns the maximum allowed character code that should be allowed
	 * unescaped which defaults to 127 in US-ASCII (7 bit) or 255 in ISO- (8
	 * bit).
	 * 
	 * @return DOCUMENT ME!
	 */
	protected int defaultMaximumAllowedCharacter() {
		String encoding = format.getEncoding();

		if (encoding != null) {
			if (encoding.equals("US-ASCII")) {
				return 127;
			}
		}

		// no encoding for things like ISO-*, UTF-8 or UTF-16
		return -1;
	}

	/**
	 * <p>
	 * This will print a new line only if the newlines flag was set to true
	 * </p>
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	protected void writePrintln() throws IOException {
		if (format.isNewlines()) {
			String seperator = format.getLineSeparator();
			if (lastChar != seperator.charAt(seperator.length() - 1)) {
				writer.write(format.getLineSeparator());
			}
		}
	}

	protected void indent() throws IOException {
		String indent = format.getIndent();

		if ((indent != null) && (indent.length() > 0)) {
			for (int i = 0; i < indentLevel; i++) {
				writer.write(indent);
			}
		}
	}
}
