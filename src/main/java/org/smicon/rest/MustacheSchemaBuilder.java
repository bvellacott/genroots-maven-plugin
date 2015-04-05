package org.smicon.rest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MustacheSchemaBuilder
{
	static String javaIdentifierRegExp = "[a-zA-Z_$0-9]+";
	static String tab = "    ";
	static Format format = Format.java;
	
	enum Format {
		json,
		java,
		;
	}
	
	enum TagType {
		variable("\\{\\{\\s*" + javaIdentifierRegExp + "\\s*\\}\\}"),
		escapedVariable("\\{\\{\\{\\s*" + javaIdentifierRegExp + "\\s*\\}\\}\\}"),
		sectionStart("\\{\\{#\\s*" + javaIdentifierRegExp + "\\s*\\}\\}"),
		invertedSectionStart("\\{\\{^\\s*" + javaIdentifierRegExp + "\\s*\\}\\}"),
		sectionEnd("\\{\\{/\\s*" + javaIdentifierRegExp + "\\s*\\}\\}"),
		//partial("\\{\\{>\\s*" + javaIdentifierRegExp + "\\s*\\}\\}"),
		// no support for set delimiters or partials
		;
		
		Pattern pat; 
		private TagType(String aRegExp) {
			pat = Pattern.compile(aRegExp);
		}
		
		public Pattern getPattern() { return pat; };
	}
	
	class Tag{
		public TagType type;
		public String identifier;
		Tag(TagType aType, String aIdentifier) { type = aType; identifier = aIdentifier;}
		
		public String toString() { return type.name() + " : " + identifier; }
		
		@Override
		public boolean equals(Object aOther) {
			if(!(aOther instanceof Tag)) return false;
			Tag ot = (Tag) aOther;
			return ot.type.equals(this.type) && ot.identifier.equals(this.identifier);
		}
		
		@Override
		public int hashCode() {return identifier.hashCode();}
	}
	
	class Node {
		public Section parent;
		public Tag tag;
		Node(Section aParent, Tag aTag){parent = aParent; tag = aTag; if(parent != null) parent.children.put(tag, this);}
		
		@Override
		public int hashCode() {return tag.hashCode();}
		
		public int depth() {
			int depth = 0;
			Node currentNode = this;
			while(currentNode.parent != null) {
				depth++;
				currentNode = currentNode.parent;
			}
			return depth;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < this.depth(); i++) sb.append(tab);
			switch(format) {
			case java:
				sb.append("public Object ").append(tag.identifier).append(";\n");
				break;
			case json:
			default:
				sb.append(tag.identifier).append(": {},\n");
			}
			return sb.toString();
		}
	}
	
	class Section extends Node{
		public Map<Tag, Node> children = new LinkedHashMap();
		Section(Section aParent, Tag aTag) { super(aParent, aTag); };
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < this.depth(); i++) sb.append(tab);
			switch(format) {
			case java:
				sb.append("public Object ").append(tag.identifier).append(" = new Object() {\n");
				for(Node child : this.children.values()) sb.append(child);
				for(int i = 0; i < this.depth(); i++) sb.append(tab);
				sb.append("};\n");
				break;
			case json:
			default:
				sb.append(tag.identifier).append(": {\n");
				for(Node child : this.children.values()) sb.append(child);
				for(int i = 0; i < this.depth(); i++) sb.append(tab);
				sb.append("},\n");
			}
			return sb.toString();
		}
	}
	
	class MustacheSchemaBuildException extends Exception {
		private static final long serialVersionUID = 1L;
		MustacheSchemaBuildException(String aMessage) {super(aMessage);}
	}
	
	Section rootSection;
	
	public MustacheSchemaBuilder(String aContents) throws Exception
	{
		TreeMap<Integer, Tag> tagMap = new TreeMap();
		for(TagType type : TagType.values()) {
			populateMap(tagMap, type, aContents);
		}
		System.out.println(tagMap);
		
		rootSection = new Section(null, new Tag(TagType.sectionStart, "$root$"));
		Section currentSection = rootSection;
		
		for(Tag tag : tagMap.values()) {
			if(currentSection.tag.type == TagType.invertedSectionStart) {
				if(tag.type == TagType.sectionEnd) {
					if(!tag.identifier.equals(currentSection.tag.identifier))
						throw new MustacheSchemaBuildException("The end tag: " + tag.identifier + " doesn't match the expected start tag: " + currentSection.tag.identifier + "\n\n" + rootSection);
					currentSection = currentSection.parent;
				}
				continue;
			}
			switch(tag.type)
			{
			case variable:
				if(!currentSection.children.containsKey(tag))
					new Node(currentSection, tag);
				break;
			case sectionStart:
				if(!currentSection.children.containsKey(tag) || !(currentSection.children.get(tag) instanceof Section)) 
					new Section(currentSection, tag);
				currentSection = (Section)currentSection.children.get(tag);
				break;
			case invertedSectionStart:
				if(!currentSection.children.containsKey(tag)) 
					currentSection = new Section(currentSection, tag);
				break;
			case sectionEnd:
				if(!tag.identifier.equals(currentSection.tag.identifier))
					throw new MustacheSchemaBuildException("The end tag: " + tag.identifier + " doesn't match the expected start tag: " + currentSection.tag.identifier + "\n\n" + rootSection);
				currentSection = currentSection.parent;
				break;
			default:
			}
		}
	}
	
	public void build() {
		System.out.println(rootSection);
	}

	public static void main(String[] args) throws Exception
	{
		String contents;
		try {
//			contents = readFile(args[0], Charset.defaultCharset());
			
//			contents = readFile("/home/benjamin/Desktop/projects/java/genroots-maven-plugin/src/main/resources/org/smicon/rest/ControllerTemplate.mustache", Charset.defaultCharset());
			contents = readFile("/home/benjamin/Desktop/projects/java/genroots-maven-plugin/src/main/resources/org/smicon/rest/EmberModel.mustache", Charset.defaultCharset());
		}catch(Exception e)
		{
			System.out.println("Unable to open file!");
			throw e;
		}
		
		MustacheSchemaBuilder builder = new MustacheSchemaBuilder(contents);
		
		builder.build();
	}
	
	void populateMap(Map<Integer, Tag> map, TagType aType, String aContents) {
		Matcher m = aType.getPattern().matcher(aContents);
		while(m.find()) {
			int start = m.start() + 3;
			if(aType == TagType.variable) start = m.start() + 2;
			map.put(m.start(), new Tag(aType, aContents.substring(start, m.end() - 2).trim()));
		}
	}

	static String readFile(String path, Charset encoding) throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
}
