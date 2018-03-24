import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeCounter 
{
	private String directoryPath;
	private String typeName = null;
	
	private ASTParser parser = null;
	private int declarationCount = 0;
	private int referenceCount = 0;
	
	
	
	
	
	public TypeCounter(String directoryPath, String typeName)
	{
		File directoryFile = new File(directoryPath);
		if (!directoryFile.isDirectory()) {
			System.out.println("Invalid directory.");
			return;
		}
		
		this.parser = ASTParser.newParser(AST.JLS8);
		this.directoryPath = directoryPath;
		this.typeName = typeName;
	}
	
	public void run()
	{
		this.declarationCount = 0;
		this.referenceCount = 0;
		
		try {
			Files.walk(Paths.get(directoryPath))
			.filter(Files::isRegularFile)
			.forEach( (filePath) -> {
				this.parse(filePath);
	        });
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		
		System.out.println(this.typeName + ". Declarations found: " + this.declarationCount + "; references found: " + this.referenceCount + "."); 
	}
	
	private void parse(Path filePath)
	{
		File file = filePath.toFile();
		
		if (file.isDirectory()) { return; }
		
		String filename = file.getName();
		String[] parts = filename.split("\\.");
		String extension = parts[parts.length - 1]; 
		
		if (!extension.toLowerCase().equals("java") && !extension.toLowerCase().equals("jar")) { return; }
		
		try {	
			String source = readFile(file);
			
			System.out.println("Reading file: " + file.getPath());

			this.parser.setSource(source.toCharArray());
			
			Map options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options); // update compiler version to 1.5 to recognize enum declarations
			this.parser.setCompilerOptions(options);

			CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			
			cu.accept(new ASTVisitor() {
				@Override public boolean visit(SimpleName node) 
				{
					String nodeString = node.getFullyQualifiedName();
					
					if (typeName.equals(nodeString)) {
						referenceCount += 1;
					}
					
					return super.visit(node);
				}
				
				@Override public boolean visit(PrimitiveType node)
				{
					String nodeString = node.getPrimitiveTypeCode().toString();
					
					if(typeName.equals(nodeString)){
						referenceCount += 1;
					}
					
					return super.visit(node);
				}
				
				
				
				
				@Override public boolean visit(TypeDeclaration node) 
				{
					String nodeString = node.getName().getFullyQualifiedName();
					
					// counter for declaration type of interest
					if (typeName.equals(nodeString)) {
						declarationCount += 1;
					}

					return super.visit(node);
				}
				
				@Override public boolean visit(EnumDeclaration node) 
				{
					String nodeString = node.getName().getFullyQualifiedName();
					
					// counter for declaration type of interest
					if (typeName.equals(nodeString)) {
						declarationCount += 1;
					}

					return super.visit(node);
				}
				
				@Override public boolean visit(AnnotationTypeDeclaration node) 
				{
					String nodeString = node.getName().getFullyQualifiedName();
					
					// counter for declaration type of interest
					if (typeName.equals(nodeString)) {
						declarationCount += 1;
					}

					return super.visit(node);
				}
			});
			
		} catch (FileNotFoundException e) {
			System.out.println("Failed to read file: " + file.getPath());
		}
	}

	private String readFile(File file) throws FileNotFoundException
	{
	    Scanner scanner = new Scanner(file);
	    String source = "";
	
	    while (scanner.hasNextLine()) {
	    	source += scanner.nextLine() + "\n";
	    }
	    
	    scanner.close();
	
	    return source;
	}
}




