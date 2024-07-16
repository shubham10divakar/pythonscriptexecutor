# PythonScriptExecutor

**PythonScriptExecutor** is a Java library that allows you to run Python scripts from within your Java code and gather their results for use in your applications. It provides both synchronous and asynchronous methods to execute the scripts, with support for logging and custom exceptions.

## Features

- **Synchronous Execution**: Run Python scripts and get results directly.
- **Asynchronous Execution**: Run Python scripts without blocking the main thread, using `CompletableFuture`.
- **Logging**: Enable or disable logging and set log levels to monitor script execution.
- **Custom Exceptions**: Handle invalid file paths and extensions gracefully.
- **Cross-Platform Support**: Automatically detects the operating system and uses the appropriate Python executable (`python` for Windows, `python3` for other OS).

## Installation

Add the following dependency to your `pom.xml` if you are using Maven:

```xml
<dependency>
    <groupId>io.github.shubham10divakar</groupId>
    <artifactId>pythonscriptexecutor</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

# Note
Ensure your Python scripts (script.py in this case) have a __main__ section defined as shown in the example below. This ensures compatibility and proper execution when using the PythonScriptExecutor library in your Java code else it wont run.
```code
# Example Python script: script.py
def main():
    # Your script logic here
    print("Hello from Python!")

if __name__ == "__main__":
    main()
```

## Synchronous Execution without Arguments
```code
import io.github.shubham10divakar.PythonScriptExecutor;

public class Main {
    public static void main(String[] args) {
        PythonScriptExecutor executor = new PythonScriptExecutor();
        String output = executor.executePythonScriptNoArgsSync("path/to/script.py", true, Level.INFO);
        System.out.println(output);
    }
}
```

## Asynchronous Execution without Arguments
```code
import io.github.shubham10divakar.PythonScriptExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) {
        PythonScriptExecutor executor = new PythonScriptExecutor();
        String output = executor.executePythonScriptNoArgsASync("path/to/script.py", true, Level.INFO);
        System.out.println(output);

    }
}
```

## Synchronous Execution with Arguments
```code
import io.github.shubham10divakar.PythonScriptExecutor;

public class Main {
    public static void main(String[] args) {
        PythonScriptExecutor executor = new PythonScriptExecutor();
        String output = executor.executePythonScriptWithArgsSync("path/to/script.py", true, Level.INFO, "arg1", "arg2");
        System.out.println(output);
    }
}
```

## ASynchronous Execution with Arguments
```code
import io.github.shubham10divakar.PythonScriptExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) {
        PythonScriptExecutor executor = new PythonScriptExecutor();
        CompletableFuture<String> future = executor.executePythonScriptWithArgsAsync("path/to/script.py", true, Level.INFO, "arg1", "arg2");
        
        future.thenAccept(output -> {
            System.out.println("Script output: " + output);
        }).exceptionally(ex -> {
            System.err.println("Script execution failed: " + ex.getMessage());
            return null;
        });
    }
}
```

## Logging
## Enable or disable logging and set the logging level to track the script execution process.
```code
import io.github.shubham10divakar.internal.Logger.Logger;

public class Main {
    public static void main(String[] args) {
        Logger.setLoggingEnabled(true);
        Logger.setLogLevel(Level.INFO);
    }
}

```

## License
This project is licensed under the MIT License - see the LICENSE file for details.


## About Me
I am Subham Divakar and I am the developer of multiple Java, Python and React libraries.
Check out my worksamples on my portfolio site.

## Connect with Me

    LinkedIn: https://www.linkedin.com/in/subham-divakar-a7420a12a/
    GitHub: https://github.com/shubham10divakar
    Portfolio: https://shubham10divakar.github.io/showcasehub/

## Feedback

Your feedback is important! Please share your thoughts and suggestions.
License

This project is licensed under the MIT License.