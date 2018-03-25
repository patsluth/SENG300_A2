// 	SENG 300 Group 14 - Project #1
//	Pat Sluth : 30032750
//	Preston : XXXXXXX
//	Aaron Hornby: 10176084

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;

public class Main 
{	
	public static void main(String[] args) 
	{	
		String directoryPath;
		String typeName;
		
		if (args.length == 2) {
			directoryPath = args[0];
			typeName = args[1];
		}

		directoryPath = "/Volumes/Malish/Development/eclipse_workspace/SENG300/jars/commons-lang-2.5.jar";
//		directoryPath = "/Volumes/Malish/Development/eclipse_workspace/SENG300/src";
		typeName = "T2";
//		else {
//			System.out.println("Usage: java Main <directoryPath> <typeName>");
//			return; 
//		}
		

		TypeCounter typeCounter = new TypeCounter(directoryPath, typeName);
		typeCounter.run();
	}
	
	
	
//	static class DeclarationVisitor extends ASTVisitor{
//		
//		public boolean visit(PackageDeclaration node) {
//			packageName = node.getName().toString();
//			return true; 
//		}
//		
//		public boolean visit(AnnotationTypeDeclaration node) {
//			
//			String fqn = this.getFQN(node, node.getName().toString());
////			String fqn = node.getName().toString();
////			
////			CompilationUnit cu = null;
////			ASTNode currentNode = node.getParent();
////			
////			while (currentNode != null && cu == null) {
////				if (currentNode instanceof CompilationUnit) {
////					cu = (CompilationUnit)currentNode;
////				} else if (currentNode instanceof TypeDeclaration) {
////					TypeDeclaration td = (TypeDeclaration)currentNode;
////					fqn = td.getName() + "." + fqn;
////				}
////				currentNode = currentNode.getParent();
////			}
////			
////			if (cu != null) {
////				if (cu.getPackage() != null) {
////					fqn = cu.getPackage().getName() + "." + fqn;
////				} 
////			}
//			
//			if (typeName.equals(fqn)) {
//				declarationCount++;
//			}
//			
//			return true;
////			System.out.println(node.getName().getFullyQualifiedName());
//		}
//		
//		
//		public boolean visit(EnumDeclaration node) {
//			
//			String fqn = this.getFQN(node, node.getName().toString());
////			String fqn = node.getName().toString();
////			
////			CompilationUnit cu = null;
////			ASTNode currentNode = node.getParent();
////			
////			while (currentNode != null && cu == null) {
////				if (currentNode instanceof CompilationUnit) {
////					cu = (CompilationUnit)currentNode;
////				} else if (currentNode instanceof TypeDeclaration) {
////					TypeDeclaration td = (TypeDeclaration)currentNode;
////					fqn = td.getName() + "." + fqn;
////				}
////				currentNode = currentNode.getParent();
////			}
////			
////			if (cu != null) {
////				if (cu.getPackage() != null) {
////					fqn = cu.getPackage().getName() + "." + fqn;
////				} 
////			}
//			
//			if (typeName.equals(fqn)) {
//				declarationCount++;
//			}
//			
//			return true; 
////			System.out.println(node.getName().getFullyQualifiedName());
//		}
//		
//		public boolean visit(TypeDeclaration node) 
//		{
//			String fqn = this.getFQN(node, node.getName().toString());
////			System.out.println(node.getName().getFullyQualifiedName());
////			
////			String fqn = node.getName().toString();
////			
////			CompilationUnit cu = null;
////			ASTNode currentNode = node.getParent();
////			
////			while (currentNode != null && cu == null) {
////				if (currentNode instanceof CompilationUnit) {
////					cu = (CompilationUnit)currentNode;
////				} else if (currentNode instanceof TypeDeclaration) {
////					TypeDeclaration td = (TypeDeclaration)currentNode;
////					fqn = td.getName() + "." + fqn;
////				}
////				currentNode = currentNode.getParent();
////			}
////			
////			if (cu != null) {
////				if (cu.getPackage() != null) {
////					fqn = cu.getPackage().getName() + "." + fqn;
////				} 
////			}
//			
//			if (typeName.equals(fqn)) {
//				declarationCount++;
//			}
//			
//			return true; 
//		}
//		
//		@Override
//		public boolean visit(FieldDeclaration node)
//		{
//			String fqn = this.getFQN(node, node.getType().toString());
//			
////			String fqn = node.getType().toString();
////			
////			CompilationUnit cu = null;
////			ASTNode currentNode = node.getParent();
////			
////			while (currentNode != null && cu == null) {
////				if (currentNode instanceof CompilationUnit) {
////					cu = (CompilationUnit)currentNode;
////				} else if (currentNode instanceof TypeDeclaration) {
////					TypeDeclaration td = (TypeDeclaration)currentNode;
////					fqn = td.getName() + "." + fqn;
////				}
////				currentNode = currentNode.getParent();
////			}
////			
////			if (cu != null) {
////				if (cu.getPackage() != null) {
////					fqn = cu.getPackage().getName() + "." + fqn;
////				} 
////			}
//			
//			if (typeName.equals(fqn)) {
//				referenceCount++;
//			}
//			
//			return true; 
//		}
//		
//		private String getFQN(ASTNode node, String simpleName) {
//			
//			String fqn = simpleName; // init fqn as inner class/interface simple name
//			
//			ASTNode parent = node.getParent(); // get either an outer class/interface/enum/annotation or the comp. unit
//			int parentType = parent.getNodeType(); 
//			while (true) { // keep appending the outer class/interface/enum/annotation names to fqn
//				String parentName; 
//				if (parentType == ASTNode.ANNOTATION_TYPE_DECLARATION)
//					parentName = ((AnnotationTypeDeclaration) parent).getName().toString();
//				else if (parentType == ASTNode.ENUM_DECLARATION)
//					parentName = ((EnumDeclaration) parent).getName().toString();
//				else if (parentType == ASTNode.TYPE_DECLARATION)
//					parentName = ((TypeDeclaration) parent).getName().toString();
//				else
//					break; 
//				
//				fqn = parentName + "." + fqn;
//				parent = parent.getParent(); // move up one parent level
//				parentType = parent.getNodeType(); // get type of new parent
//			}
//
//			if (!packageName.equals(""))
//				fqn = packageName + "." + fqn; // append the package name to front if one was explicitly declared
//			
//			return fqn; 
//		}
//		
//	}

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
