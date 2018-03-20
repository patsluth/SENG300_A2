// 	SENG 300 Group 14 - Project #1
//	Pat Sluth : 30032750
//	Preston : XXXXXXX
//	Aaron Hornby: 10176084

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.ObjectUtils.Null;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class Main 
{	
	private static ASTParser parser = null;
	private static int declarationCount = 0;
	private static int referenceCount = 0;
	private static String typeName = null;
	private static String packageName = null; 

	public static void main(String[] args) 
	{	
		String directoryPath;
		
		if (args.length == 2) {
			directoryPath = args[0];
			typeName = args[1];
		}
		
		directoryPath = "/Volumes/Malish/Development/eclipse_workspace/SENG300/src";
		typeName = "test.T1.T2";
//		else {
//			System.out.println("Usage: java Main <directoryPath> <typeName>");
//			return; 
//		}
		
		if (parse(directoryPath, typeName)) 
			System.out.println(typeName + ". Declarations found: " + declarationCount + "; references found: " + referenceCount + "."); 

	}
	
	private static boolean parse(String directoryPath, String typeName)
	{ 	
		File directoryFile = new File(directoryPath);
		if (!directoryFile.isDirectory()) {
			System.out.println("Invalid directory.");
			return false;
		}
		
		Main.parser = ASTParser.newParser(AST.JLS8);
		Main.parser.setKind(ASTParser.K_COMPILATION_UNIT);
		Main.typeName = typeName;
		
		try {
			Files.walk(Paths.get(directoryPath))
			.filter(Files::isRegularFile)
			.forEach(Main::parse);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		for (File file : directoryFile.listFiles()) {
			
			if (file.isDirectory())
				continue; // prevent trying to parse a sub-directory
			
			String filename = file.getName();
			String[] parts = filename.split("\\.");
			String extension = parts[parts.length - 1]; 
			
			if (!extension.toLowerCase().equals("java")) 
				continue; // prevent trying to parse a non .java file
			
			try {	
				String source = readFile(file);
				
				//System.out.println("Reading file: " + file.getPath());
	
				parser.setSource(source.toCharArray());
				
				Map options = JavaCore.getOptions();
				JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options); // update compiler version to 1.5 to recognize enum declarations
				parser.setCompilerOptions(options);
	
				CompilationUnit rootNode = (CompilationUnit) parser.createAST(null);
				
				packageName = ""; // reset the package name before parsing next file
				
				rootNode.accept(new DeclarationVisitor()); 
				
				rootNode.accept(new FieldDeclarationVisitor(new FieldDeclarationVisitor.Listener() {
					@Override
					public void didVisitNodeOfType(Type type) 
					{
						String typeName = type.toString();
						
						if (Main.typeName == null || typeName.equals(Main.typeName)) {
							//System.out.println("\t" + typeName);
//							referenceCount++; 
						}
					}
				}));
				
			} catch (FileNotFoundException e) {
				System.out.println("Failed to read file: " + file.getPath());
				return false; 
			}
		}
		
		return true; 
	}
	
	private static void parse(Path filePath)
	{
		File file = filePath.toFile();
		
		if (file.isDirectory()) { return; }
		
		String filename = file.getName();
		String[] parts = filename.split("\\.");
		String extension = parts[parts.length - 1]; 
		
		if (!extension.toLowerCase().equals("java") && !extension.toLowerCase().equals("jar")) { return; }
		
		try {	
			String source = readFile(file);
			
			//System.out.println("Reading file: " + file.getPath());

			Main.parser.setSource(source.toCharArray());
			
			Map options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options); // update compiler version to 1.5 to recognize enum declarations
			Main.parser.setCompilerOptions(options);

			CompilationUnit rootNode = (CompilationUnit) parser.createAST(null);
			
			packageName = ""; // reset the package name before parsing next file
			
			rootNode.accept(new DeclarationVisitor()); 
			
			rootNode.accept(new FieldDeclarationVisitor(new FieldDeclarationVisitor.Listener() {
				@Override
				public void didVisitNodeOfType(Type type) 
				{
					String typeName = type.toString();
					
					if (Main.typeName == null || typeName.equals(Main.typeName)) {
						//System.out.println("\t" + typeName);
//						referenceCount++; 
					}
				}
			}));
			
		} catch (FileNotFoundException e) {
			System.out.println("Failed to read file: " + file.getPath());
		}
	}

	private static String readFile(File file) throws FileNotFoundException
	{
	    Scanner scanner = new Scanner(file);
	    String source = "";
	
	    while (scanner.hasNextLine()) {
	    	source += scanner.nextLine() + "\n";
	    }
	    
	    scanner.close();
	
	    return source;
	}
	
	static class DeclarationVisitor extends ASTVisitor{
		
		public boolean visit(PackageDeclaration node) {
			packageName = node.getName().toString();
			return true; 
		}
		
		public boolean visit(AnnotationTypeDeclaration node) {
			
			String fqn = this.getFQN(node, node.getName().toString());
//			String fqn = node.getName().toString();
//			
//			CompilationUnit cu = null;
//			ASTNode currentNode = node.getParent();
//			
//			while (currentNode != null && cu == null) {
//				if (currentNode instanceof CompilationUnit) {
//					cu = (CompilationUnit)currentNode;
//				} else if (currentNode instanceof TypeDeclaration) {
//					TypeDeclaration td = (TypeDeclaration)currentNode;
//					fqn = td.getName() + "." + fqn;
//				}
//				currentNode = currentNode.getParent();
//			}
//			
//			if (cu != null) {
//				if (cu.getPackage() != null) {
//					fqn = cu.getPackage().getName() + "." + fqn;
//				} 
//			}
			
			if (typeName.equals(fqn)) {
				declarationCount++;
			}
			
			return true;
//			System.out.println(node.getName().getFullyQualifiedName());
		}
		
		
		public boolean visit(EnumDeclaration node) {
			
			String fqn = this.getFQN(node, node.getName().toString());
//			String fqn = node.getName().toString();
//			
//			CompilationUnit cu = null;
//			ASTNode currentNode = node.getParent();
//			
//			while (currentNode != null && cu == null) {
//				if (currentNode instanceof CompilationUnit) {
//					cu = (CompilationUnit)currentNode;
//				} else if (currentNode instanceof TypeDeclaration) {
//					TypeDeclaration td = (TypeDeclaration)currentNode;
//					fqn = td.getName() + "." + fqn;
//				}
//				currentNode = currentNode.getParent();
//			}
//			
//			if (cu != null) {
//				if (cu.getPackage() != null) {
//					fqn = cu.getPackage().getName() + "." + fqn;
//				} 
//			}
			
			if (typeName.equals(fqn)) {
				declarationCount++;
			}
			
			return true; 
//			System.out.println(node.getName().getFullyQualifiedName());
		}
		
		public boolean visit(TypeDeclaration node) 
		{
			String fqn = this.getFQN(node, node.getName().toString());
//			System.out.println(node.getName().getFullyQualifiedName());
//			
//			String fqn = node.getName().toString();
//			
//			CompilationUnit cu = null;
//			ASTNode currentNode = node.getParent();
//			
//			while (currentNode != null && cu == null) {
//				if (currentNode instanceof CompilationUnit) {
//					cu = (CompilationUnit)currentNode;
//				} else if (currentNode instanceof TypeDeclaration) {
//					TypeDeclaration td = (TypeDeclaration)currentNode;
//					fqn = td.getName() + "." + fqn;
//				}
//				currentNode = currentNode.getParent();
//			}
//			
//			if (cu != null) {
//				if (cu.getPackage() != null) {
//					fqn = cu.getPackage().getName() + "." + fqn;
//				} 
//			}
			
			if (typeName.equals(fqn)) {
				declarationCount++;
			}
			
			return true; 
		}
		
		@Override
		public boolean visit(FieldDeclaration node)
		{
			String fqn = this.getFQN(node, node.getType().toString());
			
//			String fqn = node.getType().toString();
//			
//			CompilationUnit cu = null;
//			ASTNode currentNode = node.getParent();
//			
//			while (currentNode != null && cu == null) {
//				if (currentNode instanceof CompilationUnit) {
//					cu = (CompilationUnit)currentNode;
//				} else if (currentNode instanceof TypeDeclaration) {
//					TypeDeclaration td = (TypeDeclaration)currentNode;
//					fqn = td.getName() + "." + fqn;
//				}
//				currentNode = currentNode.getParent();
//			}
//			
//			if (cu != null) {
//				if (cu.getPackage() != null) {
//					fqn = cu.getPackage().getName() + "." + fqn;
//				} 
//			}
			
			if (typeName.equals(fqn)) {
				referenceCount++;
			}
			
			return true; 
		}
		
		private String getFQN(ASTNode node, String simpleName) {
			
			String fqn = simpleName; // init fqn as inner class/interface simple name
			
			ASTNode parent = node.getParent(); // get either an outer class/interface/enum/annotation or the comp. unit
			int parentType = parent.getNodeType(); 
			while (true) { // keep appending the outer class/interface/enum/annotation names to fqn
				String parentName; 
				if (parentType == ASTNode.ANNOTATION_TYPE_DECLARATION)
					parentName = ((AnnotationTypeDeclaration) parent).getName().toString();
				else if (parentType == ASTNode.ENUM_DECLARATION)
					parentName = ((EnumDeclaration) parent).getName().toString();
				else if (parentType == ASTNode.TYPE_DECLARATION)
					parentName = ((TypeDeclaration) parent).getName().toString();
				else
					break; 
				
				fqn = parentName + "." + fqn;
				parent = parent.getParent(); // move up one parent level
				parentType = parent.getNodeType(); // get type of new parent
			}

			if (!packageName.equals(""))
				fqn = packageName + "." + fqn; // append the package name to front if one was explicitly declared
			
			return fqn; 
		}
		
	}

}



class FieldDeclarationVisitor extends ASTVisitor
{	
	interface Listener
	{
		void didVisitNodeOfType(Type type);
	}
	
	private FieldDeclarationVisitor.Listener listener = null;
	
	
	public FieldDeclarationVisitor(FieldDeclarationVisitor.Listener listener) 
	{
		this.listener = listener;
	}
	
	@Override
	public boolean visit(FieldDeclaration node) 
	{
		if (this.listener != null) {
			this.listener.didVisitNodeOfType(node.getType());
		}
		
		return super.visit(node);
	}
}
