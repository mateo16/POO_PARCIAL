@echo off
REM Set the source and output directories
set BIN_DIR=bin
set JAVAFX_LIB_PATH=C:/Users/Usuario/openjfx-22.0.2_windows-x64_bin-sdk/javafx-sdk-22.0.2/lib

REM Create bin folder if it doesn't exist
if not exist "%BIN_DIR%" (
    mkdir "%BIN_DIR%"
)

REM Compile Java files
javac --module-path "%JAVAFX_LIB_PATH%" --add-modules javafx.controls,javafx.fxml -d "%BIN_DIR%" "frontend/AppLauncher.java"

REM Run the compiled Java application
java --module-path "%JAVAFX_LIB_PATH%" --add-modules javafx.controls,javafx.fxml -cp "%BIN_DIR%" frontend.AppLauncher

REM Pause the terminal to see any errors or output
pause
