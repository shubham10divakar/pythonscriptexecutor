package io.github.shubham10divakar;

import io.github.shubham10divakar.CustomException.IncorrectFileExtensionException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PythonScriptExecutor {
    public String executePythonScriptNoArgs(String scriptPath) {

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

    /**
     * Executes a Python script with the specified arguments.
     *
     * @param scriptPath the path to the Python script
     * @param arguments  the arguments to pass to the Python script
     * @return the output of the script execution
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the process is interrupted
     */
    public String executePythonScriptWithArgs(String scriptPath, String... arguments) {
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

        // Read the output of the script
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        // Wait for the process to complete and get the exit code
        int exitCode = process.waitFor();

        // Check if the script execution was successful
        if (exitCode != 0) {
            throw new RuntimeException("Failed to execute Python script: " + output.toString());
        }

        return output.toString();
    }

    private String executeCoreLogicNoArgs(String scriptPath) throws IOException, InterruptedException {
        //String scriptPath = "src/main/resources/pythonscript/nmap_scan.py"; // Adjust the path as needed

        File scriptFile = validateFilePath(scriptPath);

        System.out.println("Using Python script at: " + scriptFile.getAbsolutePath());

        ProcessBuilder processBuilder = new ProcessBuilder("python", scriptFile.getAbsolutePath());
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

        if(!scriptPath.endsWith(".py")){
            throw new IncorrectFileExtensionException("Path is not a valid path for python file, include python(.py) file in the path in case you missed to add: " + scriptPath);
        }

        File scriptFile = new File(scriptPath);
        if (!scriptFile.exists()) {
            throw new FileNotFoundException("Python script not found at path: " + scriptPath);
        }

        return scriptFile;
    }

    /**
     * Determines the Python executable based on the operating system.
     *
     * @return the name of the Python executable
     */
    private static String getPythonExecutable() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "python";
        } else {
            return "python3";
        }
    }

    /**
     * Builds the command to execute the Python script with arguments.
     *
     * @param pythonExecutable the Python executable
     * @param scriptPath       the path to the Python script
     * @param arguments        the arguments to pass to the Python script
     * @return the command to execute the Python script
     */
    private static List<String> buildCommand(String pythonExecutable, String scriptPath, String... arguments) {
        List<String> command = new ArrayList<>();
        command.add(pythonExecutable);
        command.add(scriptPath);
        command.addAll(Arrays.asList(arguments));
        return command;
    }
}
