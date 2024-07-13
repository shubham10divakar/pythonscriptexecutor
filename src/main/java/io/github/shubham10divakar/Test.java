package io.github.shubham10divakar;

public class Test {
    public static void main(String[] args) {
            PythonScriptExecutor obj = new PythonScriptExecutor();
            //display(obj.executePythonScriptNoArgs("C:\\D\\my docs\\my docs\\projects\\springbootnetworkproject\\PythonScriptExecutor\\test_python_script.py"));
            display(obj.executePythonScriptWithArgs("C:\\D\\my docs\\my docs\\projects\\springbootnetworkproject\\PythonScriptExecutor\\test_python_script.py", "From Subham"));
    }

    public static void display(String str){
        System.out.println(str);
    }
}
