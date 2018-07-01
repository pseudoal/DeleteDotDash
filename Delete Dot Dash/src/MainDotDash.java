import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.FileVisitResult.*;

public class MainDotDash {

	private static final String INPUT_PATH = "/Volumes/TOSHIBA EXT/MUSIK/Music Media";
	//private static final String INPUT_PATH = "/Users/alan/Dropbox";
	private static final String regex = "._";
    private static ArrayList<String> deletedNames;
	
	public static void main(String[] args) {
		deletedNames = new ArrayList<String>();
		listf(INPUT_PATH);
		if (deletedNames.size() == 1) {
		    System.out.println("Done deleting files. " + deletedNames.size() + " file was deleted.");	
		} else {
		    System.out.println("Done deleting files. " + deletedNames.size() + " files were deleted.");
		}
	}
	
	/**
	 * Search through specified input directory and its sub folders.
	 * 
	 * @param directoryName The name of the input directory.
	 */
	public static void listf(String directoryName) {
	    File directory = new File(directoryName);
	    
	    // Get all the files from a directory.
	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isFile() && file.getName().contains(regex) && file.getName().contains(".mp3")) {
	            try {
	            	System.out.println("Deleting " + file.getAbsolutePath());
					deleteFileOrFolder(file.toPath());
					deletedNames.add(file.getName());
				} catch (IOException e) {
					throw new RuntimeException("Could not delete file " + file.getName() + ": " + e.getMessage());
				}
	        } else if (file.isDirectory()) {
	            listf(file.getAbsolutePath());
	        }
	    }
	}
	
	/**
	 * Delete a file or folder at a specified path.
	 * 
	 * @param path The path to the file or folder.
	 * @throws IOException
	 */
	public static void deleteFileOrFolder(final Path path) throws IOException {
		  Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
		    @Override public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
		      throws IOException {
		      Files.delete(file);
		      return CONTINUE;
		    }

		    @Override public FileVisitResult visitFileFailed(final Path file, final IOException e) {
		      return handleException(e);
		    }

		    private FileVisitResult handleException(final IOException e) {
		      e.printStackTrace(); // replace with more robust error handling
		      return TERMINATE;
		    }

		    @Override public FileVisitResult postVisitDirectory(final Path dir, final IOException e)
		      throws IOException {
		      if(e!=null)return handleException(e);
		      Files.delete(dir);
		      return CONTINUE;
		    }
		  });
		};

}
