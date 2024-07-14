package io.github.shubham10divakar;

import io.github.shubham10divakar.internal.CustomException.IncorrectFileExtensionException;
import io.github.shubham10divakar.internal.Logger.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class PythonScriptExecutor {

    public String executePythonScriptNoArgsSync(String scriptPath, boolean loggingState, Level loggLevel) {

        Logger.setLoggingEnabled(loggingState);
        Logger.setLogLevel(loggLevel);

        String output = null;
        try {
            output = executeCoreLogicNoArgs(scriptPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println(e);
        }

        if(output==null){
            return "Some Issue Was Encountered, please investigate.";
        }
        return output;
    }
    public String executePythonScriptNoArgsASync(String scriptPath, boolean loggingState, Level loggLevel) {

        Logger.setLoggingEnabled(loggingState);
        Logger.setLogLevel(loggLevel);

        if(Logger.isLoggingEnabled()){
            Logger.log(Level.INFO,"ASync Execution......");
        }

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> executePythonScriptNoArgsSync(scriptPath, loggingState, loggLevel));

        while (!completableFuture.isDone()) {
            if(Logger.isLoggingEnabled()){
                Logger.log(Level.INFO,"ASync Execution is not finished yet......");
            }
        }

        String result = null;
        try{
            result = completableFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(result==null){
            return "Some Issue Was Encountered, please investigate.";
        }
        return result;
    }


    public String executePythonScriptWithArgsSync(String scriptPath, boolean loggingState, Level loggLevel,String... arguments) {
        Logger.setLoggingEnabled(loggingState);
        Logger.setLogLevel(loggLevel);

        String output = null;
        try {
            output = executeCoreLogicWithArgs(scriptPath, arguments);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println(e);
        }

        if(output==null){
            return "Some Issue Was Encountered, please investigate.";
        }
        return output;
    }

    public String executePythonScriptWithArgsASync(String scriptPath, boolean loggingState, Level loggLevel, String... arguments) {

        Logger.setLoggingEnabled(loggingState);
        Logger.setLogLevel(loggLevel);

        if(Logger.isLoggingEnabled()){
            Logger.log(Level.INFO,"ASync Execution......");
        }

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> executePythonScriptWithArgsSync(scriptPath, loggingState, loggLevel, arguments));

        while (!completableFuture.isDone()) {
            if(Logger.isLoggingEnabled()){
                Logger.log(Level.INFO,"ASync Execution is not finished yet......");
            }
            try {
                // Sleep for 4 seconds
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String result = null;
        try{
            result = completableFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(result==null){
            return "Some Issue Was Encountered, please investigate.";
        }
        return result;
    }

    private String executeCoreLogicWithArgs(String scriptPath, String... arguments) throws IOException, InterruptedException {
        validateFilePath(scriptPath);

        // Determine the Python executable based on the operating system
        String pythonExecutable = getPythonExecutable();

        // Create the command to execute the Python script with arguments
        List<String> command = buildCommand(pythonExecutable, scriptPath, arguments);

        // Start the process and capture its output
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        if(Logger.isLoggingEnabled()){
            Logger.log(Level.INFO,"Execution is in Progress......");
        }

        // Read the output of the script
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        //System.out.println("Execution is in Progress......");
        // Wait for the process to complete and get the exit code
        int exitCode = process.waitFor();
        if(Logger.isLoggingEnabled()){
            Logger.log(Level.INFO,"Execution is Completed......");
        }
        //System.out.println("Execution is Completed......");

        // Check if the script execution was successful
        if (exitCode != 0) {
            throw new RuntimeException("Failed to execute Python script: " + output.toString());
        }

        return output.toString();
    }

    private String executeCoreLogicNoArgs(String scriptPath) throws IOException, InterruptedException {
        //String scriptPath = "src/main/resources/pythonscript/nmap_scan.py"; // Adjust the path as needed
        File scriptFile = validateFilePath(scriptPath);

        // Determine the Python executable based on the operating system
        String pythonExecutable = getPythonExecutable();

        if(Logger.isLoggingEnabled()){
            Logger.log(Level.INFO,"Using Python script at: " + scriptFile.getAbsolutePath());
        }
        //System.out.println("Using Python script at: " + scriptFile.getAbsolutePath());

        if(Logger.isLoggingEnabled()){
            Logger.log(Level.INFO,"Execution is in Progress......");
        }
        ProcessBuilder processBuilder = new ProcessBuilder(pythonExecutable, scriptFile.getAbsolutePath());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();
        //boolean scanSuccess = false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
//                if (line.contains("Scan completed successfully.")) {
//                    scanSuccess = true;
//                }
                if (line.contains("Error:")) {
                    errorOutput.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading output from Python script", e);
        }

        int exitCode = process.waitFor();
        if(Logger.isLoggingEnabled()){
            Logger.log(Level.INFO,"Execution is Completed......");
        }

        if (exitCode != 0) {
            String errorMessage = "Failed with exit code " + exitCode + ". Output: " + output.toString();
            if (errorOutput.length() > 0) {
                errorMessage += " Error Output: " + errorOutput.toString();
            }
            throw new RuntimeException(errorMessage);
        }

        return output.toString();  // This should return JSON data from the Python script
    }

    private File validateFilePath(String scriptPath) throws FileNotFoundException {
        if (Logger.isLoggingEnabled() && Logger.getLogLevel()==Level.SEVERE) {
            Logger.log(Level.SEVERE,"Validating File Path.....");
        }

        if(!scriptPath.endsWith(".py")){
            throw new IncorrectFileExtensionException("Path is not a valid path for python file, include python(.py) file in the path in case you missed to add: " + scriptPath);
        }

        File scriptFile = new File(scriptPath);
        if (!scriptFile.exists()) {
            throw new FileNotFoundException("Python script not found at path: " + scriptPath);
        }

        if (Logger.isLoggingEnabled() && Logger.getLogLevel()==Level.SEVERE) {
            Logger.log(Level.SEVERE,"Validating File Path Complete.....");
        }

        return scriptFile;
    }


    private static String getPythonExecutable() {
        if (Logger.isLoggingEnabled() && Logger.getLogLevel()==Level.SEVERE) {
            Logger.log(Level.SEVERE,"Fetching OS type.....");
        }
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            if (Logger.isLoggingEnabled() && Logger.getLogLevel()==Level.SEVERE) {
                Logger.log(Level.SEVERE,"OS type is Windows.....will be using python");
            }
            return "python";
        } else {
            if (Logger.isLoggingEnabled() && Logger.getLogLevel()==Level.SEVERE) {
                Logger.log(Level.SEVERE,"Other OS type.....will be using python3");
            }
            return "python3";
        }
    }


    private static List<String> buildCommand(String pythonExecutable, String scriptPath, String... arguments) {
        if(Logger.isLoggingEnabled() && Logger.getLogLevel()==Level.SEVERE){
            Logger.log(Level.SEVERE,"Building Command......");
        }

        List<String> command = new ArrayList<>();
        command.add(pythonExecutable);
        command.add(scriptPath);
        command.addAll(Arrays.asList(arguments));

        if(Logger.isLoggingEnabled() && Logger.getLogLevel()==Level.SEVERE){
            Logger.log(Level.SEVERE,"Building Command Completed......");
        }

        return command;
    }
}
