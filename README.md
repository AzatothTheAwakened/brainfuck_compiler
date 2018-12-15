# Brainfuck to JVM-Bytecode-compiler
This is just a simple project to compile Brainfuck to JVM Executable bytecode

program needs (jasmin.jar)[http://jasmin.sourceforge.net/] installed in the maven local repo with `mvn install:install-file -Dfile=Path/to/jasmin.jar -DgroupId=org.jasmin -DartifactId=jasmin -Dversion=2.4 -Dpackaging=jar'

use it with `java -jar bc <inputfile>`
