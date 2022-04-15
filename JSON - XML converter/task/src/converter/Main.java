package converter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        StringBuilder input = new StringBuilder();
        try {
            File file = new File("test.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                input.append(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        if (input.toString().startsWith("<")) {

            // XML to JSON
            XMLJSONConverter converter = new XMLJSONConverter(input.toString());
//            System.out.println(input);
            System.out.println(converter.getJSON());

        } else if (input.toString().startsWith("{")) {

            // JSON to XML
            JSONXMLConverter converter = new JSONXMLConverter(input.toString().replaceAll("\\s",""));
//            System.out.println(input.replaceAll("\\s",""));
            System.out.println(converter.getXML());

        }
    }
}
