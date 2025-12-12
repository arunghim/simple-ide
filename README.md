# SimpleIDE

**SimpleIDE** is a minimal, lightweight Integrated Development Environment (IDE) for editing, formatting, and executing custom programs with associated input data. It features a clean dark/light interface and supports managing multiple code and input files simultaneously.

This IDE uses a **custom programming language** developed alongside its **interpreter**, which you can access [here](https://github.com/arunghim/code-interpreter). The interpreter handles:

- **Tokenizing** source code into meaningful elements (keywords, operators, identifiers, literals).
- **Parsing** tokens into an Abstract Syntax Tree (AST).
- **Executing** statements, loops, conditionals, and I/O operations.
- **Pretty printing** programs in a standardized, readable format.

---

## Features

### **Code Editor**
- Syntax-aware editing for custom language programs.
- Supports multiple code files with a file explorer.
- Automatic formatting of programs for readability.

### **Input Data Editor**
- Separate editor for program input data.
- Supports multiple input files.
- Allows switching between data files without losing content.

### **Console Output**
- Dedicated console panel for program output.
- Captures both standard output and error streams.
- Updates in real-time.

### **File Explorer**
- Navigate between code and input files.
- Supports creation of new code (`📝`) or input (`📊`) files.
- Visual icons distinguish code vs. input files.

### **Execution & Formatting**
- Execute programs with the selected input file.
- Format programs using the built-in pretty printer.
- Supports multiple executions without losing input data.

### **Theme Support**
- Toggle between **dark** and **light** themes.
- Consistent color schemes for editors, console, and file explorer.

---

## User Interface Overview

- **Top Ribbon:**  
  Buttons for **Execute**, **Format**, **New Code**, **New Input**, and **Theme Toggle**.  
  Ribbon buttons respond to hover and pressed states.

- **File Explorer:**  
  Displays all code and input files. Click to open in the corresponding editor.

- **Editor Panels:**  
  Two horizontally split editors: **Code (left)**, **Input Data (right)**.  
  Supports editing, saving, and switching files dynamically.

- **Console Panel:**  
  Vertically split below editors. Displays program output and errors.

---

## How it Works

1. **Program Parsing**  
   The IDE sends the **currently opened code file** and **currently opened input file** to the `Parser` class. The parser tokenizes the source, builds an Abstract Syntax Tree (AST), and executes the program.

2. **Input Handling**  
   Input data is read from a `Scanner` tied to the currently selected input file. Supports multiple runs by resetting the scanner before each execution.

3. **Output**  
   Program output is redirected to the console in real-time. Any errors during parsing or execution are captured and displayed clearly in the console.

4. **Formatting**  
   Uses the parser's pretty printer to produce a clean, indented, and standardized layout of the **currently opened code file**, without altering the input file.


---

## Technology Stack

- **JavaFX** – GUI components
- **Java 11+** – Core application logic
- [**Custom Parser & Interpreter**](https://github.com/arunghim/code-interpreter) – Program validation and execution

---

## Usage

1. Launch the application.
2. Create a new code file (`📝`) or input file (`📊`) via the top ribbon.
3. Write or paste your code and input data in the respective editors.
4. Select the code file and input file to run.
5. Click **EXECUTE** to run the program.
6. View output and errors in the console.
7. Optionally, click **FORMAT** to clean up your code layout.
8. Toggle **THEME** to switch between dark and light modes.

---

## Demo Video
[Godot Demo Video](https://github.com/user-attachments/assets/a6796c03-2f82-446d-a85a-c6f74ef0401f](https://private-user-images.githubusercontent.com/229193054/526002003-96a90053-136d-4c8d-a279-0bcd233d4f4c.mov?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NjU1NjE5ODEsIm5iZiI6MTc2NTU2MTY4MSwicGF0aCI6Ii8yMjkxOTMwNTQvNTI2MDAyMDAzLTk2YTkwMDUzLTEzNmQtNGM4ZC1hMjc5LTBiY2QyMzNkNGY0Yy5tb3Y_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUxMjEyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MTIxMlQxNzQ4MDFaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0xOThmZjAyYzFmNzFkMDJjY2RlZmFkZDkyYWQwNWM0OGMzMjFlZjMyMjBkZWQyYzU2ZDdlZTQ4Zjc0M2RjM2JlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.HGpwO2TEGoKiaM_4uvFFsWpX2epNRHoFMDMqCNtHC0k))
