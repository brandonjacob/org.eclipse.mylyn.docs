/*******************************************************************************
 * Copyright (c) 2013 Stefan Seelmann and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stefan Seelmann - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.internal.wikitext.markdown.tests;

import org.eclipse.mylyn.wikitext.markdown.internal.LinkDefinition;
import org.eclipse.mylyn.wikitext.markdown.internal.LinkDefinitionParser;

import junit.framework.TestCase;

/**
 * @author Stefan Seelmann
 */
public class LinkDefinitionParserTest extends TestCase {

	private LinkDefinitionParser linkDefinitionParser;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		linkDefinitionParser = new LinkDefinitionParser();
	}

	public void testWithoutTitle() {
		String markup = "[foo]: http://example.com/";
		linkDefinitionParser.parse(markup);

		LinkDefinition linkDefinition = linkDefinitionParser.getLinkDefinition("foo");
		assertNotNull(linkDefinition);
		assertEquals("foo", linkDefinition.getId());
		assertEquals("http://example.com/", linkDefinition.getUrl());
		assertNull(linkDefinition.getTitle());
	}

	public void testEmptyTitle() {
		String markup = "[foo]: http://example.com/ \"\"";
		linkDefinitionParser.parse(markup);

		LinkDefinition linkDefinition = linkDefinitionParser.getLinkDefinition("foo");
		assertNotNull(linkDefinition);
		assertEquals("foo", linkDefinition.getId());
		assertEquals("http://example.com/", linkDefinition.getUrl());
		assertEquals("", linkDefinition.getTitle());
	}

	public void testDoubleQuotedTitle() {
		String markup = "[foo]: http://example.com/ \"Optional Title Here\"";
		linkDefinitionParser.parse(markup);

		LinkDefinition linkDefinition = linkDefinitionParser.getLinkDefinition("foo");
		assertNotNull(linkDefinition);
		assertEquals("foo", linkDefinition.getId());
		assertEquals("http://example.com/", linkDefinition.getUrl());
		assertEquals("Optional Title Here", linkDefinition.getTitle());
	}

	public void testSingleQuotedTitle() {
		String markup = "[foo]: http://example.com/ 'Optional Title Here'";
		linkDefinitionParser.parse(markup);

		LinkDefinition linkDefinition = linkDefinitionParser.getLinkDefinition("foo");
		assertNotNull(linkDefinition);
		assertEquals("foo", linkDefinition.getId());
		assertEquals("http://example.com/", linkDefinition.getUrl());
		assertEquals("Optional Title Here", linkDefinition.getTitle());
	}

	public void testParenthesedTitle() {
		String markup = "[foo]: http://example.com/ (Optional Title Here)";
		linkDefinitionParser.parse(markup);

		LinkDefinition linkDefinition = linkDefinitionParser.getLinkDefinition("foo");
		assertNotNull(linkDefinition);
		assertEquals("foo", linkDefinition.getId());
		assertEquals("http://example.com/", linkDefinition.getUrl());
		assertEquals("Optional Title Here", linkDefinition.getTitle());
	}

	public void testUrlInAngleBrackets() {
		String markup = "[foo]: <http://example.com/> (Optional Title Here)";
		linkDefinitionParser.parse(markup);

		LinkDefinition linkDefinition = linkDefinitionParser.getLinkDefinition("foo");
		assertNotNull(linkDefinition);
		assertEquals("foo", linkDefinition.getId());
		assertEquals("http://example.com/", linkDefinition.getUrl());
		assertEquals("Optional Title Here", linkDefinition.getTitle());
	}

	public void testTitleCanBePutOnNextLine() {
		String markup = "   [foo]: <http://example.com/>\n      (Optional Title Here)";
		linkDefinitionParser.parse(markup);

		LinkDefinition linkDefinition = linkDefinitionParser.getLinkDefinition("foo");
		assertNotNull(linkDefinition);
		assertEquals("foo", linkDefinition.getId());
		assertEquals("http://example.com/", linkDefinition.getUrl());
		assertEquals("Optional Title Here", linkDefinition.getTitle());
	}

	public void testMayStartWithUpToThreeSpaces() {
		String markup = "   [foo]: <http://example.com/> (Optional Title Here)";
		linkDefinitionParser.parse(markup);
		LinkDefinition linkDefinition = linkDefinitionParser.getLinkDefinition("foo");
		assertNotNull(linkDefinition);
		assertEquals("foo", linkDefinition.getId());
		assertEquals("http://example.com/", linkDefinition.getUrl());
		assertEquals("Optional Title Here", linkDefinition.getTitle());
	}

	public void testMayNotStartWithMoreThanThreeSpaces() {
		String markup = "    [foo]: http://example.com/";
		linkDefinitionParser.parse(markup);
		LinkDefinition linkDefinition = linkDefinitionParser.getLinkDefinition("foo");
		assertNotNull(linkDefinition);
	}

	public void testNoMatchInMiddleOfLine() {
		String markup = "Lorem [foo]: http://example.com/ ipsum.";
		linkDefinitionParser.parse(markup);
		LinkDefinition linkDefinition = linkDefinitionParser.getLinkDefinition("foo");
		assertNotNull(linkDefinition);
	}

	public void testNotCaseSensitive() {
		String markup = "[foo]: http://example.com/";
		linkDefinitionParser.parse(markup);

		LinkDefinition linkDefinition = linkDefinitionParser.getLinkDefinition("FoO");
		assertNotNull(linkDefinition);
		assertEquals("foo", linkDefinition.getId());
		assertEquals("http://example.com/", linkDefinition.getUrl());
		assertNull(linkDefinition.getTitle());
	}

	public void testMultiline() {
		String markup = "aaa\n\n  [foo]: http://foo.com/\n  [bar]: http://bar.com/\n\nbbb";
		linkDefinitionParser.parse(markup);
		LinkDefinition fooLinkDefinition = linkDefinitionParser.getLinkDefinition("foo");
		assertNotNull(fooLinkDefinition);
		assertEquals("foo", fooLinkDefinition.getId());
		assertEquals("http://foo.com/", fooLinkDefinition.getUrl());
		assertNull(fooLinkDefinition.getTitle());
		LinkDefinition barLinkDefinition = linkDefinitionParser.getLinkDefinition("bar");
		assertNotNull(barLinkDefinition);
		assertEquals("bar", barLinkDefinition.getId());
		assertEquals("http://bar.com/", barLinkDefinition.getUrl());
		assertNull(barLinkDefinition.getTitle());
	}
}
