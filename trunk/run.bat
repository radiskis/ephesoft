set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_12
set M2_HOME=D:\Softwares\apache-maven-2.0.9
set DCMA_HOME=D:\Eclipse-Workspaces\EphesoftFinalWorkspace\dcma-root
set IM4JAVA_TOOLPATH=C:/Program Files/ImageMagick-6.6.0-Q8
set TESSERACT_PATH=D:\Eclipse-Workspaces\EphesoftFinalWorkspace\dcma-root\native\EphesftTesseractConsole\EphesftTesseractConsole\bin\x86\Release
set RECOSTAR_PATH=D:\Eclipse-Workspaces\EphesoftFinalWorkspace\dcma-root\native\RunProjectCpp\release

set MAVEN_OPTS=-Djava.library.path=%DCMA_HOME%\native -Ddcma.home=%DCMA_HOME%
set JAVA=%JAVA_HOME%\bin
set path=%path%;%JAVA%
set M2=%M2_HOME%\bin
set path=%path%;%M2%

mvn -f %DCMA_HOME%\dcma-webapp\pom.xml -o test -Pstand-alone
