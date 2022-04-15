package converter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLJSONConverter {

    String XMLString;
    List<String> attributeList;

    public XMLJSONConverter(String input) {
        this.XMLString = input;
        this.attributeList = new ArrayList<>();
    }

    private String getElementName() {
        Pattern pattern = Pattern.compile("<(\\w+)");
        Matcher matcher = pattern.matcher(this.XMLString);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "null";
        }
    }

    private String getElementContent() {
        Pattern pattern = Pattern.compile(">([\\s\\w.]*)<");
        Matcher matcher = pattern.matcher(this.XMLString);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "null";
        }
    }

    private List<String> getAttributes() {
        Pattern pattern = Pattern.compile("\\w+\\s=\\s\"\\w+\"");
        Matcher matcher = pattern.matcher(this.XMLString);
        // add every pair of attribute and values as a string
        while(matcher.find()) {
            this.attributeList.add(matcher.group());
        }
        return attributeList;

    }
    // separates attribute names and values and format them
    private String parseAttributes(String attribute) {
        Pattern pattern = Pattern.compile("(\\w+)\\s");
        Matcher matcher = pattern.matcher(attribute);
        matcher.find();

        String attributeName = matcher.group(1);

        pattern = Pattern.compile("\"(\\w+)\"");
        matcher = pattern.matcher(attribute);
        matcher.find();

        String attributeValue = matcher.group(1);

        return String.format("\"@%s\":\"%s\"", attributeName, attributeValue);

    }

    // builds the output string using private methods.
    public String getJSON() {

        StringBuilder output = new StringBuilder();

        output.append("{");
        output.append(String.format("\"%s\"", this.getElementName()));
        output.append(":");

        this.attributeList = this.getAttributes();

        if (this.attributeList.isEmpty()) {

            // XML has no attributes
            output.append(String.format("\"%s\"", this.getElementContent()));

        } else {

            // XML has attributes
            output.append("{");

            // append every attribute to the output
            for(String s : this.attributeList) {
                String attr = this.parseAttributes(s);
                output.append(attr);
                output.append(",");
            }

            // check whether the XML has content
            if (this.getElementContent().equals("null")) {
                output.append(String.format("\"#%s\":%s", this.getElementName(), this.getElementContent()));
            } else {
                output.append(String.format("\"#%s\":\"%s\"", this.getElementName(), this.getElementContent()));

            }
            output.append("}");
        }
        output.append("}");

        return output.toString();
    }

}
