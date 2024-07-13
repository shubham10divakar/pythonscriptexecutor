package io.github.shubham10divakar;

import java.util.logging.Level;

public class Test {
    public static void main(String[] args) {
            PythonScriptExecutor obj = new PythonScriptExecutor();
            //display(obj.executePythonScriptNoArgs("C:\\D\\my docs\\my docs\\projects\\springbootnetworkproject\\PythonScriptExecutor\\test_python_script.py", true, Level.INFO));
            //display(obj.executePythonScriptWithArgs("C:\\D\\my docs\\my docs\\projects\\springbootnetworkproject\\PythonScriptExecutor\\test_python_script.py", "From Subham"));
        //display(obj.executePythonScriptWithArgs("C:\\D\\my docs\\my docs\\projects\\springbootnetworkproject\\PythonScriptExecutor\\nmap_scan.py", true, Level.WARNING,"google.com", "-sV --script ssl-enum-ciphers"));
        //display(obj.executePythonScriptWithArgs("C:\\D\\my docs\\my docs\\projects\\springbootnetworkproject\\PythonScriptExecutor\\read_csv_data.py", true, Level.WARNING,"C:\\D\\my docs\\my docs\\projects\\springbootnetworkproject\\PythonScriptExecutor\\Airplane_Crashes_and_Fatalities_Since_1908.csv"));

        //For fetching csv data from python fileasdsadasd
        //String obj1 = obj.executePythonScriptWithArgs("C:\\D\\my docs\\my docs\\projects\\springbootnetworkproject\\PythonScriptExecutor\\read_csv_data.py", true, Level.SEVERE,"C:\\D\\my docs\\my docs\\projects\\springbootnetworkproject\\PythonScriptExecutor\\Airplane_Crashes_and_Fatalities_Since_1908.csv");
        //display(obj1);
    }

    public static void display(String str){
        System.out.println(str);
    }
}
