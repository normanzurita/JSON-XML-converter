package converter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONXMLConverter {
    String XMLElementName;
    String XMLElementContent;
    List<String> XMLAttributes;

    String JSONString;

    public JSONXMLConverter (String input) {
        this.XMLAttributes = new ArrayList<>();
        this.JSONString = input;
        this.parseXMLElementName();
        this.parseXMLAttributes();
        this.parseXMLElementContent();
    }

    private void parseXMLElementName() {
        Pattern pattern = Pattern.compile("\"([\\w\\s]*)\"");
        Matcher matcher = pattern.matcher(this.JSONString);

        if (matcher.find()) {
            this.XMLElementName = matcher.group(1);
        } else {
            this.XMLElementName = "null";
        }
    }

    private void parseXMLAttributes() {
        Pattern pattern = Pattern.compile("\"(@[\\w\\s:\"]+),");
        Matcher matcher = pattern.matcher(this.JSONString);
        while (matcher.find()) {
            this.XMLAttributes.add(matcher.group(1));
        }
    }

    private void parseXMLElementContent() {
        Pattern pattern;
        if (this.XMLAttributes.isEmpty()) {
            pattern = Pattern.compile(":\"([\\w\\s.]+)\"");
        } else {
            pattern = Pattern.compile("\"#[\\w\\s]+\":\"?([\\w ]+)\"?");
        }
        Matcher matcher = pattern.matcher(this.JSONString);
        if (matcher.find()) {
            this.XMLElementContent = matcher.group(1);
        } else {
            this.XMLElementContent = "null";
        }

    }

    private String parseAttributeName(String attribute) {
        Pattern pattern = Pattern.compile("@([\\w\\s]+)");
        Matcher matcher = pattern.matcher(attribute);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return "null";
    }

    private String parseAttributeValue(String attribute) {
        Pattern pattern = Pattern.compile(":\"?([\\w\\d ]+)");
        Matcher matcher = pattern.matcher(attribute);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return "null";
    }

    public String getXML() {
        StringBuilder output = new StringBuilder();
        output.append("<");
        output.append(String.format("%s", this.XMLElementName));
        if (!this.XMLAttributes.isEmpty()) {
            for (String attr : XMLAttributes) {
                output.append(" ");
                output.append(this.parseAttributeName(attr));
                output.append(" = ");
                output.append(String.format("\"%s\"", this.parseAttributeValue(attr)));

            }
        }
        if (this.XMLElementContent.equals("null")) {
            output.append(" />");
        } else {
            output.append(String.format(">%s", this.XMLElementContent));
            output.append(String.format("</%s>", this.XMLElementName));
        }
        return output.toString();
    }

}
