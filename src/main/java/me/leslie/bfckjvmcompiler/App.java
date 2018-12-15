package me.leslie.bfckjvmcompiler;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.LinkedList;




import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import jasmin.ClassFile;

public class App 
{

    private int maxCells;

    public App(int maxCells){
        this.maxCells = maxCells;
    }

    public static void main(String[] args) throws Exception {
        if(args.length == 0){
            System.out.println("Please use the syntax 'java -jar bc.jar <inputfile>'");
            return;
        }

        if(args.length > 1){
            System.out.println("Only one file at a time supported currently.");
            return;
        }

        try{
            App instance = new App(256);
            instance.start(args);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public void start(String[] args) throws Exception{
        Path infile = Paths.get(args[0]);
        System.out.println(infile.toString());
        String filename = infile.getFileName().toString();
        filename = filename.substring(0, filename.lastIndexOf('.'));
        

        List<String> asm = compileToAsm(infile);


        asm = insertIntoSkeleton(asm, filename, maxCells);

        ClassFile cf = new ClassFile();

        final StringBuilder sb = new StringBuilder();

        asm.forEach(x -> sb.append(x).append(System.lineSeparator()));

        cf.readJasmin(new StringReader(sb.toString()), "", false);

        Path outfile = Paths.get(System.getProperty("user.dir"), cf.getClassName() + ".class");

        cf.write(Files.newOutputStream(outfile));

    }

    public List<String> insertIntoSkeleton(List<String> insert, String filename, int maxCells){

        if(maxCells < 1){
            throw new IllegalStateException("maxSectors have to be at least 1");
        }

        List<String> skeleton = new LinkedList<>();

        skeleton.add("; setup JVM");
        skeleton.add(".class public " + filename);
        skeleton.add(".super java/lang/Object");        
        skeleton.add(".method public static main([Ljava/lang/String;)V");
        skeleton.add(".limit stack " + 100);
        skeleton.add(".limit locals " + 3);
        skeleton.add(" ");

        skeleton.add("; setup cells");
        skeleton.add("ldc " + maxCells);
        skeleton.add("newarray int");
        skeleton.add("astore_0");
        skeleton.add("ldc 0");
        skeleton.add("istore_1");
        skeleton.add(" ");

        insert.forEach(skeleton::add);
        skeleton.add(" ");

        skeleton.add("return");
        skeleton.add(" ");
        
        skeleton.add(".end method");

        return skeleton;
    }



    private List<String> compileToAsm(Path f) throws Exception{

        System.out.println("Compiling <" +f.toString() + ">");

        CharStream cs = CharStreams.fromFileName(f.toString());
        BrainfuckLexer lexer = new BrainfuckLexer(cs);
        CommonTokenStream ts = new CommonTokenStream(lexer);

        BrainfuckParser parser = new BrainfuckParser(ts);
        ParseTree pt = parser.prog();

        BfckVisitor v = new BfckVisitor();
        List<String> asm = v.visit(pt);
        return asm;

    }



}
