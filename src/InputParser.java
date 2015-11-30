import java.awt.List;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class InputParser {
	
	public static List readFile(String pathString) throws IOException{
		Path path = Paths.get(pathString);
		return (List) Files.readAllLines(path, StandardCharsets.UTF_8);
	}
}
