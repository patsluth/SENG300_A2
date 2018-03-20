
package test;
//[test.T1, test.T1.T2]
//
//        
//        fn (refname):
//            if refername == T1 -> test.T1
//            else if refername == T2 -> test.T1.T2
        

public class T1 {
    T1 t = new T1(); 
    T2 q = new T2(); 
    
    private class T2 {
        T1 x = new T1(); 
        T2 z = new T2(); 
    }
    
    /*
    class T1c {}
    interface T1i {}
    enum T1e {}
    @interface T1a {}
    */
}